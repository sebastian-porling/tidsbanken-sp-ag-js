<template>
    <v-dialog v-model="dialog" width="350px">
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
            <p>{{ errorMessage }}</p>
            <v-date-picker v-model="dates" range :min="today"></v-date-picker>
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
            ineligible_period: {
              period_start: null,
              period_end: null,
            },
            today: new Date().toJSON().slice(0,10),
            errorMessage: "",
        }
    },
    methods: {
      validateData() {
        this.setDates(this.dates);

        if(this.period_start != null && this.period_end != null){
          if(this.period_start < this.period_end) {
              this.dialog = false;
              // Connect to api
              alert("New Ineligible Period has been added between " + this.period_start + " and " + this.period_end);
          } else {
            this.switchDates();
            this.dialog = false;
            // Connect to api
              alert("New Ineligible Period has been added between " + this.period_start + " and " + this.period_end);
          }
        } else {
          this.errorMessage = "You need to enter a start and an end date..";
        }
      },
      setDates(dates) {
        this.period_end = dates[1];
        this.period_start = dates[0];
      },
      switchDates(){
        let temp = this.period_start;
        this.period_start = this.period_end;
        this.period_end = temp;
      }
    },    
}
</script>

<style>

</style>