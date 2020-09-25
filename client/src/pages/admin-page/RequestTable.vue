<template>
<v-container>
  <v-row>
    <h1>Vacations request from all users</h1>
  </v-row>
  <v-row>
    
      <v-data-table
    :headers="headers"
    :items="requests"
    :items-per-page="10"
    :sort-by="['created_at']"
    :sort-desc="[true]"
    class="elevation-1"
    @click:row="launchModal"
  >
  <template v-slot:item.status="{ item }">
      <v-chip :color="getColor(item.status.status)" dark>{{ item.status.status }}</v-chip>
    </template>
  </v-data-table>
  <view-request-modal :active="activateModal" :request="request"  @closeModal="closeModal"/>
  </v-row>
</v-container>
</template>

<script>
 import ViewRequestModal from '../../components/shared/ViewRequestModal'
    export default {
    name: 'RequestHistoryTable',
    components: {
       'view-request-modal': ViewRequestModal
     },
    data () {
      return {
        headers: [
          {
            text: 'Title',
            align: 'start',
            sortable: false,
            value: 'title',
          },
          { text: 'Start', value: 'start'},
          { text: 'End', value: 'end' },
          { text: 'Status', value: 'status' },
          { 
            text: 'Created', 
            value: 'created_at'
          },
          { text: 'Owner', value: 'owner.full_name' },
        ],
        activateModal: false,
        request: {}
      }
    },
    created() {
      this.$store.dispatch('retrieveAllRequests')
    },
    computed: {
      user: {
        get() {
          return this.$store.getters.getCurrentUser;
        }
      },
      requests: {
        get() {
          return this.$store.getters.getAllRequests;
        }
      }
    },
    methods: {
      getColor (status) {
          switch(status) {
            case 'Pending':
                  return 'orange';
            case 'Approved':
                return 'green';
            case 'Denied':
                return 'red';
          }
      },
      launchModal(value) {
        this.request = value; 
        this.$store.dispatch('retrieveComments', value.id)
        this.activateModal = true;
      },
      closeModal() {
        this.request = {};
        this.activateModal = false;
      }
    },
  }
</script>

<style>

</style>