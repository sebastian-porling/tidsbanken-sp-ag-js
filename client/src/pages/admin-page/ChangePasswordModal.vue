<template>
  <v-card>
    <v-card-title>
      <span class="headline">Edit password for {{ user.full_name }}</span>
    </v-card-title>
    <v-card-text>
      <v-form v-model="valid">
        <v-row>
          <v-col cols="10">
            <v-text-field
              v-model="password"
              label="Password"
              required
              :rules="passwordRules"
              type="password"
            ></v-text-field>
          </v-col>
        </v-row>
      </v-form>
    </v-card-text>
    <v-card-actions>
      <v-spacer></v-spacer>
      <v-btn color="grey darken-1" text @click="changeMode">Back</v-btn>
      <v-btn color="red darken-1" text @click="closeModal">Cancel</v-btn>
      <v-progress-circular v-if="isLoading" indeterminate color="green"></v-progress-circular>
      <v-btn v-if="!isLoading" color="green darken-1" text @click="submit" :disabled="!valid">Save Changes</v-btn>
    </v-card-actions>
  </v-card>
</template>

<script>
export default {
  name: "ChangePasswordModal",
  data() {
    return {
      isLoading: false,
      valid: true,
      password: "",
      passwordRules: [
        (v) => !!v || "Password is required",
        (v) =>
          (v && v.length >= 6) || "Password must be more than 6 characters",
      ],
    };
  },
  props: ["user"],
  methods: {
    closeModal() {
      this.$emit("closeModal");
    },
    changeMode() {
      this.$emit("changeMode");
    },
    /**
     * Updates the password on the user
     * @param {Number} user id
     * @param {String} password
     */
    submit() {
      this.isLoading = true;
      if (this.valid) {
        this.$store
          .dispatch("changePassword", this.user.id, this.password)
          .then(() => {
            setTimeout(() => {
              this.isLoading = false;
            }, 500)
          })
          .catch(() => {
            setTimeout(() => {
              this.isLoading = false;
            }, 500)
          });
      }
    },
  },
};
</script>

<style scoped>
</style>