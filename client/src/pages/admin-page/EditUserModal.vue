<template>
  <v-dialog v-model="active" width="600px">
    <v-card>
      <v-card-title>
        <span class="headline">Edit user</span>
      </v-card-title>
      <v-card-text>
        <v-form v-model="valid">
          <v-row>
            <v-col cols="10">
              <v-text-field v-model="user.full_name" label="Name*" required :rules="nameRules"></v-text-field>
            </v-col>
            <v-col cols="10">
              <v-text-field v-model="user.email" label="Email*" required :rules="emailRules"></v-text-field>
            </v-col>
            <v-col cols="10">
              <v-text-field v-model="password" label="Password*" required :rules="passwordRules"></v-text-field>
            </v-col>
            <br />
            <v-col cols="10">
              <v-text-field v-model="user.vacation_days" label="Number of vacation days*" required :rules="daysRules"></v-text-field>
            </v-col>
            <v-col cols="10">
                <v-switch
                v-model="user.is_admin"
                label="Admin"
                ></v-switch>
            </v-col>
          </v-row>
        </v-form>
        <small>*indicates required field</small>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="red darken-1" text @click="closeModal">Cancel</v-btn>
        <v-btn color="green darken-1" text @click="submit" :disabled="!valid">Save Changes</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
export default {
  name: "CreateUserModal",
  data() {
    return {
      valid: true,
      nameRules: [
        (v) => !!v || "Name is required",
        (v) =>
          (v && v.length <= 100) || "Name must be less than 100 characters",
      ],
      emailRules: [
        (v) => !!v || "E-mail is required",
        (v) => /.+@.+\..+/.test(v) || "E-mail must be valid",
      ],
      password: "",
      passwordRules: [
        (v) => !!v || "Password is required",
        (v) =>
          (v && v.length >= 6) || "Password must be more than 6 characters",
      ],
      daysRules: [
        (v) => !!v || "Vacation days is required",
        (v) => (v && v > 0 ) || "Can't be 0",
        (v) => (v && v > 30 ) || "Maxmum amount of days per year is 30"
      ],
    }
  },
  props: [
    'active',
    'user'
  ],
    methods: {
      closeModal() {
            this.$emit("closeModal");
        },
        openModal() {
            this.dialog = true;
        },
      submit() {
        if (this.valid) {
          // connect to api
          this.dialog = false;
          alert("User added");
        }
      }    
    }
};
</script>

<style>
</style>