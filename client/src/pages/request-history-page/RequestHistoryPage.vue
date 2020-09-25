<template>
<v-col class="container">
  <br>
  <br>
  <div v-if="user">
    <v-row>
    <UserInfoComponent :user="user" />
  </v-row>
  <br>
  <v-row>
    <RequestHistoryTable :user="user" />
  </v-row>
  </div>
  <div v-if="!user">
    <h1>Something went wrong..</h1>
  </div>
</v-col>
</template>

<script>
import UserInfoComponent from './UserInfoComponent';
import RequestHistoryTable from './RequestHistoryTable';

export default {
  name: 'RequestHistoryPage',
  components: {
    UserInfoComponent,
    RequestHistoryTable
  },
  props: {
    id: {
      type: Number,
      required: true
    }
  },
  created() {
    this.$store.dispatch("retrieveAllUsers");
  },
  computed: {
    user() {
      return this.$store.getters.getAllUsers.find(
        user => user.id === this.id
      )
    }
  }
}
</script>

<style>

</style>