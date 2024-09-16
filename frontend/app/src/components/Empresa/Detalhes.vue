<template>
   <v-card class=" min-h-screen max-h-fit">
     <v-layout>
      <v-app-bar
         color=""
         prominent
      >
         <v-btn
            variant="text"
            size="large" 
            @click.stop="drawer = !drawer" 
            style="padding: 0; box-sizing: border-box; width: fit-content; margin-left: 1rem; display: flex; justify-content: center;"
         >
            <v-list-item prepend-icon="mdi-menu" style="width: 60%;"></v-list-item>
         </v-btn>
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
               <v-list-item prepend-icon="mdi-home" href="/" title="Home"></v-list-item>
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
                  <v-card variant="tonal">
                     <template v-slot:title>
                        {{ empresa.nome_fantasia }}
                     </template>

                     <template v-slot:subtitle>
                        ID: {{ empresa.id }}
                     </template>

                     <template v-slot:text>
                        CNPJ: {{ empresa.cnpj }}<br/>
                        CEP: {{  empresa.cep  }}
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
      empresa: {} as EmpresaFornResponseDTO,
      fetchEmpresas: () => {}
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
               method: 'GET',
               headers: { 'Content-Type': 'application/json' }
            });
            const data: EmpresaFornResponseDTO = await response.json();
            this.empresa = data;
            this.parseToDateString();
         } catch (error) {
            alert('Erro ao buscar empresas: ' + error.message);
         }
      },

      parseToDateString(): void {
         this.empresa.fornecedores.forEach(element => {
            if (Array.isArray(element.nascimento)) {
               const dataFormatada = new Date(element.nascimento[0], element.nascimento[1] - 1, element.nascimento[2])
                  .toLocaleDateString('pt-BR');
               element.nascimento = dataFormatada;
            }
         });
      },
   }
}
</script>
