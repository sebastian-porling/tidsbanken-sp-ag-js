<template>
  <v-container class="request-history-page">
    <v-card style="padding: 10px;">
      <v-row>
        <UserInfoComponent :user="user" />
      </v-row>
      <br />
      <v-card-text>
        <RequestHistoryTable :user="user" />
      </v-card-text>
    </v-card>
  </v-container>
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
.request-history-page{
  display: flex;
  justify-content: center;
  align-content: center;
  margin-top: 100px;
}
</style>