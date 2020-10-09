<template>
     <v-snackbar
     v-if="notification"
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
    computed: {
      socket: {
        get() {
          return this.$store.getters.getSocket;
        }
      }
    },
    watch: {
      socket() {
        /*let socket = this.$store.getters.getSocket;
        if (socket !== null) {
          this.$store.getters.getSocket.on('notification', (notification) => {
            this.notification = notification;
            this.snackbar = true;
        })
        }*/
      }
    },
}
</script>

<style>

</style>