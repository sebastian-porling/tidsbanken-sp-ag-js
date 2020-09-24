<template>
  <v-main>
    <v-row>
      <edit-user-modal :active="activateModal" :user="user" @closeModal="closeModal" />
      <create-user-modal />
    </v-row>
    <v-row>      
      <v-data-table
        :headers="headers"
        :items="users"
        :items-per-page="10"
        class="elevation-1"
        @click:row="launchModal"
      ></v-data-table>
    </v-row>
  </v-main>
</template>

<script>
import CreateUserModal from "@/pages/admin-page/CreateUserModal";
import EditUserModal from "@/pages/admin-page/EditUserModal";
export default {
  name: "UserTable",
  components: {
    "create-user-modal": CreateUserModal,
    "edit-user-modal": EditUserModal,
  },
  data() {
    return {
      headers: [
        {
          align: "start",
          sortable: false,
          value: "title",
        },
        { text: "Id", value: "id" },
        { text: "Name", value: "full_name" },
        { text: "Email", value: "email" },
      ],
      user: {},
      activateModal: false,
    };
  },
  created() {
    this.$store.dispatch("retrieveAllUsers");
  },
  computed: {
    users: {
      get() {
        return this.$store.getters.getAllUsers;
      },
    },
  },
  methods: {
    launchModal(value) {
      this.user = value;
      console.log(this.user);
      this.activateModal = true;
    },
    closeModal() {
      this.user = {};
      this.activateModal = false;
    },
  },
};
</script>

<style>
</style>