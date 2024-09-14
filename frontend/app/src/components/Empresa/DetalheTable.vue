<script lang="ts">
import { EmpresaFornResponseDTO, EmpresaPagFornResponseDTO } from '@/interfaces/Empresa/EmpresaFornResponseDTO';
import { CepService } from '@/services/cep/CepService';
import { DateUtil } from '@/util/DateUtil';
import { emit } from 'process';

export default {
   props: {
      empresa: {
         type: {} as EmpresaFornResponseDTO,
         required: true
      },
      fetchEmpresas: {
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
      };
   },

   watch: {
      dialog(val) {
         val || this.close();
      },
   },
 
   mounted() {
      // this.fetchEmpresa(window.location.href.split('/')[4]);
   },
 
   methods: {
      async buscaFornecedor(id: string) {
         const url: string = `http://localhost:8080/api/fornecedor/${id}`;
         const response: Response = await fetch(url, {
            method: 'GET',
            headers: { 'Content-Type': 'application/json' }
         });
         if (response.status != 200) throw new Error("");
         const data = await response.json();
         return data;
      },
      
      async save() {
         try {
            const fornecedor = await this.buscaFornecedor(this.editedItem.id_fornecedor);
            if (fornecedor.e_pf && await CepService.isPr(this.empresa.cep)) {
               if (DateUtil.isUnderAge(new Date(fornecedor.nascimento))) {
                  alert('Não é permitido cadastrar um fornecedor menor de idade no Paraná');
                  return;
               }
            }
            const url: string = `http://localhost:8080/api/empresa/associar/${this.empresa.id}/${fornecedor.id}`
            const response: Response = await fetch(url, {
               method: 'PATCH',
               headers: { 'Content-Type': 'application/json' }
            });
            if (response.status != 200) throw new Error("Stat dif 200");
            const data = await response.json();
            alert('Sucesso!');
            this.fetchEmpresas();
         } catch(e) {
            alert("Erro");
         }
         this.close();
      },

      close() {
         this.dialog = false;
         this.$nextTick(() => {
            this.editedItem = Object.assign({}, this.defaultItem);
            this.editedIndex = -1;
         });
      }
   },
};
</script>

<template>
   <div>
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
               <v-btn class="mb-2" color="primary" dark v-bind="props">
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
                 <v-btn color="blue-darken-1" variant="text" @click="close">
                   Cancelar
                 </v-btn>
                 <v-btn color="blue-darken-1" variant="text" @click="save">
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
