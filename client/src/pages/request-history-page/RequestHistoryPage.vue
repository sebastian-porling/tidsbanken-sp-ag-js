<template>
<v-col class="container">
  <br>
  <br>
  <v-row>
    <UserInfoComponent />
  </v-row>
  <br>
  <v-row>
    <RequestHistoryTable />
  </v-row>
</v-col>
</template>

<script>
import UserInfoComponent from './UserInfoComponent';
import RequestHistoryTable from './RequestHistoryTable';

export default {
  name: 'RequestHistoryPage',
  components: {
    UserInfoComponent,
    RequestHistoryTable
  }
}
</script>

<style>

</style>