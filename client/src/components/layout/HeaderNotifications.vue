<template>
    <div class="text-center">
        <v-menu
            v-model="menu"
            :close-on-content-click="false"
            :nudge-width="200"
            offset-x
        >
            <template v-slot:activator="{ on, attrs }">
                <v-btn icon v-bind="attrs" v-on="on">
                    <v-badge color="red" content="6">
                        <v-icon>mdi-bell</v-icon>
                    </v-badge>
                </v-btn>
            </template>

            <v-card>
                <v-list>
                    <v-list-item>
                        <v-list-item-title>Notifications</v-list-item-title>
                        <v-badge color="red" content="6" left bottom>
                            <v-icon>mdi-bell</v-icon>
                        </v-badge>
                    </v-list-item>
                </v-list>

                <v-divider></v-divider>

                <notification-list />

                <v-card-actions>
                    <v-spacer></v-spacer>

                    <v-btn text color="red" @click="menu = false">Clear</v-btn>
                    <v-btn color="primary" text @click="menu = false">Ok</v-btn>
                </v-card-actions>
            </v-card>
        </v-menu>
    </div>
</template>

<script>
import NotificationList from './NotificationList';
export default {
    name: "HeaderNotificationList",
    components: {
        'notification-list': NotificationList
    },
    data: () => ({
        menu: false,
    })
};
</script>

<style></style>
