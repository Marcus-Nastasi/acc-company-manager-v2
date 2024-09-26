<template>
   <div>
      <v-snackbar
         variant="flat"
         color="success"
         v-model="snackbarSuccess"
         :timeout="3000"
         style="margin-bottom: 5rem;"
      >
         Operação concluida com sucesso
      </v-snackbar>
      <v-snackbar
         variant="flat"
         color="red"
         v-model="snackbarError"
         :timeout="3000"
         style="margin-bottom: 5rem;"
      >
         {{ errorMessage }}
      </v-snackbar>
      <SearchEmp
         :fetchEmpresas="fetchEmpresas" 
         v-model:name="name"
         v-model:cnpj="cnpj"
         v-model:cep="cep"
         style="margin-bottom: 2rem;" 
      />
     <v-data-table
       :headers="headers"
       :items="empresas"
       :page.sync="page"
       :items-per-page="size"
       :server-items-length="totalEmpresas"
     >
       <template v-slot:top>
         <v-toolbar flat>
           <v-toolbar-title>Empresas</v-toolbar-title>
           <v-divider class="mx-4" inset vertical></v-divider>
           <v-spacer></v-spacer>
           <v-dialog v-model="dialog" max-width="500px">
             <template v-slot:activator="{ props }">
               <v-btn class="mr-4" color="success" variant="tonal" dark v-bind="props">
                 Nova Empresa
               </v-btn>
             </template>
             <v-card>
               <v-card-title>
                 <span class="text-h5">{{ formTitle }}</span>
               </v-card-title>
 
               <v-card-text>
                 <v-container>
                   <v-row>
                     <v-col cols="12">
                       <v-text-field
                         v-model="editedItem.cnpj"
                         label="CNPJ"
                         required
                       ></v-text-field>
                     </v-col>
                     <v-col cols="12">
                       <v-text-field
                         v-model="editedItem.nome_fantasia"
                         label="Nome Fantasia"
                         required
                       ></v-text-field>
                     </v-col>
                     <v-col cols="12">
                       <v-text-field
                         v-model="editedItem.cep"
                         label="CEP"
                         required
                       ></v-text-field>
                     </v-col>
                   </v-row>
                 </v-container>
               </v-card-text>
 
               <v-card-actions>
                 <v-spacer></v-spacer>
                 <v-btn color="red" variant="text" @click="close">
                   Cancelar
                 </v-btn>
                 <v-btn color="blue-darken-1" variant="tonal" @click="save">
                   Salvar
                 </v-btn>
               </v-card-actions>
             </v-card>
           </v-dialog>
         </v-toolbar>
       </template>
 
       <template v-slot:item.actions="{ item }">
         <div class=" w-full flex justify-around items-center">
            <v-icon class="me-2" size="small" @click="editItem(item)">
               mdi-pencil
            </v-icon>
            <v-icon size="small" @click="deleteItem(item)">
               mdi-delete
            </v-icon>
            <v-btn size="small" @click="verDetalhes(item)" color="info" variant="text">
               Ver Detalhes
            </v-btn>
         </div>
       </template>
     </v-data-table>
      <v-pagination
       v-model="page"
       :length="totalPaginas"
       @input="fetchEmpresas"
       @click="fetchEmpresas"
     ></v-pagination>
   </div>
</template>

<script lang="ts">
import { EmpresaFornResponseDTO, EmpresaPagFornResponseDTO, EmpresaRequestDTO } from '@/interfaces/Empresa/EmpresaDTO';
import SearchEmp from './SearchEmp.vue';
import { EmpresaService } from '@/services/empresas/EmpresaService';

export default {
   components: {
      SearchEmp
   },

   data() {
      return {
         dialog: false,
         headers: [
            { text: "ID", value: "id" },
            { text: "CNPJ", value: "cnpj" },
            { text: "Nome Fantasia", value: "nome_fantasia" },
            { text: "CEP", value: "cep" },
            { text: "Ações", value: "actions", sortable: false },
         ],
         empresas: [],
         editedIndex: -1,
         editedItem: {
            id: '',
            cnpj: '',
            nome_fantasia: '',
            cep: '',
         },
         defaultItem: {
            id: '',
            cnpj: '',
            nome_fantasia: '',
            cep: '',
         },
         page: 1,
         size: 10,
         totalPaginas: 0,
         totalEmpresas: 0,
         snackbarSuccess: false,
         snackbarError: false,
         name: '',
         cnpj: '',
         cep: '',
         errorMessage: ''
      };
   },
 
   computed: {
     formTitle() {
       return this.editedIndex === -1 ? 'Nova Empresa' : 'Editar Empresa';
     },
   },
 
   watch: {
     dialog(val) {
       val || this.close();
     },
   },
 
   mounted() {
     this.fetchEmpresas();
   },

   methods: {
      async fetchEmpresas() {
         try {
            const data: EmpresaPagFornResponseDTO = await EmpresaService
               .fetchEmpresas(this.page, this.size, this.name, this.cnpj, this.cep);
            this.empresas = data.dados;
            this.totalPaginas = data.totalPaginas;
            this.totalEmpresas = data.dados.length;
         } catch (error) {
            this.errorMessage = error.message
            this.snackbarError = true;
            console.error('Erro ao buscar empresas:', error);
         }
      },
   
      editItem(item) {
         this.editedIndex = this.empresas.indexOf(item);
         this.editedItem = Object.assign({}, item);
         this.dialog = true;
      },
 
      async deleteItem(item) {
         try {
            const empresaDeletada: EmpresaFornResponseDTO = await EmpresaService.deleteEmpresa(item);
            if (empresaDeletada === null) throw new Error('Não foi possível concluir a operação');
            await this.fetchEmpresas();
            this.snackbarSuccess = true;
         } catch (error) {
            this.errorMessage = error.message
            this.snackbarErro = true;
            console.error('Erro na requisição de deletar:', error);
         }
      },
 
      async save(): Promise<void> {
         if (this.editedIndex > -1) {
            try {
               this.validaCamposForm(this.editedItem);
               const empresaAtualizada: EmpresaFornResponseDTO = await EmpresaService
                  .atualizarEmpresa(this.editedItem);
               await this.fetchEmpresas();
               this.snackbarSuccess = true;
               this.close();
            } catch (error) {
               this.errorMessage = error.message
               this.snackbarError = true;
               console.error('Erro na requisição de atualização:', error);
            }
         }

         try {
            this.validaCamposForm(this.editedItem);
            const empresaRegistrada: EmpresaFornResponseDTO = await EmpresaService
               .registrarEmpresa(this.editedItem);
            await this.fetchEmpresas();
            this.snackbarSuccess = true;
            this.close();
            return
         } catch(error) {
            this.errorMessage = error.message
            this.snackbarError = true;
            console.error(error);
            return
         }
      },

      validaCamposForm(item: EmpresaRequestDTO): void {
         if (!item.cnpj) throw new Error('CNPJ é obrigatório');
         if (!item.nome_fantasia) throw new Error('Nome é obrigatório');
         if (!item.cep) throw new Error('CEP é obrigatório');
      },
 
      close() {
         this.dialog = false;
         this.$nextTick(() => {
            this.editedItem = Object.assign({}, this.defaultItem);
            this.editedIndex = -1;
         });
      },

      verDetalhes(item: EmpresaRequestDTO): void {
         window.open(`/empresas/${item.id}`, '_self')
      },
   },
};
</script>
