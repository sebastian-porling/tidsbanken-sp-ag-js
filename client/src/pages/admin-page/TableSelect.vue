<template>
  <v-row justify="center">
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
      <v-chip :color="getColor(item.status)" dark>{{ item.status }}</v-chip>
    </template>
  </v-data-table>
  <view-request-modal :active="activateModal" :request="request" @closeModal="closeModal"/>
  </v-row>
</template>

<script>
import response from '../../../mock_data/get_request'
 import ViewRequestModal from '../../components/shared/ViewRequestModal'
    export default {
    name: 'TableSelect',
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
          { text: 'Start', value: 'period_start'},
          { text: 'End', value: 'period_end' },
          { text: 'Status', value: 'status' }
        ],
        requests: response.data,
        activateModal: false,
        request: 0
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
        console.log("Request with id " + value.request_id + " is clicked")
        this.request = value.request_id;
        this.activateModal = true;
      },
      closeModal() {
        this.request = 0;
        this.activateModal = false;
      }
    },
  }
</script>

<style>
</style>