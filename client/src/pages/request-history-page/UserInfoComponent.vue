<template>
  <v-col>
    <v-row justify="center" align="center">
      <v-avatar color="indigo" size="60" class="my-4" v-model="user.profile_pic">
        <span v-if="!user.profile_pic" class="white--text headline">
          {{ user.full_name | initials }}
        </span>
        <img v-if="user.profile_pic" :src="user.profile_pic" alt="profilePic" />
      </v-avatar>
      <h1 class="px-6">{{ user.full_name }}</h1>
    </v-row>
    <v-row justify="center" align="center">
      <h3>
        Vacation days:
        <h2>{{ user.used_vacation_days }}</h2>/
        <h2>{{ user.vacation_days }}</h2>
      </h3>
    </v-row>
  </v-col>
</template>

<script>
export default {
  name: "UserInfoComponent",
  props: ["user", "userId"],
  /**
   * Fetches the current user
   */
  computed: {
    currentUser() {
      return this.$store.getters.getCurrentUser;
    },
  },
    /**
   * Generates a profile image placeholder if no name exists
   * or if name is less than 2 characters
   */
  filters: {
      initials: (data) => {
            if (!data) return '😁';
            data = data.toString();
            data = data.split(' ');
            if(data.length < 2) return '😁';
            return  data[0].charAt(0).toUpperCase() + 
                    data[1].charAt(0).toUpperCase();
        },
    }
};
</script>

<style>
h3,
h2 {
  display: inline;
}
</style>