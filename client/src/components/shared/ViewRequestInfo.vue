<template>
  <v-card>
    <v-card-title>
      <span class="headline">{{ request.title }}</span>
      <v-chip
        class="ma-2"
        :color="getColor(request.status.status)"
        text-color="white"
      >{{ request.status.status }}</v-chip>
    </v-card-title>
    <v-card-text>
      <v-container>
        <v-row>
          <v-col cols="12">
            <v-btn text>
              <v-avatar color="light-blue" size="36">
                <span v-if="!request.owner.profile_pic" class="white--text headline">
                  {{ request.owner.full_name | initials }}
                </span>
                <img
                  v-if="request.owner.profile_pic"
                  :src="request.owner.profile_pic"
                  alt="profilePic"
                />
              </v-avatar>
              <router-link
                :to="{ name: 'RequestHistory', params: { id: request.owner.owner_id.toString() } }"
                style="text-decoration: none;"
              >
                <strong style="margin-left: 5px">{{ request.owner.full_name }}</strong>
              </router-link>
            </v-btn>
          </v-col>
          <v-col cols="12" xs="6" sm="6" md="6">
            <v-text-field type="date" label="Start" :value="request.start | formatDate" disabled></v-text-field>
          </v-col>
          <v-col cols="12" xs="6" sm="6" md="6">
            <v-text-field type="date" label="End" :value="request.end | formatDate" disabled></v-text-field>
          </v-col>
          <v-col cols="12" v-if="request.owner.owner_id === currentUser.id || currentUser.is_admin">
            <view-request-comments v-if="currentUser.id === request.owner.owner_id || currentUser.is_admin" :request_id="request.id" />
            <view-request-comment-form v-if="currentUser.id === request.owner.owner_id || currentUser.is_admin" :request_id="request.id" />
          </v-col>
        </v-row>
      </v-container>
    </v-card-text>
    <v-card-actions>
      <v-spacer></v-spacer>
      <v-btn v-if="currentUser.is_admin" color="red darken-1" text @click="deleteRequest">Delete</v-btn>
      <v-btn color="grey darken-1" text @click="closeModal">Cancel</v-btn>
      <v-btn color="primary darken-1" text @click="changeMode">Edit</v-btn>
    </v-card-actions>
  </v-card>
</template>

<script>
import ViewRequestCommentForm from "./ViewRequestCommentForm";
import ViewRequestComments from "./ViewRequestComments";

export default {
  name: "ViewRequestInfo",
  components: {
    "view-request-comment-form": ViewRequestCommentForm,
    "view-request-comments": ViewRequestComments,
  },
  filters: {
    /**
   * Formats the date to only show DD-MM-YYYY
   */
    formatDate: (date) => {
      if (!date) {
        return "";
      }
      return date.substring(0, 10);
    },
      /**
   * Generates a profile image placeholder if no name exists
   * or if name is less than 2 characters
   */
    initials: (data) => {
            if (!data) return '😁';
            data = data.toString();
            data = data.split(' ');
            if(data.length < 2) return '😁';
            return  data[0].charAt(0).toUpperCase() + 
                    data[1].charAt(0).toUpperCase();
        },
  },
  props: ["request"],
  methods: {
    closeModal() {
        this.$emit("closeModal");
    },
    changeMode() {
      this.$emit("changeMode");
    },
    /**
     * Sets colors according to state
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
    /**
     * Deletes the vacation request
     */
    deleteRequest() {
      if (confirm("You sure you want to delete this request?")) {
        this.$store
          .dispatch("deleteRequest", this.request)
          .then(() => {
            this.closeModal();
          })
          .catch((error) => {
            alert(error);
          });
      }
    },
  },
  /**
   * Fetches current user
   */
  computed: {
    currentUser() {
      return this.$store.getters.getCurrentUser;
    },
  },
};
</script>

<style></style>
