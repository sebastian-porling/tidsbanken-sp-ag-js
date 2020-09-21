<template>
 <v-main>
        <v-row align="center" justify="space-around">
    <v-col class="text-center" cols="10" sm="7">
        <div class="imp-ex">
        <v-btn color="grey" medium>Import</v-btn>
        &nbsp;&nbsp;&nbsp;
        <v-btn color="grey" medium>Export</v-btn>
        </div>
        <br>
       <div class="redirect">
            <v-btn @click="$router.push('requests')">View Requests</v-btn>
            &nbsp;&nbsp;&nbsp;
              <v-btn @click="$router.push('users')">View Users</v-btn>
              </div>
              <br>
              <div class="modals">
            <application-settings-modal/>
            </div>
    </v-col>
  </v-row>
    </v-main>
    
</template>

<script>
import ApplicationSettingsModal from '@/pages/admin-page/ApplicationSettingsModal'
export default {
    name: 'AdminPage',
  components: {
          'application-settings-modal': ApplicationSettingsModal,

  }

}
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