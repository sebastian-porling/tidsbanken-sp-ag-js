<template>
     <v-snackbar
      v-model="snackbar"
      :color="'cyan darken-2'"
      :multi-line="'multi-line'"
      :timeout="5000"
      :top="'top'"
    >
      {{ notification.message }}
      <template v-slot:action="{ attrs }">
        <v-btn
          dark
          text
          v-bind="attrs"
          @click="snackbar = false"
        >
          Close
        </v-btn>
      </template>
    </v-snackbar>
</template>

<script>
export default {
    name: 'NotificationAlert',
    data() {
        return {
            notification: null,
            snackbar: false
        }
    },
    created() {
        this.$store.getters.getSocket.on('notification', (notification) => {
            this.notification = notification;
            setTimeout(() => {
              this.snackbar = false;
              this.notification = null;
            }, 5000);
        })
    }
}
</script>

<style>

</style>