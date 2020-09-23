<template>
<v-row justify="center" align="center">
  <LoginForm v-if="!multiAuth" @loginMultiAuth="loginMultiAuth"/>
  <LoginMultiAuthForm v-else :email="email" :password="password"/>
</v-row>
</template>

<script>
import LoginForm from './LoginForm';
import LoginMultiAuthForm from './LoginMultiAuthForm';

export default {
  name: 'LoginPage',
  components: {
    LoginForm,
    LoginMultiAuthForm
  },
  data() {
    return {
      multiAuth: false,
      email: '',
      password: ''
    }
  },
  methods: {
    loginMultiAuth(email, password) {
      this.multiAuth = true;
      this.email = email;
      this.password = password;
    }
  },
}
</script>

<style>

</style>