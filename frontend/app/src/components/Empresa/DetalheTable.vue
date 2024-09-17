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
     <v-data-table
       :headers="headers"
       :items="empresa.fornecedores"
     >
       <template v-slot:top>
         <v-toolbar flat>
           <v-toolbar-title>Fornecedores</v-toolbar-title>
           <v-divider class="mx-4" inset vertical></v-divider>
           <v-spacer></v-spacer>
           <v-dialog v-model="dialog" max-width="500px">
             <template v-slot:activator="{ props }">
               <v-btn class="mr-5" color="success" variant="tonal" dark v-bind="props">
                 Associar fornecedor
               </v-btn>
             </template>
             <v-card>
               <v-card-title>
                 <span class="text-h5">Associar fornecedor</span>
               </v-card-title>
 
               <v-card-text>
                 <v-container>
                   <v-row>
                     <v-col cols="12">
                       <v-text-field
                         v-model="editedItem.id_fornecedor"
                         label="Id do fornecedor"
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
     </v-data-table>
   </div>
</template>

<script lang="ts">
import { EmpresaFornResponseDTO, EmpresaPagFornResponseDTO } from '@/interfaces/Empresa/EmpresaFornResponseDTO';
import { CepService } from '@/services/cep/CepService';
import { DateUtil } from '@/util/DateUtil';

export default {
   props: {
      empresa: {
         type: {} as EmpresaFornResponseDTO,
         required: true
      },
      fetchEmpresa: {
         type: () => {},
         required: true
      }
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
         editedItem: {
            id_fornecedor: '',
         },
         snackbarSuccess: false,
         snackbarError: false,
         errorMessage: ''
      };
   },

   watch: {
      dialog(val) {
         val || this.close();
      },
   },
 
   methods: {
      async buscaFornecedor(id: string) {
         try {
            const url: string = `http://localhost:8080/api/fornecedor/${id}`;
            const response: Response = await fetch(url, {
               method: 'GET',
               headers: { 'Content-Type': 'application/json' }
            });
            if (response.status != 200) throw new Error("Não foi possível buscar os dados");
            const data = await response.json();
            this.parseToDateString();
            return data;
         } catch(error) {
            this.errorMessage = error.message
            this.snackbarErro = true;
            console.error(error.message)
         }
      },
      
      async save() {
         try {
            if (!this.editedItem.id_fornecedor || this.editedItem.id_fornecedor.length < 1) {
               throw new Error('Id do fornecedor é obrigatório');
            }
      
            const fornecedor = await this.buscaFornecedor(this.editedItem.id_fornecedor);

            if (fornecedor.e_pf && await CepService.isPr(this.empresa.cep) && DateUtil.isUnderAge(new Date(fornecedor.nascimento))) {
               throw new Error('Não é permitido cadastrar um fornecedor menor de idade no Paraná'); 
            }
            const url: string = `http://localhost:8080/api/empresa/associar/${this.empresa.id}/${fornecedor.id}`
            const response: Response = await fetch(url, {
               method: 'PATCH',
               headers: { 'Content-Type': 'application/json' }
            });
            if (response.status != 200) throw new Error("Status diferente de 200");
            const data = await response.json();
            this.snackbarSuccess = true;
            this.close();
            await this.fetchEmpresa(this.editedItem.id_empresa);
         } catch(error) {
            this.errorMessage = error.message;
            this.snackbarError = true;
            console.error(error.message);
         }
      },

      parseToDateString(): void {
         this.empresa.fornecedores.forEach(fornecedor => {
            if (fornecedor.nascimento && Array.isArray(fornecedor.nascimento)) {
               const dataFormatada = new Date(
                  fornecedor.nascimento[0],
                  fornecedor.nascimento[1] - 1,
                  fornecedor.nascimento[2]
               ).toLocaleDateString('pt-BR', {
                  day: '2-digit',
                  month: '2-digit',
                  year: 'numeric',
               });
               fornecedor.nascimento = dataFormatada;
            }
         });
      },
      
      close() {
         this.dialog = false;
         this.$nextTick(() => {
            this.editedItem = Object.assign({}, this.defaultItem);
            this.editedIndex = -1;
         });
      },
   },
};
</script>
