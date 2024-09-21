<template>
   <v-container style="background-color: #212121; border-radius: 10px;">
     <v-row no-gutters style="display: flex; justify-content: space-evenly;">
       <v-col
         cols="12"
         sm="4"
       >
         <v-sheet class="ma-2 pa-2">
            <v-card
               variant="elevated"
               color="primary"
               class="mx-auto"
               max-width="344"
               style="padding: 10px; text-wrap: wrap;"
               link
            > 
               <v-list-item @click="goToEmpresa" prepend-icon="mdi-navigation-variant-outline" title="Empresa"></v-list-item>
            </v-card>
         </v-sheet>
       </v-col>
       <v-col
         cols="12"
         sm="4"
       >
         <v-sheet class="ma-2 pa-2">
            <v-card
               variant="elevated"
               color="success"
               class="mx-auto"
               max-width="344"
               style="padding: 10px; text-wrap: wrap;"
               link
            > 
               <v-list-item @click="goToFornecedor" prepend-icon="mdi-navigation-variant-outline" title="Fornecedor"></v-list-item>
            </v-card>
         </v-sheet>
       </v-col>
     </v-row>
   </v-container>
</template>

<script>
export default {
   methods: {
      goToEmpresa() {
        window.open('/empresas', '_self');
      },
      goToFornecedor() {
        window.open('/fornecedores', '_self');
      }
   }
}
</script>
