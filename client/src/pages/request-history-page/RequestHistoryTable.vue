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
      <v-chip :color="getColor(item.status.status)" dark>{{ item.status.status }}</v-chip>
    </template>
  </v-data-table>
  <view-request-modal :active="activateModal" :request="request"  @closeModal="closeModal"/>
  </v-row>
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
          }
        ],
        activateModal: false,
        request: {}
      }
    },
    created() {
      this.$store.dispatch('retrieveRequestHistory', 1)
    },
    computed: {
      user: {
        get() {
          return this.$store.state.user;
        }
      },
      requests: {
        get() {
          return this.$store.getters.getRequestHistory;
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
        console.log(this.request);
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