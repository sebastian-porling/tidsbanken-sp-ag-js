<template>
  <v-form v-model="valid">
    <v-textarea v-model="comment" name="input-7-4" rows="2" label="Comment" :counter="250" :rules="rules"></v-textarea>
    <v-container class="d-flex flex-row-reverse">
      <v-progress-circular v-if="isLoading" indeterminate color="green"></v-progress-circular>
      <v-btn v-if="!isLoading" color="green darken-1" text :disabled="!valid" @click="submit">Submit</v-btn>
    </v-container>
  </v-form>
</template>

<script>
export default {
  name: "ViewRequestCommentForm",
  props: [
      'request_id'
  ],
  data() {
    return {
      isLoading: false,
      valid: true,
      comment: "",
      rules: [(v) => v.length <= 250 || "Max 250 characters"],
    };
  },
  methods: {
    submit() {
      if (this.valid) {
        this.isLoading = true;
        this.$store
          .dispatch("createComment", {
            requestId: this.request_id, 
            message: this.comment
            })
          .then(() => {
            this.isLoading = false
            this.comment = "";
            this.dialog = false;
          })
          .catch(() => {
            this.isLoading = false
          });
      }
    },
  },
};
</script>

<style></style>
