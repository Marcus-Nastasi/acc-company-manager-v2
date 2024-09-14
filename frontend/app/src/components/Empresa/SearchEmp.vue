<!-- <template>
   <v-expansion-panels variant="inset" color="#d3d3d3">
     <v-expansion-panel>
       <v-expansion-panel-title>
         <template v-slot:default="{ expanded }">
           <v-row no-gutters>
             <v-col class="d-flex justify-start" cols="4">
               Filtros
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
                     Filtre por nome ou cnpj/cpf
                 </span>
                 
               </v-fade-transition>
             </v-col>
           </v-row>
         </template>
       </v-expansion-panel-title>
       <v-expansion-panel-text>
         
         <v-text-field
            v-bind="name"
            v-on:update:modelValue="update"
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
            v-bind="cnpj"
            v-on:update:modelValue="update"
            :loading="loading"
            append-inner-icon="mdi-magnify"
            label="Search by cnpj..."
            variant="solo-filled"
            hide-details
            single-line
            @click:append-inner="onClick"
         ></v-text-field>
         <v-text-field
            v-bind="cep"
            v-on:update:modelValue="update"
            :loading="loading"
            append-inner-icon="mdi-magnify"
            label="Search by cep..."
            variant="solo-filled"
            hide-details
            single-line
            class=" my-5"
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
      fetchEmpresas: {
         type: Function,
         required:  true 
      },
      name: {
         type: String,
         required: true
      },
      cnpj: {
         type: String,
         required: true
      },
      cep: {
         type: String,
         required: true
      }
   },

   data: () => ({
      loaded: false,
      loading: false,
      name: this.name,
      cnpj: this.cnpj,
      cep: this.cep
   }),

   methods: {
      async onClick(): Promise<void> {
         this.loading = true;
         setTimeout(async () => {
            await this.fetchEmpresas(this.name, this.cnpj, this.cep);
            this.loading = false;
         }, 1000);
      },

      async clean(): Promise<void> {
         this.loading = true;
         this.name = '';
         this.cnpj = '';
         this.cep = '';
         setTimeout(async () => {
            await this.fetchEmpresas();
            this.loading = false;
         }, 1000);
      },

      update() {
         this.$emit('update:modelValue', this.nome);
      },

      watch: {
         modelValue(name) {
            this.name = name;
         },
      },
   }
}
</script> -->

<template>
   <v-expansion-panels variant="inset" color="#d3d3d3">
     <v-expansion-panel>
       <v-expansion-panel-title>
         <template v-slot:default="{ expanded }">
           <v-row no-gutters>
             <v-col class="d-flex justify-start" cols="4">
               Filtros
             </v-col>
             <v-col class="text-grey" cols="8">
               <v-fade-transition leave-absolute>
                 <span v-if="expanded" key="0">
                   Filtre por nome ou cnpj/cpf
                 </span>
               </v-fade-transition>
             </v-col>
           </v-row>
         </template>
       </v-expansion-panel-title>
       <v-expansion-panel-text>
         <!-- TextField para Name -->
         <v-text-field
           v-model="localName"
           :loading="loading"
           append-inner-icon="mdi-magnify"
           label="Search by name..."
           variant="solo-filled"
           hide-details
           single-line
           class="my-5"
         ></v-text-field>
 
         <!-- TextField para CNPJ -->
         <v-text-field
           v-model="localCnpj"
           :loading="loading"
           append-inner-icon="mdi-magnify"
           label="Search by cnpj..."
           variant="solo-filled"
           hide-details
           single-line
         ></v-text-field>
 
         <!-- TextField para CEP -->
         <v-text-field
           v-model="localCep"
           :loading="loading"
           append-inner-icon="mdi-magnify"
           label="Search by cep..."
           variant="solo-filled"
           hide-details
           single-line
           class="my-5"
         ></v-text-field>
 
         <div class="w-full mt-5 flex flex-row-reverse items-center">
           <v-btn class="ml-2" color="" size="small" variant="text" @click="clean">
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
     fetchEmpresas: {
       type: Function,
       required: true,
     },
     name: {
       type: String,
       required: true,
     },
     cnpj: {
       type: String,
       required: true,
     },
     cep: {
       type: String,
       required: true,
     },
   },
 
   data() {
     return {
       loading: false,
       localName: this.name, // Localmente armazenamos o valor inicial da prop
       localCnpj: this.cnpj,
       localCep: this.cep,
     };
   },
 
   methods: {
     async onClick(): Promise<void> {
       this.loading = true;
       setTimeout(async () => {
         await this.fetchEmpresas(this.localName, this.localCnpj, this.localCep);
         this.loading = false;
       }, 1000);
     },
 
      async clean(): Promise<void> {
         this.localName = '';
         this.localCnpj = '';
         this.localCep = '';
         this.$emit('update:name', this.localName);
         this.$emit('update:cnpj', this.localCnpj);
         this.$emit('update:cep', this.localCep);
         await this.fetchEmpresas();
      },
   },
 
   watch: {
      name(newVal) {
         this.localName = newVal;
      },
      cnpj(newVal) {
         this.localCnpj = newVal;
      },
      cep(newVal) {
         this.localCep = newVal;
      },
 
      localName(newVal) {
         this.$emit('update:name', newVal);
      },
      localCnpj(newVal) {
         this.$emit('update:cnpj', newVal);
      },
      localCep(newVal) {
         this.$emit('update:cep', newVal);
      },
   },
 };
</script>
