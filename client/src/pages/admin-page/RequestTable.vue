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
        max-height="1000"
        type="card-heading, image, table-tfoot"
      ></v-skeleton-loader>
    </v-sheet>

    <transition name="fade">
      <v-row v-if="!isLoading">
        <v-card style="padding: 10px">
          <v-card-title class="justify-center">
            Vacations request from all users
          </v-card-title>

          <v-data-table
            :headers="headers"
            :items="requests"
            :items-per-page="10"
            :sort-by="['created_at']"
            :sort-desc="[true]"
            class="elevation-1"
            @click:row="launchModal"
          >
            <template v-slot:item.status="{ item }">
              <v-chip :color="getColor(item.status.status)" dark>{{
                item.status.status
              }}</v-chip>
            </template>
          </v-data-table>
          <view-request-modal
            :active="activateModal"
            :request="request"
            @closeModal="closeModal"
          />
        </v-card>
      </v-row>
    </transition>
  </v-container>
</template>

<script>
import ViewRequestModal from "../../components/shared/ViewRequestModal";
export default {
  name: "RequestHistoryTable",
  components: {
    "view-request-modal": ViewRequestModal,
  },
  /**
   * Changes color of skeletion loader according to 
   * application theme
   */
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
          text: "Title",
          align: "start",
          sortable: false,
          value: "title",
        },
        { text: "Start", value: "start" },
        { text: "End", value: "end" },
        { text: "Status", value: "status" },
        {
          text: "Created",
          value: "created_at",
        },
        { text: "Owner", value: "owner.full_name" },
      ],
      activateModal: false,
      request: {},
    };
  },
  /**
   * Initialises request to fetch all requests
   */
  created() {
    this.$store.dispatch("retrieveAllRequests").then(() => {
      this.isLoading = false;
    });
  },
  /**
   * Fetches current user and all requests
   */
  computed: {
    user: {
      get() {
        return this.$store.getters.getCurrentUser;
      },
      set() {
        return this.$store.getters.getCurrentUser;
      }
    },
    requests: {
      get() {
        return this.$store.getters.getAllRequests;
      },
      set() {
        return this.$store.getters.getAllRequests;
      }
    },
  },
  methods: {
    /**
     * Sets color for vacation request status
     */
    getColor(status) {
      switch (status) {
        case "Pending":
          return "orange";
        case "Approved":
          return "green";
        case "Denied":
          return "red";
      }
    },
    launchModal(value) {
      this.request = value;
      this.$store.dispatch("retrieveComments", value.id);
      this.activateModal = true;
    },
    closeModal() {
      this.activateModal = false;
      this.request = {};
    },
  },
};
</script>

<style>
</style>