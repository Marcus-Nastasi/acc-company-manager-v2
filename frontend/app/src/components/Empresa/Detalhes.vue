<script lang="ts">
import { EmpresaFornResponseDTO, EmpresaPagFornResponseDTO } from '@/interfaces/Empresa/EmpresaFornResponseDTO';
import DetalheTable from './DetalheTable.vue';

export default {
   components: {
      DetalheTable
   },

   data: () => ({
      drawer: false,
      group: null,
      empresa: {} as EmpresaFornResponseDTO
   }),

   watch: {
      group () {
         this.drawer = false
      },
   },

   mounted() {
      this.fetchEmpresa(window.location.href.split('/')[4]);
   },

   methods: {
      async fetchEmpresa(id: string): Promise<void> {
         try {
            const response = await fetch(`http://localhost:8080/api/empresa/${id}`, { 
               method: 'GET' 
            });
            const data: EmpresaFornResponseDTO = await response.json();
            this.empresa = data;
         } catch (error) {
            alert('Erro ao buscar empresas: ' + error.message());
         }
      },
   }
}
</script>

<template>
   <v-card class=" min-h-screen max-h-fit">
     <v-layout>
      <v-app-bar
         color=""
         prominent
      >
         <v-list-item prepend-icon="mdi-menu" @click.stop="drawer = !drawer"></v-list-item>
      </v-app-bar>
 
      <v-navigation-drawer
         v-model="drawer"
         :location="$vuetify.display.mobile ? 'bottom' : undefined"
         
         class=" h-screen"
      >
         <v-list
            class=" min-h-screen max-h-fit"
            :items="items"
         >
            <v-list>
               <v-list-item prepend-icon="mdi-menu" href="/" title="Home"></v-list-item>
               <v-list-item prepend-icon="mdi-store-settings" href="/empresas" title="Empresas"></v-list-item>
               <v-list-item prepend-icon="mdi-account"  href="/fornecedores" title="Fornecedores"></v-list-item>
            </v-list>
         </v-list>
      </v-navigation-drawer>
 
      <v-main class=" min-h-screen max-h-fit mt-20">
         <v-container>
            <v-row>
               <v-col cols="12" md="4">
               </v-col>
               <v-col cols="12" md="4" style="margin-bottom: 50px;">
                  <v-card color="success">
                     <template v-slot:title>
                        {{ empresa.nome_fantasia }}
                     </template>

                     <template v-slot:subtitle>
                        CEP: {{  empresa.cep  }}
                     </template>

                     <template v-slot:text>
                        CNPJ: {{ empresa.cnpj }}
                     </template>
                  </v-card>
               </v-col>
               <v-col cols="12" md="4">
               </v-col>
            </v-row>
            <DetalheTable
               :empresa="empresa"
            />
         </v-container>
      </v-main>
     </v-layout>
   </v-card>
</template>
