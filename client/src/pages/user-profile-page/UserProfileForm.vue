<template>
  <v-row justify="center" align="center">
    <v-form ref="form" v-model="valid" :lazy-validation="lazy">

      <v-row justify="center">
        <v-avatar color="indigo" size="120" v-model="profile_pic">
          <span class="white--text headline">A</span>
        </v-avatar>
      </v-row>

      <v-row justify="center" class="my-8">
        <v-btn color="default" @click="changePicture">Change Picture</v-btn>
      </v-row>


      <v-text-field v-model="name" :counter="100" :rules="nameRules" label="Name" required></v-text-field>

      <v-text-field v-model="email" :rules="emailRules" label="E-mail" required></v-text-field>

      <v-text-field
        type="password"
        v-model="password"
        :rules="passwordRules"
        label="Password"
        required
      ></v-text-field>

      <v-btn
      class="my-8"
        color="default"
        @click="twoFactorAuthenticationSettings"
      >Add/Remove Two-Factor-Authentication</v-btn>

        <br>
      <v-btn color="info" @click="saveChanges">Save Changes</v-btn>
    </v-form>
  </v-row>
</template>

<script>
export default {
  name: 'UserProfileForm',
  data: () => ({
    valid: true,
    name: "",
    nameRules: [
      (v) => !!v || "Name is required",
      (v) => (v && v.length <= 10) || "Name must be less than 100 characters",
    ],
    email: "",
    emailRules: [
      (v) => !!v || "E-mail is required",
      (v) => /.+@.+\..+/.test(v) || "E-mail must be valid",
    ],
    password: "",
    passwordRules: [
      (v) => !!v || "Password is required",
      (v) => (v && v.length >= 6) || "Password must be more than 6 characters",
    ],
  }),

  methods: {
    saveChanges() {
      this.$refs.form.saveChanges();
    },
    twoFactorAuthenticationSettings() {
      this.$refs.form.twoFactorAuthenticationSettings();
    },
    changePicture() {
      this.$refs.form.changePicture();
    },
  },
};
</script>

<style>
</style>