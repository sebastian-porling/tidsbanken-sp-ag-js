<template>
    <v-dialog v-model="active" persistent width="350px">
      <v-card>
        <v-card-title>
          <span class="headline">Edit Ineligible Period</span>
        </v-card-title>
        <v-card-text>
            <p>{{ errorMessage }}</p>
            <v-date-picker v-model="dates" range></v-date-picker>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="red darken" text @click="deleteIp">Delete</v-btn>
          <v-btn color="red darken-1" text @click="closeModal">Cancel</v-btn>
          <v-progress-circular v-if="isLoading" indeterminate color="green"></v-progress-circular>
          <v-btn v-if="!isLoading" color="green darken-1" text @click="validateData">Submit</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
</template>

<script>
export default {
    name: 'EditIpModal',
    props: ['active', 'ineligible'],
    data() {
        return {
            isLoading: false,
            dates: [],
            today: '',
            errorMessage: ''
        }
    },
    mounted() {
        if (this.ineligible !== null) {
          this.dates = [this.ineligible.start, this.ineligible.end]
          this.today = this.ineligible.start;
        }
    },
    watch: {
      ineligible() {
        this.dates = [this.ineligible.start, this.ineligible.end]
      }
    },
    methods: {
        closeModal() {
          this.dates = [];
          this.$emit('closeModal');
        },
        validateData() {
          this.isLoading = true
        if(this.dates[0] != null && this.dates[1] != null){
          if(this.dates[0] > this.dates[1]) this.switchDates();
          this.$store.dispatch('updateIneligiblePeriod', {id: this.ineligible.id, start: this.dates[0], end: this.dates[1]})
            .then(() => {
              this.isLoading = false
              this.$emit('closeModal');
            })
            .catch(error => {
              this.isLoading = false;
              this.errorMessage = error.data.message;
            });
        } else {
          this.errorMessage = "You need to enter a start and an end date..";
        }
      },
      switchDates(){
        this.dates = [this.dates[1], this.dates[0]]
      },
      deleteIp() {
        this.$store.dispatch('deleteIneligiblePeriod', this.ineligible.id)
        .then(() => {
          this.closeModal();
        })
        .catch(() => {
        })
      }
    },
}
</script>

<style>

</style>