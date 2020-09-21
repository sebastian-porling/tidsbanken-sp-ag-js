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
      ]
    }),

    methods: {
      login () {
         this.$store.dispatch('retrieveToken', {
          email: this.email,
          password: this.password
         })
         .then(() => {
           this.$router.push('/dashboard')
         })
      }
    },
  }
</script>

<style>

</style>