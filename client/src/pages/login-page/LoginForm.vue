<template>
<v-row justify="center" align="center">
    <v-form
      ref="form"
      v-model="valid"
      :lazy-validation="lazy"
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
        color="info"
        @click="login"
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
      valid: true,
      name: '',
      nameRules: [
        v => !!v || 'Name is required',
        v => (v && v.length <= 10) || 'Name must be less than 100 characters',
      ],
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
        this.$refs.form.login()
      }
    },
  }
</script>

<style>

</style>