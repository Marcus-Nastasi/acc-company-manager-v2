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
         <v-btn
            variant="tonal"
            size="large"
            color="primary" 
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
 
      <v-main class="min-h-screen max-h-fit mt-5">
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
