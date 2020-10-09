<template>
  <v-container class="request-history-page">
    <v-card style="padding: 10px;">
      <v-row>
        <UserInfoComponent :user="user" :userId="id" />
      </v-row>
      <br />
      <v-card-text>
        <RequestHistoryTable :user="user" :userId="id" />
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
  /**
   * Instansiates a request to retireve information about a
   * specific user
   * @param {Number} id
   */
  created() {
    this.$store.dispatch("retrieveUser", this.id);
  },
  /**
   * Fetches the current user and the user who's vacation
   * request history to view
   */
  computed: {
    currentUser() {
      return this.$store.getters.getCurrentUser;
    },
    user() {
      return this.$store.getters.getFetchedUser;
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