<template>
  <v-container >
        <v-file-input
            v-model="file"
            show-size
            accept="application/json"
            placeholder=".json"
            label="Enter your import file"
            style="max-width:300px;"
        ></v-file-input>
        <v-btn text @click="importData" color="blue">Import</v-btn>
  </v-container>
</template>

<script>

export default {
    name: "ImportComponent",
    components: {
    },
    data() {
        return {
            file: null
        }
    },
    methods: {
        importData() {
            const fr = new FileReader();
            fr.onload = e => {
                this.$store.dispatch('importData', JSON.parse(e.target.result))
                .then(() => console.log("Success!"))
                .catch(error => console.log("fail...", error.response));
            }
            fr.readAsText(this.file);
        }
    },
}
</script>

<style>

</style>