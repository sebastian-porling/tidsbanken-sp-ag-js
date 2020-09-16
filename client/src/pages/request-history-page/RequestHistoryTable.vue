<template>
  <v-row justify="center">
      <v-data-table
    :headers="headers"
    :items="requests"
    :items-per-page="5"
    :sort-by="['created_at']"
    :sort-desc="[true]"
    class="elevation-1"
  >
  <template v-slot:item.status="{ item }">
      <v-chip :color="getColor(item.status)" dark>{{ item.status }}</v-chip>
    </template>
  </v-data-table>
  </v-row>
</template>

<script>
  export default {
    name: 'RequestHistoryTable',
    data () {
      return {
        headers: [
          {
            text: 'Title',
            align: 'start',
            sortable: false,
            value: 'title',
          },
          { text: 'Start', value: 'start_period'},
          { text: 'End', value: 'end_period' },
          { text: 'Status', value: 'status' },
          { 
            text: 'Created', 
            value: 'created_at'
          },
        ],
        requests: [
          {
            title: 'Weekend to France',
            start_period: '2020-10-10',
            end_period: '2020-10-23',
            status: 'Pending',
            created_at: '2020-09-01'
          },
          {
            title: 'Christmas Vacay',
            start_period: '2020-10-10',
            end_period: '2020-10-23',
            status: 'Pending',
            created_at: '2020-09-03'
          },
          {
            title: 'New Years in Thailand',
            start_period: '2020-10-10',
            end_period: '2020-10-23',
            status: 'Pending',
            created_at: '2020-05-01'
          },
          {
            title: 'Autumn Break',
            start_period: '2020-10-10',
            end_period: '2019-10-23',
            status: 'Pending',
            created_at: '2019-11-01'
          },
          {
            title: 'Cottage vacation with family',
            start_period: '2020-10-10',
            end_period: '2020-10-23',
            status: 'Approved',
            created_at: '2020-01-01'
          },
          {
            title: 'Two Months Backpacking in China',
            start_period: '2020-10-10',
            end_period: '2020-10-23',
            status: 'Denied',
            created_at: '2020-05-01'
          },
          {
            title: 'Winter break in Banff, Canada',
            start_period: '2020-10-10',
            end_period: '2020-10-23',
            status: 'Approved',
            created_at: '2020-06-14'
          },
          {
            title: 'Long Weekend',
            start_period: '2020-10-10',
            end_period: '2020-10-23',
            status: 'Pending',
            created_at: '2020-03-26'
          },
          {
            title: 'Visit family in Germany',
            start_period: '2020-10-10',
            end_period: '2020-10-23',
            status: 'Approved',
            created_at: '2020-04-12'
          },
          {
            title: 'I just need a break!',
            start_period: '2020-10-10',
            end_period: '2020-10-23',
            status: 'Pending',
            created_at: '2020-04-13'
          },
        ],
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
    },
  }
</script>

<style>

</style>