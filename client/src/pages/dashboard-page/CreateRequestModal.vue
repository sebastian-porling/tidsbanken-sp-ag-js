<template>
    <v-dialog v-model="dialog" width="600px">
      <template v-slot:activator="{ on, attrs }">
          <v-btn 
          v-bind="attrs"
          text
          :color="'green darken-2'"
          v-on="on">
            <v-icon>mdi-plus</v-icon> 
            Request Vacation
          </v-btn>
      </template>
      <v-card>
        <v-card-title>
          <span class="headline">Create Vacation Request</span>
        </v-card-title>
        <v-card-text>
          <v-form v-model="valid">
            {{errorMessage}}
            <v-row>
              <v-col cols="12">
                <v-text-field label="Title*" v-model="title" required :rules="titleRules"></v-text-field>
              </v-col>
              <v-col cols="12" sm="6" md="6">
                <v-text-field type="date" v-model="start" label="Start*" required :min="today" :rules="startRules"></v-text-field>
              </v-col>
               <v-col cols="12" sm="6" md="6">
                <v-text-field type="date" v-model="end" label="End*" required :min="today" :rules="endRules"></v-text-field>
              </v-col>
              <v-col cols="12">
                <v-textarea
                solo
                name="input-7-4"
                label="Comment"
                v-model="comment"
                ></v-textarea>
              </v-col>
            </v-row>
          </v-form>
          <small>*indicates required field</small>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="red darken-1" text @click="dialog = false">Cancel</v-btn>
          <v-btn color="green darken-1" text @click="submit" :disabled="!valid">Submit</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
</template>

<script>
export default {
    name: 'CreateRequestModal',
    data() {
        return {
            dialog: false,
            today: new Date().toJSON().slice(0,10),
            valid: true,
            title: "",
            comment: "",
            errorMessage: "",
            titleRules: [
              v => !!v || "Title is required",
              v => (v && v.length >= 10) || 'Title must be 10 characters or more',
            ],
            start: "",
            startRules: [
              v => !!v || "Start date is required",
            ],
            end: "",
            endRules: [
              v => !!v || "Start date is required",
              v => (v && v > this.start) || "End date can not be before start date"
            ]
        }
    },
    methods: {
      submit() {
        if (this.valid) {
          this.$store.dispatch('createVacationRequest', {title: this.title, start: this.start, end: this.end})
          .then(request => {
            if (this.comment !== "") {
              this.$store.dispatch('createComment', { requestId: request.id, message: this.comment })
              this.$emit("openRequestModal", request);
              this.reset();
            }
          })
          .catch(() => {
          })
        } else {
          this.errorMessage = "You have to fill out all the required fields";
        }

      },
      reset() {
        this.valid = true;
        this.title = "";
        this.errorMessage = "";
        this.start = "";
        this.end = "";
        this.dialog = false;
      }
    }
}
</script>

<style>

</style>