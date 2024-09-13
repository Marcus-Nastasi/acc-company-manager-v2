<script lang="ts">
import { EmpresaFornResponseDTO, EmpresaPagFornResponseDTO } from '@/interfaces/Empresa/EmpresaFornResponseDTO';

export default {
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
         nascimento: Date,
         nome: '',
         email: '',
         cep: '',
         e_pf: false
       },
       defaultItem: {
         id: '',
         cnpj_cpf: '',
         rg: '',
         nascimento: Date,
         nome: '',
         email: '',
         cep: '',
         e_pf: false
       },
       page: 1,
       size: 10,
       totalPaginas: 0,
       totalEmpresas: 0,
     };
   },
 
   computed: {
     formTitle() {
       return this.editedIndex === -1 ? 'Novo Fornecedor' : 'Editar Fornecedor';
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
         const response = await fetch(
           `http://localhost:8080/api/fornecedor?page=${this.page - 1}&size=${this.size}`,
           { method: 'GET' }
         );
         const data = await response.json();
         this.empresas = data.dados;
         this.totalPaginas = data.totalPaginas;
         this.totalEmpresas = data.dados.length;
       } catch (error) {
         console.error('Erro ao buscar fornecedores:', error);
       }
     },
 
     editItem(item) {
       this.editedIndex = this.empresas.indexOf(item);
       this.editedItem = Object.assign({}, item);
       this.dialog = true;
     },
 
     async deleteItem(item) {
      if (!item) return;
      if (!confirm('Deseja apagar realmente?')) return;
      try {
         const url: string = `http://localhost:8080/api/empresa/deletar/${item.id}`;
          const response = await fetch(url, {
            method: 'DELETE',
            headers: {
              'Content-Type': 'application/json',
            },
          });
          if (response.ok) {
            this.fetchEmpresas();
            // Object.assign(this.empresas[this.editedIndex], this.editedItem);
          } else {
            console.error('Erro ao atualizar a empresa');
          }
        } catch (error) {
          console.error('Erro na requisição de atualização:', error);
        }
     },
 
     async save() {
       if (this.editedIndex > -1) {
         Object.assign(this.empresas[this.editedIndex], this.editedItem);
         try {
            const response = await fetch(`http://localhost:8080/api/empresa/atualizar/${this.editedItem.id}`, {
               method: 'PATCH',
               headers: {
                  'Content-Type': 'application/json',
               },
               body: JSON.stringify(this.editedItem),
            });
            if (response.ok) {
               // Object.assign(this.empresas[this.editedIndex], this.editedItem);
            } else {
               console.error('Erro ao atualizar a empresa');
            }
         } catch (error) {
            console.error('Erro na requisição de atualização:', error);
         }
         } else {
            this.empresas.push(this.editedItem);
            const response = await fetch('http://localhost:8080/api/fornecedor/registrar', {
               method: 'POST',
               headers: {
                  'Content-Type': 'application/json',
               },
               body: JSON.stringify({
                  cnpj_cpf: this.editedItem.cnpj_cpf,
                  rg: this.editedItem.rg,
                  nascimento: this.editedItem.nascimento,
                  nome: this.editedItem.nome,
                  email: this.editedItem.email,
                  cep: this.editedItem.cep,
                  e_pf: this.editedItem.e_pf
               }),
            });
            if (response.status !== 201) {
               alert("erro!");
            }
            this.fetchEmpresas();
         }
      this.close();
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

<template>
   <div>
     <v-data-table
       :headers="headers"
       :items="empresas"
       :page.sync="page"
       :items-per-page="size"
       :server-items-length="totalEmpresas"
     >
       <template v-slot:top>
         <v-toolbar flat>
           <v-toolbar-title>Fornecedor</v-toolbar-title>
           <v-divider class="mx-4" inset vertical></v-divider>
           <v-spacer></v-spacer>
           <v-dialog v-model="dialog" max-width="500px">
             <template v-slot:activator="{ props }">
               <v-btn class="mb-2" color="primary" dark v-bind="props">
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
                     <v-col cols="12" class=" flex flex-col justify-center items-center">
                        <label for="e_pf">Pessoa física</label>
                        <input style="margin: 5px;" type="checkbox" name="e_pf" id="e_pf">
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
       @input="fetchEmpresas"
       @click="fetchEmpresas"
     ></v-pagination>
   </div>
</template>
