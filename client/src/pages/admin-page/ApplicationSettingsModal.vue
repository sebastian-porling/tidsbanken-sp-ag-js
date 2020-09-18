<template>
    <v-dialog v-model="dialog" width="600px">
      <template v-slot:activator="{ on, attrs }">
          <v-btn 
          v-bind="attrs"
          v-on="on">
          <v-icon dark>mdi-wrench</v-icon>
          </v-btn>
      </template>
      <v-card>
        <v-card-title>
          <span class="headline">Application Settings </span>
        </v-card-title>
        <v-card-text>
              <v-container>
    <v-row>
      <v-col cols="12" sm="6" md="3">
        <v-text-field label="Setting" solo v-model="setting"></v-text-field>
      </v-col>
      <v-col cols="12" sm="6" md="3">
        <v-text-field label="Value" solo v-model="value"></v-text-field>
      </v-col>
      <!-- add this later
      <div class="addbutton">
            <v-btn color="green" large v-on:click="add">Add</v-btn>
      </div>
      -->
    </v-row>
  </v-container>
  <h2 class="set">Setting</h2>
   <h2 class="val">Value</h2>
  
               <v-divider
        class="mx-4"
        :inset="inset"
        horizontal
      ></v-divider>
         <v-container>
    <v-row>
         
      <v-col cols="12" sm="6" md="3">
           <p>{{ setting}}</p>
      
      </v-col>
      <v-col cols="12" sm="6" md="3">
        <p>{{value }}</p>
      </v-col>
      <!-- add this later
      <div class="editbutton">
            <v-btn color="info" medium v-on:click="edit">Edit</v-btn>
      </div>
      --->
    </v-row>
  </v-container>
        
           

           
           <br>
           <v-btn color="red darken-1" text @click="dialog = false">Cancel</v-btn>
             
        </v-card-text>
        <v-card-actions>
      
        </v-card-actions>
      </v-card>
    </v-dialog>
</template>

<script>
export default {
    name: 'ApplicationSettingsModal',
    data() {
        return {
            setting: null,
            value: null,
            dialog: false
        }
    },
}
</script>

<style>
.addbutton{
    padding-left: 10%;
    padding-top: 2.8%;
}
.editbutton{
    padding-left: 10%;
    padding-top: 1%;
}
.set{
    padding-left:3%;
    padding-bottom: 1%;
    padding-right: 10%;
}
.set, .val{
    display:inline;
}
</style>