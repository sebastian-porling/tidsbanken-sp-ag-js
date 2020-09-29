<template>
  <v-container>
    <v-row align="center" justify="center">
      <v-card max-width="600px" style="padding: 2%; margin: 5%">
        <v-card-text align="center" justify="center">
          <v-avatar color="indigo" size="120" v-model="user.profile_pic">
            <span v-if="!user.profile_pic" class="white--text headline">A</span>
            <img v-if="user.profile_pic" :src="user.profile_pic" alt="profilePic" />
          </v-avatar>

          <change-profile-picture-modal :active="activateModal" :user="user" @closeModal="closeModal" />

          <br />
          <v-btn color="default" @click="launchModal" style="margin: 4%">Change Picture</v-btn>
          <v-form v-model="valid">
            <v-row>
              <v-col cols="10">
                <v-text-field v-model="user.full_name" label="Name" required :rules="nameRules"></v-text-field>
              </v-col>
              <v-col cols="10">
                <v-text-field v-model="user.email" label="Email" required :rules="emailRules"></v-text-field>
              </v-col>
              <br />
              <v-col cols="10">
                <v-text-field
                  type="password"
                  v-model="password"
                  :rules="passwordRules"
                  label="Password"
                  required
                ></v-text-field>
              </v-col>
              <!-- <v-col cols="10">
                <v-text-field
                  type="password"
                  v-model="confirm"
                  :rules="confirmRules"
                  label="Confirm Password"
                  required
                ></v-text-field>
              </v-col>-->
            </v-row>
          </v-form>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="green darken-1" text @click="submit" :disabled="!valid">Save Changes</v-btn>
        </v-card-actions>
      </v-card>
    </v-row>
  </v-container>
</template>

<script>
import ChangeProfilePictureModal from "./ChangeProfilePictureModal"
export default {
  name: "UserProfileForm",
  components: {
    "change-profile-picture-modal": ChangeProfilePictureModal
  },
  data: () => ({
    valid: true,
    activateModal: false,
    nameRules: [
      (v) => !!v || "Name is required",
      (v) => (v && v.length <= 100) || "Name must be less than 100 characters",
    ],
    emailRules: [
      (v) => !!v || "E-mail is required",
      (v) => /.+@.+\..+/.test(v) || "E-mail must be valid",
    ],
    password: "",
    passwordRules: [
      (v) => !!v || "Password is required",
      (v) => (v && v.length >= 6) || "Password must be more than 6 characters",
    ],
    /* confirm: "",
    confirmRules: [
      (v) => (v && v === this.password) || "Passwords does not match"
    ], */
  }),
  computed: {
    loggedIn() {
      return this.$store.getters.loggedIn;
    },
    user: {
      get() {
        return this.$store.getters.getCurrentUser;
      },
    },
  },
  methods: {
    twoFactorAuthenticationSettings() {
      this.$refs.form.twoFactorAuthenticationSettings();
    },
    launchModal() {
      this.activateModal = true;
    },
    closeModal() {
      this.activateModal = false;
    },
    submit() {
      this.$store
        .dispatch("updateUser", {
          id: this.user.id,
          full_name: this.user.full_name,
          email: this.user.email,
        })
        .then((response) => {
          alert(response.data.message);
          this.changePassword();
        })
        .catch((error) => {
          alert(error.data.message);
        });
    },
    changePassword() {
      if (this.password !== "" && this.password !== null) {
        this.$store
          .dispatch("changePassword", this.user.id, this.password)
          .then(() => {
            alert("User updated successfully");
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
</style>