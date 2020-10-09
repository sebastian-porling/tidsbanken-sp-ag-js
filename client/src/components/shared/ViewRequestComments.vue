<template>
  <v-container>
    <v-skeleton-loader
      v-if="isLoading"
      v-bind="attrs"
      type="list-item-avatar-three-line"
    ></v-skeleton-loader>
    <v-skeleton-loader
      v-if="isLoading"
      v-bind="attrs"
      type="list-item-avatar-three-line"
    ></v-skeleton-loader>

    <v-list style="max-height: 225px" class="overflow-y-auto" v-if="!isLoading">
      <v-list-item v-for="comment in comments" :key="comment.id">
        <v-list-item-avatar>
          <router-link
                :to="{ name: 'RequestHistory', params: { id: comment.user.user_id.toString() } }"
                style="text-decoration: none;"
              >
               <v-avatar color="light-blue" size="36">
            <img
              v-if="comment.user.profile_pic"
              :src="comment.user.profile_pic"
              :alt="comment.user.name | initials"
            />
            <span v-else class="white--text headline">{{
              comment.user.name | initials
            }}</span>
          </v-avatar>
              </router-link>
          
        </v-list-item-avatar>

        <v-list-item-content>
          <v-list-item-title>{{ comment.user.name }}</v-list-item-title>
          <v-list-item-subtitle>
            <p>{{ comment.created_at | formatDate }}
              <v-small
              v-if="comment.created_at !== comment.modified_at"
            small
            ><i>(Edited)</i></v-small>
            </p>

            <p
              v-if="!editMode || currentComment !== comment.id"
              class="text-wrap"
              style="color: black"
            >
              {{ comment.message }}
            </p>

            <v-text-field
              v-if="editMode && currentComment === comment.id"
              v-on:keyup.enter="editComment(comment)"
              v-model="comment.message"
            ></v-text-field>
          </v-list-item-subtitle>
        </v-list-item-content>

        <v-list-item-action>
          <v-icon
            small
            v-if="!editMode || currentComment !== comment.id"
            class="mr-2"
            @click="changeMode(comment.id)"
            >mdi-pencil</v-icon
          >
          <v-icon
            small
            v-if="editMode && currentComment === comment.id"
            class="mr-2"
            @click="changeMode(0)"
            >mdi-close</v-icon
          >
        </v-list-item-action>
        <v-list-item-action>
          <v-icon small @click="deleteComment(comment)">mdi-delete</v-icon>
        </v-list-item-action>
      </v-list-item>
    </v-list>
  </v-container>
</template>

<script>
export default {
  props: ["request_id"],
  data() {
    return {
      isLoading: true,
      editMode: false,
      currentComment: null,
    };
  },
  /**
   * Instantiate request to fetch all comments for a specific request
   * @param (Number) id
   */
  created() {
    this.$store
      .dispatch("retrieveComments", this.request_id)
      .then(() => {
        this.isLoading = false;
      })
      .catch(() => {
        this.isLoading = false;
      });
  },
  /**
   * Fetches all comments
   */
  computed: {
    comments: {
      get() {
        return this.$store.getters.getComments;
      },
    },
  },

  filters: {
    /**
   * Generates a profile image placeholder if no name exists
   * or if name is less than 2 characters
   */
    initials: (data) => {
      if (!data) return "UU";
      data = data.toString();
      data = data.split(" ");
      if (data.length < 2) return "UU";
      return data[0].charAt(0).toUpperCase() + data[1].charAt(0).toUpperCase();
    },
    /**
     * Format dates and time to DD-MM-YYYY HH:MM AM/PM
     */
    formatDate: (date) => {
      if (!date) {
        return "";
      }
      return date.substring(0, 10) + " at " + date.substring(11, 19);
    },
  },
  methods: {
    /**
     * Deletes a comment
     * @param {Object} requestId commentId
     */
    deleteComment(comment) {
        this.$store.dispatch("deleteComment", {
          requestId: this.request_id,
          commentId: comment.id,
        });
    },
    /**
     * Edits comment
     * @param {Object} requestId message commentId
     */
    editComment(comment) {
      this.$store
        .dispatch("updateComment", {
          requestId: this.request_id,
          message: comment.message,
          commentId: comment.id,
        })
        .then(() => {
          this.changeMode(0);
        })
        .catch(() => {});
    },
    /**
     * Changes mode between edit and view
     */
    changeMode(id) {
      this.editMode = !this.editMode;
      this.currentComment = id;
    },
  },
};
</script>

<style></style>
