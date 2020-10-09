<template>
  <v-dialog v-model="active" persistent max-width="400px">
    <v-card>
      <v-card-text>
        <v-form v-model="valid">
          <v-row>
            <v-col cols="12"> </v-col>
            <v-col cols="12">
              <v-text-field
                type="text"
                outlined
                clearable
                label="Url"
                v-model="url"
                :rules="picRules"
                prepend-icon="mdi-file-image"
              ></v-text-field>
            </v-col>
          </v-row>
        </v-form>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="red darken-1" text @click="closeModal">Cancel</v-btn>
        <v-btn color="primary darken-1" text @click="submit" :disabled="!valid"
          >Submit</v-btn
        >
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
export default {
  name: "ChangeProfilePictureModal",
  data() {
    return {
      valid: true,
      picRules: [
          (v) => !!v || "Url is required",
          (v) => /^(https?|chrome):\/\/[^\s$.?#].[^\s]*$/.test(v) || "Not a valid url"
        ],
        url: ''
    };
  },
  mounted() {
      this.url = this.user.profile_pic
  },
  methods: {
    closeModal() {
      this.$emit("closeModal");
    },
    openModal() {
      this.dialog = true;
    },
    submit() {
      this.$store
        .dispatch("updateUser", {
          id: this.user.id,
          profile_pic: this.url,
        })
        .then(() => {
          this.closeModal();
        })
        .catch(() => {
        });
    },
  },
  props: ["active", "user"],
};
</script>

<style></style>
