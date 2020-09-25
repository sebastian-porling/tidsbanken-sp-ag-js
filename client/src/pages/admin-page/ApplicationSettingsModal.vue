<template>
    <v-dialog v-model="active" persistent width="600px">
        <settings-info v-if="!showEdit" @editSetting="toggleCard" @closeModal="closeModal"/>
        <settings-edit-form v-else @showSettings="toggleCard" :setting="setting"/>
    </v-dialog>
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
    created() {

    },
    computed: {
    },
    methods: {
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
