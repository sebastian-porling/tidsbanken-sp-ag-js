<template>
<v-col>
      <v-row justify="center" align="center">
      <v-avatar color="indigo" size="60" class="my-4" v-model="profile_pic">
           <span v-if="!user.profile_pic" class="white--text headline">A</span>
          <img v-if="user.profile_pic" :src="user.profile_pic" alt="profilePic">
        </v-avatar>
        <v-h1 class="px-6">
            {{ user.full_name }}
        </v-h1>
  </v-row>
    <v-row justify="center" align="center">
        <v-h3>
            Vacation days: 
            <v-h2 v-model="used_vacation_days">{{ user.used_vacation_days }}</v-h2>
            /
            <v-h2 v-model="vacation_days">{{ user.vacation_days }}</v-h2>
        </v-h3>
    </v-row>
</v-col>

</template>

<script>

export default {
    name: 'UserInfoComponent', 
    computed: {
      loggedIn() {
        return this.$store.getters.loggedIn;
      },
      user: {
        get() {
          return this.$store.getters.getCurrentUser;
        }
      }
    },
}
</script>

<style>

</style>