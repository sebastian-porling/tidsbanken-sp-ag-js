<template>
  <v-main>
    <v-row justify="space-around">
      <v-col cols="4" >
        <v-card class="mx-auto" max-width="300" tile>
          <v-list rounded>
            <v-subheader>Menu</v-subheader>
            <v-list-item-group v-model="item" color="primary">

              <v-list-item>
                <v-list-item-icon>
                  <v-icon>mdi-arrow-down-bold-box-outline</v-icon>
                </v-list-item-icon>
                <v-list-item-content>
                  <v-list-item-title>Import</v-list-item-title>
                </v-list-item-content>
              </v-list-item>

              <v-list-item>
                <v-list-item-icon>
                  <v-icon>mdi-arrow-up-bold-box-outline</v-icon>
                </v-list-item-icon>
                <v-list-item-content>
                  <v-list-item-title>Export</v-list-item-title>
                </v-list-item-content>
              </v-list-item>

               <v-list-item @click="launchModal">
                <v-list-item-icon>
                  <v-icon>mdi-wrench</v-icon>
                </v-list-item-icon>
                <v-list-item-content>
                  <v-list-item-title>Settings</v-list-item-title>
                </v-list-item-content>
              </v-list-item>

              <v-list-item @click="toggleTable('users')">
                <v-list-item-icon>
                  <v-icon>mdi-account</v-icon>
                </v-list-item-icon>
                <v-list-item-content>
                  <v-list-item-title >Users</v-list-item-title>
                </v-list-item-content>
              </v-list-item>

              <v-list-item @click="toggleTable('requests')">
                <v-list-item-icon>
                  <v-icon>mdi-clock</v-icon>
                </v-list-item-icon>
                <v-list-item-content>
                  <v-list-item-title >Vacation Requests</v-list-item-title>
                </v-list-item-content>
              </v-list-item>

            </v-list-item-group>
          </v-list>
        </v-card>
        <application-settings-modal :active="activateModal" @closeModal="closeModal"/>
      </v-col>
      <v-col cols="8">
        <TableSelect v-if="activeTable === 'requests'" />
        <UserTable v-if="activeTable === 'users'" />
      </v-col>
    </v-row>
  </v-main>
</template>

<script>
import ApplicationSettingsModal from "@/pages/admin-page/ApplicationSettingsModal";
import TableSelect from "@/pages/admin-page/TableSelect";
import UserTable from "@/pages/admin-page/UserTable";
export default {
  name: "AdminPage",
  components: {
    "application-settings-modal": ApplicationSettingsModal,
    TableSelect,
    UserTable,
  },
  data() {
    return {
      activeTable: 'requests',
      activateModal: null,
    };
  },
  methods: {
    toggleTable(val) {
      this.activeTable = val;
    },
    launchModal(value) {
      this.user = value;
      console.log(this.user);
      this.activateModal = true;
    },
    closeModal() {
      this.user = {};
      this.activateModal = false;
    },
  },
};
</script>

<style scoped>
</style>