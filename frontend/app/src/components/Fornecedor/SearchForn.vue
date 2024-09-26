<template>
   <v-expansion-panels variant="inset" color="#d3d3d3">
     <v-expansion-panel>
       <v-expansion-panel-title>
         <template v-slot:default="{ expanded }">
           <v-row no-gutters>
             <v-col class="d-flex justify-start" cols="4">
               Filters
             </v-col>
             <v-col
               class="text-grey"
               cols="8"
             >
               <v-fade-transition leave-absolute>
                 <span
                   v-if="expanded"
                   key="0"
                 >
                     Filter by name or cnpj/cpf
                 </span>
                 
               </v-fade-transition>
             </v-col>
           </v-row>
         </template>
       </v-expansion-panel-title>
       <v-expansion-panel-text>
         <v-text-field
            v-model="name"
            :loading="loading"
            append-inner-icon="mdi-magnify"
            label="Search by name..."
            variant="solo-filled"
            hide-details
            single-line
            class=" my-5"
            @click:append-inner="onClick"
         ></v-text-field>
         <v-text-field
            v-model="cnpj_cpf"
            :loading="loading"
            append-inner-icon="mdi-magnify"
            label="Search by cpf/cnpj..."
            variant="solo-filled"
            hide-details
            single-line
            @click:append-inner="onClick"
         ></v-text-field>
         <div class=" w-full mt-5 flex flex-row-reverse items-center">
            <v-btn class=" ml-2" color="" size="small" variant="text" @click="clean">
               limpar
            </v-btn>
            <v-btn color="blue-darken-1" variant="tonal" @click="onClick">
               Filtrar
            </v-btn>
         </div>
       </v-expansion-panel-text>
     </v-expansion-panel>
   </v-expansion-panels>
</template>

<script lang="ts">
export default {
   props: {
      fetchFornecedores: {
         type: Function,
         required:  true 
      }
   },

   data: () => ({
      loaded: false,
      loading: false,
      name: '',
      cnpj_cpf: ''
   }),

   methods: {
      async onClick(): Promise<void> {
         this.loading = true;
         setTimeout(async () => {
            await this.fetchFornecedores(this.name, this.cnpj_cpf);
            this.loading = false;
         }, 1000);
      },

      async clean(): Promise<void> {
         this.loading = true;
         this.name = '';
         this.cnpj_cpf = '';
         setTimeout(async () => {
            await this.fetchFornecedores();
            this.loading = false;
         }, 1000);
      }
   }
}
</script>
