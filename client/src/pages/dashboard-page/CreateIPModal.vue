<template>
    <v-dialog v-model="dialog" width="350px">
        <template v-slot:activator="{ on, attrs }">
          <v-btn 
          v-bind="attrs"
          text
          :color="'blue darken-2'"
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
            <p>{{ errorMessage }}</p>
            <v-date-picker v-model="dates" range :min="today" :rules="dateRules"></v-date-picker>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="red darken-1" text @click="dialog = false">Cancel</v-btn>
          <v-btn color="green darken-1" text @click="validateData">Submit</v-btn>
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
            dates: [null, null],
            today: new Date().toJSON().slice(0,10),
            errorMessage: "",
            dateRules: [
              v => !!v || "Required!",
              v => v.length < 2 || "Two dates are required!"
            ]
        }
    },
    methods: {
      validateData() {
        if(this.dates[0] != null && this.dates[1] != null){
          if(this.dates[0] > this.dates[1]) this.switchDates();
          this.$store.dispatch('createIneligiblePeriod', {start: this.dates[0], end: this.dates[1]})
            .then(() => {
              this.dialog = false;
            })
            .catch(() => {
            });
        } else {
          this.errorMessage = "You need to enter a start and an end date..";
        }
      },
      switchDates(){
        this.dates = [this.dates[1], this.dates[0]]
      }
    },    
}
</script>

<style>

</style>