<template>
<v-row class="container">
    <UserInfoComponent />
  <RequestHistoryTable />
</v-row>
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