<template>
  <v-dialog v-model="active" persistent max-width="500px" justify="center" align="center">
    <v-card class="qr">
        <v-card-title>Generate new Two Factor Authentication!</v-card-title>
        <v-card-text v-if="qrCode && uri" justify="center" align="center">
        Remember to scan it to your authentication app!
        <img :src="qrCode" alt="QR-code for multi authentication" />
        <div v-if="isMobile" style="padding-bottom: 10px;">
            or click..
            <v-btn text :href="uri" color="deep-purple lighten-2">Add 2FA</v-btn>
        </div>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="red darken-1" text @click="closeModal">Close</v-btn>
        <v-btn color="primary darken-1" text @click="generate">Generate</v-btn
        >
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
export default {
    name: "GenerateTwoFactorModal",
    props: ['active'],
    data() {
        return {
            qrCode: null,
            uri: null
        }
    },
    computed: {
      isMobile: {
        get() {
          return window.innerWidth < 700;
        }
      }
    },
    methods: {
        closeModal() {
            this.$emit('closeModal');
        },
        generate() {
            this.$store.dispatch('generateMultiAuth')
            .then(() => {
                this.qrCode = `data:image/png;base64, ${this.$store.getters.getQrCode}`;
                this.uri = this.$store.getters.getUri;
            })
            .catch(error => console.log(error))
        }
    },

}
</script>

<style>
.qr {
  position: relative;
  text-align: center;
}
.qr img {
  display: block;
  text-align: center;
  width: 100%;
  max-width: 300px;
}
</style>