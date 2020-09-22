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
              alert("Inegible period has been removed from " + this.period_start + " to " + this.period_end);
          } else {
            this.switchDates();
            this.dialog = false;
            // Connect to api
              alert("Inegible period has been removed from " + this.period_start + " to " + this.period_end);
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