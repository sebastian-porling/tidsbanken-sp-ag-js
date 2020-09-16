<template>
    <v-dialog v-model="dialog" width="600px">
        <template v-slot:activator="{ on, attrs }">
          <v-btn 
          v-bind="attrs"
          v-on="on">
            <v-icon>mdi-plus</v-icon> 
            Ineligible Period
          </v-btn>
      </template>
      <v-card>
        <v-card-title>
          <span class="headline">Add a Ineligible Period</span>
        </v-card-title>
        <v-card-text>
          <v-container>
            <v-row>
              <v-col cols="12" sm="6" md="6">
                <v-text-field type="date" label="Start*" required></v-text-field>
              </v-col>
               <v-col cols="12" sm="6" md="6">
                <v-text-field type="date" label="End*" required></v-text-field>
              </v-col>
            </v-row>
          </v-container>
          <small>*indicates required field</small>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="red darken-1" text @click="dialog = false">Cancel</v-btn>
          <v-btn color="green darken-1" text @click="dialog = false">Submit</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
</template>

<script>
export default {
    name: 'CreateIPModal',
    data() {
        return {
            dialog: false,
        }
    },
}
</script>

<style>

</style>