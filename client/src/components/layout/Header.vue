<template>
  <div>
      <v-navigation-drawer
      v-model="drawer"
      app
    >
      <v-list dense>
        <v-list-item router-link to="dashboard">
          <v-list-item-action>
            <v-icon>mdi-view-dashboard</v-icon>
          </v-list-item-action>
          <v-list-item-content>
            <v-list-item-title><router-link to="/" style="text-decoration: none;">Dashboard</router-link></v-list-item-title>
          </v-list-item-content>
        </v-list-item>
        <v-list-item link>
          <v-list-item-action>
            <v-icon>mdi-history</v-icon>
          </v-list-item-action>
          <v-list-item-content>
            <v-list-item-title><router-link to="history" style="text-decoration: none;">Request History</router-link></v-list-item-title>
          </v-list-item-content>
        </v-list-item>
        <v-list-item link>
          <v-list-item-action>
            <v-icon>mdi-head-minus</v-icon>
          </v-list-item-action>
          <v-list-item-content>
            <v-list-item-title><router-link to="admin" style="text-decoration: none;">Admin</router-link></v-list-item-title>
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
      <header-notifications />
      <v-btn text>
            <v-avatar color="light-blue" size="36">
                <span class="white--text headline">UU</span>
            </v-avatar>
            <strong style="margin-left: 5px"><router-link to="profile" class="white--text" style="text-decoration: none;">User Usersson</router-link></strong>
      </v-btn>
      <v-btn outlined>
          <router-link to="login" class="white--text" style="text-decoration: none;">Sign out</router-link>
      </v-btn>
    </v-app-bar>

  </div>
</template>

<script>
import HeaderNotifications from './HeaderNotifications';
export default {
    name: 'Header',
    components: {
        'header-notifications': HeaderNotifications
    },
    data: () => ({
        drawer: null,
    }),
}
</script>

<style>

</style>