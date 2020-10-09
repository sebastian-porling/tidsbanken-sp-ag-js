<template>
  <v-card v-if="setting">
    <v-card-title>
      <span class="headline">Edit Application Setting</span>
    </v-card-title>
    <v-card-text>
      <v-form v-model="valid">
        <v-row justify="end">
          <v-col cols="12" sm="6" md="6">
            <v-text-field
              label="Setting"
              v-model="setting.key"
              :rules="keyRules"
            ></v-text-field>
          </v-col>
          <v-col cols="12" sm="6" md="6">
            <v-text-field
              label="Value"
              v-model="setting.value"
              :rules="valueRules"
            ></v-text-field>
          </v-col>
        </v-row>
      </v-form>
    </v-card-text>
    <v-card-actions>
      <v-row justify="end" style="margin-right: 10px">
        <v-btn color="red darken-1" text @click="closeEdit">Back</v-btn>

        <v-progress-circular
          v-if="isLoading"
          indeterminate
          color="green"
        ></v-progress-circular>
        <v-btn
          v-if="!isLoading"
          color="green"
          text
          v-on:click="editSetting"
          :disabled="!valid"
          >Edit</v-btn
        >
      </v-row>
    </v-card-actions>
  </v-card>
</template>

<script>
export default {
  name: "ApplicationSettingsEditForm",
  props: ["setting"],
  data() {
    return {
      isLoading: false,
      valid: true,
      keyRules: [(v) => !!v || "Field is required"],
      valueRules: [(v) => !!v || "Field is required"],
    };
  },
  methods: {
    /**
     * Modifies the setting with the modified setting data
     * @param {Object} modified setting
     */
    editSetting() {
      this.isLoading = true;
      if (this.setting.key && this.setting.value) {
        this.$store
          .dispatch("patchSetting", {
            id: this.setting.id,
            key: this.setting.key,
            value: this.setting.value,
          })
          .then(() => {
            setTimeout(() => {
              this.isLoading = false;
            }, 500);
          })
          .catch(() => {
            setTimeout(() => {
              this.isLoading = false;
            }, 500);
          });
        this.$emit("showSettings");
      }
    },
    closeEdit() {
      this.$emit("showSettings");
    },
  },
};
</script>

<style></style>
