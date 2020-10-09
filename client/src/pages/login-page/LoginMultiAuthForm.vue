<template>
<v-row justify="center" align="center">
   <v-col cols="12" sm="8" md="6" lg="4" xl="3">
    <v-card style="margin: 0 20px;">
      <v-card-title>Sign in with 2fa</v-card-title>
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
      </v-card-text>
      <v-card-actions>
      <v-spacer />
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
         .catch(() => {
             this.code = '';
         })
      }
    },
  }
</script>

<style>

</style>