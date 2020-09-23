<template>
<v-row justify="center" align="center">
  <div v-if="qrCode" class="qr">
    <h1>Scan the QR code...</h1>
    <h5>...and add it to your authentication app!</h5>
    <img :src="qrCode" alt="QR-code for multi authentication" />
    <v-btn @click="loginMultiAuth">Login</v-btn>
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
      qrCode: null
    }),
    methods: {
      login () {
         this.$store.dispatch('retrieveQr', {
          email: this.email,
          password: this.password
         })
         .then(() => {
           this.qrCode = `data:image/png;base64, ${this.$store.state.qrCode}`;
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