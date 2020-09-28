<template>
<v-row justify="center" align="center">
  <div v-if="qrCode" class="qr">
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
  </div>
  <v-form
    ref="form"
    @submit.prevent="login"
    v-else
    >
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

    <v-btn
      type="submit"
      class="submit"
      color="info"
    >
      Login
    </v-btn>
  </v-form>
</v-row>
</template>

<script>
  export default {
    name: 'LoginForm',
    data: () => ({
      email: '',
      emailRules: [
        v => !!v || 'E-mail is required',
        v => /.+@.+\..+/.test(v) || 'E-mail must be valid',
      ],
      password: '',
      passwordRules: [
        v => !!v || 'Password is required',
        v => (v && v.length >= 6) || 'Password must be more than 6 characters',
      ],
      qrCode: null,
      uri: ''
    }),
    computed: {
      isMobile: {
        get() {
          return window.innerWidth < 700;
        }
      }
    },
    methods: {
      login () {
         this.$store.dispatch('retrieveQr', {
          email: this.email,
          password: this.password
         })
         .then(() => {
           this.qrCode = `data:image/png;base64, ${this.$store.getters.getQrCode}`;
           this.uri = this.$store.getters.getUri;
         })
         .catch((e) => {
           if(e.data.message.includes('code')) {
             this.loginMultiAuth();
           } else {
             this.email = '';
             this.password = '';
           }
           
         }) 
      },
      loginMultiAuth() {
        this.$emit('loginMultiAuth', this.email, this.password);
      }
    },
  }
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