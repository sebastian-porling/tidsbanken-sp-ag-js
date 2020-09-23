<template>
<v-col>
      <v-row justify="center" align="center">
      <v-avatar color="indigo" size="60" class="my-4" v-model="user.profile_pic">
           <span v-if="!user.profile_pic" class="white--text headline">A</span>
          <img v-if="user.profile_pic" :src="user.profile_pic" alt="profilePic">
        </v-avatar>
        <h1 class="px-6">
            {{ user.full_name }}
        </h1>
  </v-row>
    <v-row justify="center" align="center">
        <h3>
            Vacation days: 
            <h2>{{ user.used_vacation_days }}</h2>
            /
            <h2>{{ user.vacation_days }}</h2>
        </h3>
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
h3, h2 {
  display: inline;
}
</style>