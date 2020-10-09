<template>
    <v-container v-model="active">
        <settings-info v-if="!showEdit" @editSetting="toggleCard" @closeModal="closeModal"/>
        <settings-edit-form v-else @showSettings="toggleCard" :setting="setting"/>
    </v-container>
</template>

<script>
import ApplicationSettingsInfo from './ApplicationSettingsInfo';
import ApplicationSettingsEditForm from './ApplicationSettingsEditForm';

export default {
    name: "ApplicationSettingsModal",
    components: {
      'settings-info': ApplicationSettingsInfo,
      'settings-edit-form': ApplicationSettingsEditForm
    },
    data() {
        return {
          showEdit: false,
          setting: null
        };
    },
    props: ["active"],
    methods: {
      /**
       * Toggle edit form and info card
       */
       toggleCard(setting) {
         this.setting = setting;
         this.showEdit = !this.showEdit;
       },
       closeModal() {
            this.$emit("closeModal");
      },
    }
};
</script>

<style scoped>

</style>
