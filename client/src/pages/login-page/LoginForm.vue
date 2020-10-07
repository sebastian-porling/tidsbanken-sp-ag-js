<template>
<<<<<<< HEAD
<v-row justify="center" align="center">
  <v-col cols="12" sm="8" md="6" lg="4" xl="3" v-if="qrCode" class="qr">
    <v-card style="margin-bottom: 30px;">
      <v-card-title>Scan the QR code...</v-card-title>
      <v-card-subtitle>...and add it to your authentication app!</v-card-subtitle>
      <img :src="qrCode" alt="QR-code for multi authentication" />
      <div v-if="isMobile" style="padding-bottom: 10px;">
        <v-card-text>Or click: </v-card-text>
        <v-btn text :href="uri" color="deep-purple lighten-2">Add 2FA</v-btn>
      </div>
    </v-card>
    
    <v-btn text @click="loginMultiAuth" color="indigo lighten-2">Login</v-btn>
  </v-col>
  <v-col cols="12" sm="8" md="6" lg="4" xl="3" v-else>
    <v-card style="margin: 0 20px;">
  <v-card-title>Sign in</v-card-title>
    <v-form
    ref="form"
    @submit.prevent="login"
    
    >
    <v-card-text>
    <v-text-field
      v-model="email"
      :rules="emailRules"
      label="E-mail"
      required
    ></v-text-field>

    <v-text-field
      type="password"
      v-model="password"
      :rules="passwordRules"
      label="Password"
      required
    ></v-text-field>
  </v-card-text>
  <v-card-actions>
    <v-spacer/>
    <v-btn
      type="submit"
      class="submit"
      color="info"
      text
    >
      Login
    </v-btn>
    </v-card-actions>
  </v-form>
  </v-card>
  </v-col>
</v-row>
=======
  <v-row justify="center" align="center">
    <v-col cols="12" sm="8" md="6" lg="4" xl="3" v-if="qrCode" class="qr">
      <v-card style="margin-bottom: 30px">
        <v-card-title>Scan the QR code...</v-card-title>
        <v-card-subtitle
          >...and add it to your authentication app!</v-card-subtitle
        >
        <img :src="qrCode" alt="QR-code for multi authentication" />
        <div v-if="isMobile" style="padding-bottom: 10px">
          <v-card-text>Or click: </v-card-text>
          <v-btn text :href="uri" color="deep-purple lighten-2">Add 2FA</v-btn>
        </div>
      </v-card>

      <v-btn text @click="loginMultiAuth" color="indigo lighten-2">Login</v-btn>
    </v-col>

    <v-col cols="12" sm="8" md="6" lg="4" xl="3" v-else>
      <v-card style="margin: 0 20px">
        <v-card-title>Sign in</v-card-title>

        <v-form ref="form" @submit.prevent="login">
          <v-card-text>
            <v-text-field
              v-model="email"
              :rules="emailRules"
              label="E-mail"
              required
            ></v-text-field>

            <v-text-field
              type="password"
              v-model="password"
              :rules="passwordRules"
              label="Password"
              required
            ></v-text-field>
          </v-card-text>
          <v-card-actions>
            <v-spacer />
            <v-progress-circular v-if="submit" indeterminate color="info"></v-progress-circular>
            <v-btn v-if="!submit" type="submit" class="submit" color="info" text>
              Login
            </v-btn>
          </v-card-actions>
        </v-form>
      </v-card>
    </v-col>
  </v-row>
>>>>>>> 47897119bd8ae2427c0f89ef035a1a921039f60c
</template>

<script>
export default {
  name: "LoginForm",
  data: () => ({
    submit: false,
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
    qrCode: null,
    uri: "",
  }),
  inject: {
    theme: {
      default: { isDark: false },
    },
  },
  computed: {
    isMobile: {
      get() {
        return window.innerWidth < 700;
      },
    },
  },
  methods: {
    login() {
      this.submit = true;
      this.$store
        .dispatch("retrieveQr", {
          email: this.email,
          password: this.password,
        })
        .then(() => {
          setTimeout(() => (this.submit = false), 500);          
          this.qrCode = `data:image/png;base64, ${this.$store.getters.getQrCode}`;
          this.uri = this.$store.getters.getUri;
        })
        .catch((e) => {
          if (e.data.message.includes("code")) {
            this.loginMultiAuth();
          } else {
            this.email = "";
            this.password = "";
          }
          setTimeout(() => (this.submit = false), 500);          
        });
    },
    loginMultiAuth() {
      this.$emit("loginMultiAuth", this.email, this.password);
    },
  },
};
</script>

<style>
.qr {
  position: relative;
  text-align: center;
}
.qr img {
  display: block;
  width: 100%;
}
</style>