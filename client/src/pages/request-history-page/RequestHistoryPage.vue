<template>
  <v-col class="container">
    <br />
    <br />
    <div v-if="user">
      <v-row>
        <UserInfoComponent :user="user" />
      </v-row>
      <br />
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
import UserInfoComponent from "./UserInfoComponent";
import RequestHistoryTable from "./RequestHistoryTable";

export default {
  name: "RequestHistoryPage",
  components: {
    UserInfoComponent,
    RequestHistoryTable,
  },
  props: {
    id: {
      type: String,
      required: true,
    },
  },
  created() {
    this.$store.dispatch("retrieveAllUsers");
  },
  computed: {
    currentUser() {
      return this.$store.getters.getCurrentUser;
    },
    user() {
      if (this.currentUser.is_admin) {
        return this.$store.getters.getAllUsers.find(
          (user) => user.id === parseInt(this.id)
        )
      } else {
        return this.currentUser;
      }
    },
  },
};
</script>

<style>
</style>