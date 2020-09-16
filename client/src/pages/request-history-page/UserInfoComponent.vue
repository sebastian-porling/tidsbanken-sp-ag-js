<template>
<v-col>
      <v-row justify="center" align="center">
      <v-avatar color="indigo" size="60" class="my-4" v-model="profile_pic">
          <span class="white--text headline">A</span>
        </v-avatar>
        <v-h1 class="px-6">
            User Usersson
        </v-h1>
  </v-row>
    <v-row justify="center" align="center">
        <v-h3>
            Vacation days: 
            <v-h2 v-model="used_vacation_days">5</v-h2>
            /
            <v-h2 v-model="vacation_days">25</v-h2>
        </v-h3>
    </v-row>
</v-col>

</template>

<script>
export default {
    name: 'UserInfoComponent'
}
</script>

<style>

</style>