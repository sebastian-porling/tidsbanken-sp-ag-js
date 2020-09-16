<template>
  <div>
      <v-navigation-drawer
      v-model="drawer"
      app
    >
      <v-list dense>
        <v-list-item link>
          <v-list-item-action>
            <v-icon>mdi-view-dashboard</v-icon>
          </v-list-item-action>
          <v-list-item-content>
            <v-list-item-title>Dashboard</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
        <v-list-item link>
          <v-list-item-action>
            <v-icon>mdi-history</v-icon>
          </v-list-item-action>
          <v-list-item-content>
            <v-list-item-title>Request History</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
        <v-list-item link>
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
      <header-notifications />
      <v-btn text>
            <v-avatar color="light-blue" size="36">
                <span class="white--text headline">UU</span>
            </v-avatar>
            <strong style="margin-left: 5px"> User Usersson</strong>
      </v-btn>
      <v-btn outlined>
          Sign out
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