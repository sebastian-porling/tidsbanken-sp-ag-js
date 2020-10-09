<template>
  <v-container>
    <v-sheet
      v-if="isLoading"
      class="pa-10"
    >
      <v-skeleton-loader
        v-bind="attrs"
        class="mx-auto"
        max-width="600"
        max-height="600"
        type="card-heading, image, table-tfoot"
      ></v-skeleton-loader>
    </v-sheet>

    <transition name="fade">
      <v-row v-if="!isLoading">
        <v-card class="users">
          <user-modal
            :active="activateModal"
            :user="user"
            @closeModal="closeModal"
          />
          <create-user-modal />
          <v-card-title class="justify-center"
            >Users of Tidsbanken</v-card-title
          >
          <v-card-text>
            <v-data-table
              :headers="headers"
              :items="users"
              :items-per-page="10"
              class="elevation-1"
            >
              <template v-slot:item.actions="{ item }">
                <v-icon small class="mr-2" @click="launchModal(item)"
                  >mdi-pencil</v-icon
                >
                <v-icon small @click="deleteUser(item)">mdi-delete</v-icon>
              </template>
              ></v-data-table
            >
          </v-card-text>
        </v-card>
      </v-row>
    </transition>
  </v-container>
</template>

<script>
import CreateUserModal from "@/pages/admin-page/CreateUserModal";
import UserModal from "@/pages/admin-page/UserModal";
export default {
  name: "UserTable",
  components: {
    "create-user-modal": CreateUserModal,
    "user-modal": UserModal,
  },
  inject: {
    theme: {
      default: { isDark: false },
    },
  },
  data() {
    return {
      isLoading: true,
      headers: [
        {
          align: "start",
          sortable: false,
          value: "title",
        },
        { text: "Id", value: "id" },
        { text: "Name", value: "full_name" },
        { text: "Email", value: "email" },
        { text: "Actions", value: "actions" },
      ],
      user: {},
      activateModal: false,
    };
  },
  created() {
    this.$store.dispatch("retrieveAllUsers")
        .then(() => {
          setTimeout(() => (this.isLoading = false), 500);
        });
  },
  computed: {
    users: {
      get() {
        return this.$store.getters.getAllUsers;
      },
      set() {
        return this.$store.getters.getAllUsers;
      }
    },
  },
  methods: {
    launchModal(value) {
      this.user = value;
      this.activateModal = true;
    },
    closeModal() {
      this.user = {};
      this.activateModal = false;
    },
    deleteUser(user) {
      this.$store.dispatch("deactivateUser", user.id);
    },
  },
};
</script>

<style>
.users {
  position: relative;
}
</style>
