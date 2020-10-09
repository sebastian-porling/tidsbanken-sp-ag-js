<template>
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
            <br />
            <v-col cols="10">
              <v-text-field
                v-model="user.vacation_days"
                label="Number of vacation days*"
                required
                :rules="daysRules"
              ></v-text-field>
            </v-col>
            <v-col cols="10">
              <v-switch v-model="user.is_admin" label="Admin"></v-switch>
            </v-col>
            <v-col cols="10">
              <v-switch v-model="resetTwoFactor" label="Reset Two Factor Authentication"></v-switch>
            </v-col>
          </v-row>
        </v-form>
        <small>*indicates required field</small>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="grey darken-1" text @click="changeMode">Change password</v-btn>
        <v-btn color="red darken-1" text @click="closeModal">Cancel</v-btn>
        <v-progress-circular v-if="isLoading" indeterminate color="green"></v-progress-circular>
        <v-btn v-if="!isLoading" color="green darken-1" text @click="submit" :disabled="!valid">Save Changes</v-btn>
      
      </v-card-actions>
    </v-card>
</template>

<script>
export default {
  name: "EditUserModal",
  data() {
    return {
      isLoading: false,
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
      daysRules: [
        (v) => !!v || "Vacation days is required",
        (v) => (v && parseInt(v) > 0) || "Has to be more than 1",
        (v) =>
          (v && parseInt(v) <= 30) || "Maxmum amount of days per year is 30",
      ],
      changePassword: false,
      resetTwoFactor: false,
    };
  },
  props: ["user"],
  methods: {
    closeModal() {
      this.$emit("closeModal");
    },
    changeMode() {
      this.$emit("changeMode");
    },
    /**
     * Update the user with new user info
     * @param {Object} modified user
     */
    submit() {
      if (this.valid) {
        this.isLoading = true;
        this.$store
          .dispatch("updateUser", {
            id: this.user.id,
            full_name: this.user.full_name,
            email: this.user.email,
            vacation_days: this.user.vacation_days,
            is_admin: this.user.is_admin,
            two_factor_auth: this.resetTwoFactor ? !this.resetTwoFactor : null 
          })
          .then(() => {
            setTimeout(() => {
              this.isLoading = false;
              this.closeModal();
            }, 500)
          })
          .catch(() => {
            setTimeout(() => {
              this.isLoading = false;
            }, 500)
          });
      }
    }
  },
};
</script>

<style>
</style>