<template>
  <v-app id="inspire">
    <!-- Header should only be visible if the user is logged in  -->
    <Header />
    <response-alert v-if="isAlert" />
    <router-view :key="$route.path" />
  </v-app >
</template>

<script>
import Header from './components/layout/Header'
import ResponseAlert from './components/layout/ResponseAlert'

export default {
  name: 'App',
  components: {
    Header,
    'response-alert': ResponseAlert
  },
  computed: {
    isAlert: {
      get() {
        return this.$store.getters.getIsAlert;
      }
    }
  },
  created() {
    if(this.$store.getters.loggedIn) {
      this.$store.dispatch('establishClientSocket');
    }
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
.fade-enter-active, .fade-leave-active {
  transition: opacity .5s;
}
.fade-enter, .fade-leave-to /* .fade-leave-active below version 2.1.8 */ {
  opacity: 0;
}
.v-progress-circular {
  margin: 1rem;
}
</style>
