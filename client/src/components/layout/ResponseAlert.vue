<template>
     <v-snackbar
     v-if="response"
      v-model="snackbar"
      :color="color"
      :multi-line="'multi-line'"
      :top="'top'"
    >
        {{ response || "What?" }}
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
        response: {
            get() {
                return this.$store.getters.getResponse;
            }
        },
        snackbar: {
            get() {
                return this.$store.getters.getIsAlert;
            },
            set() {
                this.$store.dispatch("disableIsAlert");
            }
        },
        typeIsError: {
            get() {
                return this.$store.getters.getTypeIsError;
            }
        },
        color: {
            get() {
                if(this.typeIsError) return 'red'
                else return 'green';
            }
        }
    },
    methods: {
        close() {
            console.log("clicked closed");
            this.$store.dispatch("disableIsAlert");
        }
    }
}
</script>

<style>

</style>