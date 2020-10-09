<template>
<v-container class="login-page">
  <LoginForm v-if="!multiAuth" @loginMultiAuth="loginMultiAuth"/>
  <LoginMultiAuthForm v-else :email="email" :password="password"/>
</v-container>
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
    /**
     * Change to multi auth login component
     */
    loginMultiAuth(email, password) {
      this.multiAuth = true;
      this.email = email;
      this.password = password;
    }
  },
}
</script>

<style>
.login-page{
  display: flex;
  justify-content: center;
  align-content: center;
  height: 100%;
}
</style>