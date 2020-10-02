<template>
  <v-list style="max-height: 225px" class="overflow-y-auto">
    <v-list-item v-for="comment in comments" :key="comment.id">
      <v-list-item-avatar>
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
      </v-list-item-avatar>

      <v-list-item-content>
        <v-list-item-title>{{ comment.user.name }}</v-list-item-title>
        <v-list-item-subtitle>

          <p>{{ comment.modified_at | formatDate }}</p>

          <p v-if="!editMode || currentComment !== comment.id" class="text-wrap" style="color: black">{{ comment.message }}</p>

          <v-text-field 
          v-if="editMode && currentComment === comment.id" 
          v-on:keyup.enter="editComment(comment)" 
          v-model="comment.message"
          ></v-text-field>

        </v-list-item-subtitle>
      </v-list-item-content>

      <v-list-item-action>
            <v-icon small v-if="!editMode || currentComment !== comment.id" class="mr-2" @click="changeMode(comment.id)">mdi-pencil</v-icon>
            <v-icon small v-if="editMode && currentComment === comment.id" class="mr-2" @click="changeMode(0)">mdi-close</v-icon>
        </v-list-item-action>
        <v-list-item-action>
              <v-icon small @click="deleteComment(comment)">mdi-delete</v-icon>
        </v-list-item-action>

    </v-list-item>
  </v-list>
</template>

<script>
export default {
  props: ["request_id"],
  data() {
      return {
          editMode: false,
          currentComment: null
      }
  },
  created() {
    this.$store.dispatch("retrieveComments", this.request_id);
  },
  computed: {
    comments: {
      get() {
        return this.$store.getters.getComments;
      },
    },
  },
  filters: {
    initials: (data) => {
      if (!data) return "UU";
      data = data.toString();
      data = data.split(" ");
      if (data.length < 2) return "UU";
      return data[0].charAt(0).toUpperCase() + data[1].charAt(0).toUpperCase();
    },
    formatDate: (date) => {
      if (!date) {
        return "";
      }
      return date.substring(0, 10) + " at " + date.substring(11, 16);
    },
  },
  methods: {
      deleteComment(comment) {
          console.log(comment.message);
          // Delete comment
          this.$store
          .dispatch("deleteComment", {
            requestId: this.request_id, 
            commentId: comment.id
            })
      }, 
      editComment(comment) {
          console.log(comment.message);
          // patch comment
          this.$store
          .dispatch("updateComment", {
            requestId: this.request_id, 
            message: comment.message,
            commentId: comment.id
            })
          .then(() => {
            this.changeMode(0);
          })
          .catch(() => {
          });
      },
      changeMode(id){
          this.editMode = !this.editMode;
          this.currentComment = id;
      }
  }
};
</script>

<style></style>
