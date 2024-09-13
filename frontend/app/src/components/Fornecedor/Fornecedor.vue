<script>
import FornecedorTable from './FornecedorTable.vue';

   export default {
      data: () => ({
         drawer: false,
         group: null
   }),

   watch: {
      group () {
         this.drawer = false
      },
   },
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
            <FornecedorTable
               :empresas="empresas"
               :page="page"
               :size="size"
            />
         </v-container>
      </v-main>
     </v-layout>
   </v-card>
</template>
