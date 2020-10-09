<template>
     <v-snackbar
     v-if="response"
      v-model="snackbar"
      :color="color"
      :multi-line="'multi-line'"
        :timeout="5000"
      :top="'top'"
    >
        {{ response }}
      <template v-slot:action="{ attrs }">
        <v-btn
          dark
          text
          v-bind="attrs"
          @click="close"
        >
          Close
        </v-btn>
      </template>
    </v-snackbar>
</template>

<script>
export default {
    name: 'ResponseAlert',
    data() {
        return {
            isAlert: null
        }
    },
    computed: {
        /**
         * Fetches response
         */
        response: {
            get() {
                return this.$store.getters.getResponse;
            }
        },
        /**
         * Sets snackbar to true or false depending on value of isAlert
         */
        snackbar: {
            get() {
                return this.$store.getters.getIsAlert;
            },
            set() {
                this.$store.dispatch("disableIsAlert");
            }
        },
        /**
         * Sets type to error if message/alert is an error message
         */
        typeIsError: {
            get() {
                return this.$store.getters.getTypeIsError;
            }
        },
        /**
         * Sets color depending on error message or not
         */
        color: {
            get() {
                if(this.typeIsError) return 'red'
                else return 'green';
            }
        }
    },
    methods: {
        close() {
            this.$store.dispatch("disableIsAlert");
        }
    }
}
</script>

<style>

</style>