<template>
  <v-app id="inspire">
<RequestHistoryPage />
  </v-app >
</template>

<script>
import RequestHistoryPage from './pages/request-history-page/RequestHistoryPage';

export default {
  name: 'App',
  components: {
    RequestHistoryPage
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
