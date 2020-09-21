<template>
  <div>
      <v-navigation-drawer
      v-model="drawer"
      app
    >
      <v-list dense>
        <v-list-item router-link to="/">
          <v-list-item-action>
            <v-icon>mdi-view-dashboard</v-icon>
          </v-list-item-action>
          <v-list-item-content>
            <v-list-item-title>Dashboard</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
        <v-list-item router-link to="history">
          <v-list-item-action>
            <v-icon>mdi-history</v-icon>
          </v-list-item-action>
          <v-list-item-content>
            <v-list-item-title>Request History</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
        <v-list-item router-link to="admin">
          <v-list-item-action>
            <v-icon>mdi-head-minus</v-icon>
          </v-list-item-action>
          <v-list-item-content>
            <v-list-item-title>Admin</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>

    <v-app-bar
      app
      color="indigo"
      dark
    >
      <v-app-bar-nav-icon @click.stop="drawer = !drawer"></v-app-bar-nav-icon>
      <v-toolbar-title>Tidsbanken</v-toolbar-title>
      <v-spacer></v-spacer>
      <header-notifications v-if="loggedIn"/>
      <v-btn v-if="loggedIn" text>
            <v-avatar color="light-blue" size="36">
               <!--  <span class="white--text headline">UU</span> -->
                 <img :src="user.profile_pic" alt="profilePic">
            </v-avatar>
            <strong style="margin-left: 5px"><router-link to="profile" class="white--text" style="text-decoration: none;">{{ user.full_name }}</router-link></strong>
      </v-btn>
      <v-btn v-if="!loggedIn" router-link to="login" outlined> Login </v-btn>
      <v-btn v-if="loggedIn" @click="signOut" outlined> Sign Out </v-btn>
    </v-app-bar>

  </div>
</template>

<script>
//import response from '../../../mock_data/get_user_userid';

import HeaderNotifications from './HeaderNotifications';
export default {
    name: 'Header',
    components: {
        'header-notifications': HeaderNotifications
    },
    data: () => ({
        drawer: null,
        user: null,
    }),
    computed: {
      loggedIn() {
        return this.$store.getters.loggedIn;
      }
    },
    methods: {
      signOut() {
        return this.$store.dispatch('destroyToken')
        .then(() => {
           this.$router.push('/login')
         })
      }
    }
}
</script>

<style>

</style>