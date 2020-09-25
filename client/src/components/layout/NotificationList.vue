<template>
    <v-list>
        <v-list-item v-for="notification in notifications" :key="notification.id">
            <v-list-item-text>{{notification.message}}</v-list-item-text>
            <v-list-item-action>
                <v-btn color="red" fab x-small dark @click="deleteNotification(notification.id)">
                    <v-icon>mdi-delete-forever</v-icon>
                </v-btn>
            </v-list-item-action>
        </v-list-item>
    </v-list>
</template>

<script>
export default {
    name: "NotificationList",
    props: ['notifications'],
    methods: {
        deleteNotification(notificationId) {
            this.$store.dispatch('deleteNotification', notificationId);
        }
    },
};
</script>

<style></style>
