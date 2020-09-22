<template>
 <v-main>
        <v-row align="center" justify="space-around">
    <v-col class="text-center" cols="10" sm="12">
        <div class="imp-ex">
        <v-btn color="grey" medium>Import</v-btn>
        &nbsp;&nbsp;&nbsp;
        <v-btn color="grey" medium>Export</v-btn>
        </div>
         <div class="modals">
            <application-settings-modal/>
            </div>
             <div class="redirect">
           <v-btn @click="toggleTable('requests')">View Requests</v-btn>
                &nbsp;&nbsp;&nbsp;
           <v-btn @click="toggleTable('users')">View Users</v-btn>
       <TableSelect v-if="activeTable === 'requests'"/>
       <UserTable v-if="activeTable === 'users'"/>
             </div>
    </v-col>
  
  </v-row>
    </v-main>
    
</template>

<script>
import ApplicationSettingsModal from '@/pages/admin-page/ApplicationSettingsModal'
import TableSelect from '@/pages/admin-page/TableSelect'
import UserTable from '@/pages/admin-page/UserTable'
export default {
    name: 'AdminPage',
  components: {
          'application-settings-modal': ApplicationSettingsModal,
          TableSelect,
          UserTable
    
  },
       data() {
    return {
        activeTable: null,
    }
  },
  methods: {
      toggleTable(val) {
          this.activeTable = val;
      }
  },
};
</script>

<style>
.imp-ex{
    padding: 2%;
}
.modals{
    padding:2%;
    padding-bottom: 4%;
}
.redirect{
    padding: 2%;
}
</style>