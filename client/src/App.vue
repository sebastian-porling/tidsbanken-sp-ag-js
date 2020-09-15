<template>
  <v-app id="inspire">
    <UserProfilePage />
  </v-app >
</template>

<script>
import UserProfilePage from './pages/user-profile-page/UserProfilePage';

export default {
  name: 'App',
  components: {
    UserProfilePage
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
