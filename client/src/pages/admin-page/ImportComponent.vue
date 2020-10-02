<template>
    <v-row justify="center">
        <v-col cols="12" sm="8" md="6">
            <v-card>
                <v-card-title>Import data to the database</v-card-title>
                <v-card-text>
                    <v-file-input
                        v-model="file"
                        show-size
                        accept="application/json"
                        placeholder=".json"
                        label="Enter your import file"
                        style="max-width:300px;"
                    ></v-file-input>
                </v-card-text>
                <v-card-actions>
                    <v-spacer />
                    <v-btn text @click="importData" color="blue">Import</v-btn>
                </v-card-actions>
            </v-card>

            
        </v-col>
    </v-row>
</template>

<script>
export default {
    name: "ImportComponent",
    components: {},
    data() {
        return {
            file: null
        };
    },
    methods: {
        importData() {
            const fr = new FileReader();
            fr.onload = e => {
                this.$store
                    .dispatch("importData", JSON.parse(e.target.result))
                    .then(() => console.log("Success!"))
                    .catch(error => console.log("fail...", error.response));
            };
            fr.readAsText(this.file);
        }
    }
};
</script>

<style></style>
