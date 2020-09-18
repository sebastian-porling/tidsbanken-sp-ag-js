<template>
  <v-row justify="center">
       <div class="modals">
  <create-user-modal/>
  &nbsp;&nbsp;&nbsp;
            <edit-user-modal/>
            </div>
            
      <v-data-table
    :headers="headers"
    :items="requests"
    :items-per-page="10"
    :sort-by="['created_at']"
    :sort-desc="[true]"
    class="elevation-1"
  >
  </v-data-table>
  
  </v-row>
</template>

<script>
import response from '../../../mock_data/get_all_users'
import CreateUserModal from '@/pages/admin-page/CreateUserModal'
import EditUserModal from '@/pages/admin-page/EditUserModal'
    export default {
    name: 'UserTable',
    components: {
     'create-user-modal': CreateUserModal,
          'edit-user-modal': EditUserModal,
  },
    data () {
      return {
        headers: [
          {
            align: 'start',
            sortable: false,
            value: 'title',
          },
          {text: 'Name', value: 'full_name'},
          {text: 'Email', value: 'email'},
          { text: 'Created at', value: 'created_at'},
          { text: 'Modified at', value: 'modified_at' }
        ],
        requests: response.data,
        request: 0
      }
    },
    }
     
</script>

<style>
.modals{
    padding:2%;
    padding-bottom: 4%;
}
</style>