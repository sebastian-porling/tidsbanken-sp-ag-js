<template>
<v-row justify="center" align="center">
    <v-form
      ref="form"
      @submit.prevent="login"
    >

      <v-text-field
        v-model="email"
        :rules="emailRules"
        label="E-mail"
        required
        disabled
      ></v-text-field>

      <v-text-field
        type="password"
        v-model="password"
        :rules="passwordRules"
        label="Password"
        required
        disabled
      ></v-text-field>

      <v-text-field
        v-model="code"
        :rules="codeRules"
        label="Code"
        required
      ></v-text-field>

      <v-btn
      type="submit"
        class="submit"
        color="info"
        text
      >
        Login
      </v-btn>
    </v-form>
</v-row>
</template>

<script>
  export default {
    name: 'LoginForm',
    props: ['email', 'password'],
    data: () => ({
      emailRules: [
        v => !!v || 'E-mail is required',
        v => /.+@.+\..+/.test(v) || 'E-mail must be valid',
      ],
      passwordRules: [
        v => !!v || 'Password is required',
        v => (v && v.length >= 6) || 'Password must be more than 6 characters',
      ],
      code: '',
      codeRules: [
        v => !!v || 'Two-Factor Authentication required',
        v => (v && v.length === 6) || 'The code must be 6 characters',
      ]
    }),
    methods: {
      login () {
         this.$store.dispatch('retrieveToken', {
          email: this.email,
          password: this.password,
          code: this.code
         })
         .then(() => {
           this.$store.dispatch('establishClientSocket')
           this.$router.push('/')
         })
         .catch((e) => {
           console.log(e);
             this.code = '';
         })
      }
    },
  }
</script>

<style>

</style>