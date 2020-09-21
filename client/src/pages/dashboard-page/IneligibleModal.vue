<template>
    <v-dialog v-model="dialog" width="350px">
        <template v-slot:activator="{ on, attrs }">
          <v-btn 
          v-bind="attrs"
          v-on="on">
            <v-icon>mdi-minus</v-icon> 
            Delete Ineligible Period
          </v-btn>
      </template>
      <v-card>
        <v-card-title>
          <span class="headline">Delete an Ineligible Period</span>
        </v-card-title>
         <v-card-text>
            <v-date-picker v-model="dates" range :min="today"></v-date-picker>
        </v-card-text>
        <v-card-actions>
          <v-btn text @click="dialog = false">Cancel</v-btn>
          <v-btn color="red darken-1" text @click="validateData">Remove</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
</template>

<script>
export default {
    name: 'IneligibleModal',
    data() {
        return {
            dialog: false,
            
    }    
},
}
</script>

<style>

</style>