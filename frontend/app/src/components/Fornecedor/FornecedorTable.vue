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
      <SearchForn
         :fetchFornecedores="fetchFornecedores" 
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
            <v-toolbar-title>Fornecedores</v-toolbar-title>
            <v-divider class="mx-4" inset vertical></v-divider>
            <v-spacer></v-spacer>
            <v-dialog v-model="dialog" max-width="500px">
               <template v-slot:activator="{ props }">
                  <v-btn class="mr-4" color="success" variant="tonal" dark v-bind="props">
                     Novo fornecedor
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
                           v-model="editedItem.cnpj_cpf"
                           label="CNPJ_CPF"
                           required
                        ></v-text-field>
                     </v-col>
                     <v-col cols="12">
                        <v-text-field
                           v-model="editedItem.nome"
                           label="Nome"
                           required
                        ></v-text-field>
                     </v-col>
                     <v-col cols="12">
                        <v-text-field
                           v-model="editedItem.email"
                           label="Email"
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
                     <v-col cols="12">
                        <v-switch
                           v-model="editedItem.e_pf"
                           color="primary"
                           label="Pessoa física"
                           hide-details
                        ></v-switch>
                     </v-col>
                     <v-col cols="12">
                        <v-text-field
                           v-model="editedItem.rg"
                           label="RG"
                           required
                        ></v-text-field>
                     </v-col>
                     <v-col cols="12">
                        <v-text-field
                           v-model="editedItem.nascimento"
                           label="Nascimento"
                           required
                           type="date"
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
         <v-icon class="me-2" size="small" @click="editItem(item)">
            mdi-pencil
         </v-icon>
         <v-icon size="small" @click="deleteItem(item)">
            mdi-delete
         </v-icon>
         </template>
      </v-data-table>

      <v-pagination
         v-model="page"
         :length="totalPaginas"
         @input="fetchFornecedores"
      ></v-pagination>
   </div>
</template>

<script lang="ts">
import { FornecedorEmpResponseDTO } from '@/interfaces/Fornecedor/FornecedorDTO';
import SearchForn from './SearchForn.vue';
import Search from './SearchForn.vue';
import { FornecedoresService } from '@/services/fornecedores/FornecedoresService';

export default {
   components: {
      Search
   },

   data() {
      return {
         dialog: false,
         headers: [
            { text: "ID", value: "id" },
            { text: "CNPJ_CPF", value: "cnpj_cpf" },
            { text: "RG", value: "RG" },
            { text: "Nascimento", value: "nascimento" },
            { text: "Nome", value: "nome" },
            { text: "Email", value: "email" },
            { text: "CEP", value: "cep" },
            { text: "Ações", value: "actions", sortable: false },
         ],
         empresas: [],
         editedIndex: -1,
         editedItem: {
            id: '',
            cnpj_cpf: '',
            rg: '',
            nascimento: '',
            nome: '',
            email: '',
            cep: '',
            e_pf: false,
         },
         defaultItem: {
            id: '',
            cnpj_cpf: '',
            rg: '',
            nascimento: '',
            nome: '',
            email: '',
            cep: '',
            e_pf: false,
         },
         page: 1,
         size: 10,
         totalPaginas: 0,
         totalEmpresas: 0,
         snackbarSuccess: false,
         snackbarError: false,
         errorMessage: ''
      };
   },

   computed: {
      formTitle(): string {
         return this.editedIndex === -1 ? 'Novo Fornecedor' : 'Editar Fornecedor';
      },
  },

   watch: {
      dialog(val): void {
         val || this.close();
      },
  },

   async mounted(): Promise<void> {
      await this.fetchFornecedores();
   },

   methods: {
      async fetchFornecedores(name = '', cnpj_cpf = ''): Promise<void> {
         try {
            const data = await FornecedoresService.fetchFornecedores(this.page, this.size, name, cnpj_cpf);
            this.empresas = data.dados;
            this.parseToDateString();
            this.totalPaginas = data.totalPaginas;
            this.totalEmpresas = data.dados.length;
         } catch (error) {
            this.errorMessage = error.message
            this.snackbarError = true;
         }
      },

      async fetchRegister(item): Promise<void> {
         try {
            this.validaCamposForm(item);
            const data = await FornecedoresService.registraFornecedor(item);
            this.fetchFornecedores();
            this.close();
            this.snackbarSuccess = true;
            return;
         } catch (error) {
            this.errorMessage = error.message
            this.snackbarError = true;
            return;
         }
      },

      async fetchEdit(id: string): Promise<FornecedorEmpResponseDTO> {
         try {
            this.validaCamposForm(this.editedItem);
            const data = await FornecedoresService.atualizarFornecedor(id, this.editedItem);  
            this.fetchFornecedores();
            this.close();
            this.snackbarSuccess = true;
            return data;
         } catch (error) {
            this.errorMessage = error.message
            this.snackbarError = true;
            return;
         }
      },

      async deleteItem(item): Promise<void> {
         if (!item) throw new Error('Item não existe, checar logs');
         if (!confirm('Deseja apagar realmente?')) return;
         try {
            const data = await FornecedoresService.deletarFornecedor(item.id);
            this.fetchFornecedores();
            this.snackbarSuccess = true;
            return
         } catch (error) {
            this.errorMessage = error.message
            this.snackbarError = true;
            console.error(error);
         }
      },

      async save(): Promise<void> {
         if (this.editedItem.nascimento) {
            this.editedItem.nascimento = new Date(this.editedItem.nascimento);
         }
         this.editedItem.e_pf = Boolean(this.editedItem.e_pf);
         if (this.editedIndex > -1) {
            this.fetchEdit(this.editedItem.id);
            return
         }
         this.fetchRegister(this.editedItem);
         return
      },

      close(): void {
         this.dialog = false;
         this.$nextTick(() => {
            this.editedItem = Object.assign({}, this.defaultItem);
            this.editedIndex = -1;
         });
      },

      editItem(item): void {
         this.editedIndex = this.empresas.indexOf(item);
         this.editedItem = Object.assign({}, item);
         this.dialog = true;
      },

      validaCamposForm(item): void {
         if (!item.cnpj_cpf) throw new Error('CNPJ ou CPF é obrigatório');
         if (!item.nome) throw new Error('Nome é obrigatório');
         if (!item.email) throw new Error('Email é obrigatório');
         if (!item.cep) throw new Error('CEP é obrigatório');

         if (item.e_pf) if (!item.rg || !item.nascimento) 
            throw new Error('RG e data de nascimento são obrigatórios para pessoas físicas');
      },

      parseToDateString(): void {
         this.empresas.forEach(element => {
            if (Array.isArray(element.nascimento)) {
               const dataFormatada = new Date(
                  element.nascimento[0], 
                  element.nascimento[1] - 1, 
                  element.nascimento[2]
               ).toLocaleDateString('pt-BR');
               element.nascimento = dataFormatada;
            }
         });
      },
   },
};
</script>
