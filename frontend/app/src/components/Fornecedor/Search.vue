<template>
   <v-card
     class="mx-auto"
     color="surface-light"
     max-width="400"
   >
     <v-card-text>
       <v-text-field
         v-model="name"
         :loading="loading"
         append-inner-icon="mdi-magnify"
         density="compact"
         label="Search by name..."
         variant="solo"
         hide-details
         single-line
         @click:append-inner="onClick"
       ></v-text-field>
     </v-card-text>
     <v-card-text>
       <v-text-field
         v-model="cnpj_cpf"
         :loading="loading"
         append-inner-icon="mdi-magnify"
         density="compact"
         label="Search by cpf/cnpj..."
         variant="solo"
         hide-details
         single-line
         @click:append-inner="onClick"
       ></v-text-field>
     </v-card-text>
   </v-card>
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
   },
}
</script>
