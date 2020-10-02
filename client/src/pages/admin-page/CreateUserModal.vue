<template>
  <v-dialog v-model="dialog" persistent width="600px">
    <template v-slot:activator="{ on, attrs }">
      <v-btn v-bind="attrs" fab small v-on="on" color="success" class="add-btn">+</v-btn>
    </template>
    <v-card>
      <v-card-title>
        <span class="headline">Create new user</span>
      </v-card-title>
      <v-card-text>
        <v-form v-model="valid">
          <v-row>
            <v-col cols="10">
              <v-text-field label="Name*" v-model="name" required :rules="nameRules"></v-text-field>
            </v-col>
            <v-col cols="10">
              <v-text-field label="Email*" v-model="email" required :rules="emailRules"></v-text-field>
            </v-col>
            <v-col cols="10">
              <v-text-field
                label="Password*"
                v-model="password"
                required
                :rules="passwordRules"
                type="password"
              ></v-text-field>
            </v-col>
            <br />
            <v-col cols="10">
              <v-text-field
                label="Number of vacation days*"
                v-model="vacationDays"
                required
                :rules="daysRules"
              ></v-text-field>
            </v-col>
            <v-col cols="10">
              <v-switch v-model="admin" label="Admin"></v-switch>
            </v-col>
          </v-row>
        </v-form>
        <small>*indicates required field</small>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="red darken-1" text @click="dialog = false">Cancel</v-btn>
        <v-btn color="green darken-1" text @click="submit" :disabled="!valid">Create</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
export default {
  name: "CreateUserModal",
  data() {
    return {
      dialog: false,
      valid: true,
      name: "",
      nameRules: [
        (v) => !!v || "Name is required",
        (v) =>
          (v && v.length <= 100) || "Name must be less than 100 characters",
      ],
      email: "",
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
      vacationDays: null,
      daysRules: [
        (v) => !!v || "Vacation days is required",
        (v) => (v && parseInt(v) > 0) || "Has to be more than 1",
        (v) =>
          (v && parseInt(v) <= 30) || "Maxmum amount of days per year is 30",
      ],
      admin: false,
    };
  },
  methods: {
    submit() {
      if (this.valid) {
        this.$store
          .dispatch("createUser", {
            full_name: this.name,
            email: this.email,
            password: this.password,
            vacation_days: this.vacationDays,
            is_admin: this.admin,
          })
          .then(() => {
            this.dialog = false;
          })
          .catch((error) => {
            alert(error.data.message);
          });
      }
    },
  },
};
</script>

<style>
.add-btn {
   position: absolute;
   right: 10px; 
   top: 10px;
}
</style>