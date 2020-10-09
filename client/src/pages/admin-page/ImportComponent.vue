<template>
    <v-row justify="center">
        <v-col cols="12" sm="8" md="6">
            <v-card>
                <v-card-title>Import data to the database</v-card-title>
                <v-card-text>
                    <v-form v-model="valid">
                    <v-file-input
                        v-model="file"
                        show-size
                        accept="application/json"
                        placeholder=".json"
                        label="Enter your import file"
                        style="max-width:300px;"
                        :rules="importRules"
                    ></v-file-input>
                    </v-form>
                </v-card-text>
                <v-card-actions>
                    <v-spacer />
                    <v-progress-circular v-if="isLoading" indeterminate color="blue"></v-progress-circular>
                    <v-btn text @click="importData" color="blue" :disabled="!valid">Import</v-btn>
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
            file: null,
            isLoading: false,
            valid: true,
            importRules: [
                (v) => !!v || "You have to choose a file"
            ]
        };
    },
    methods: {
        importData() {
            this.isLoading = true;
            const fr = new FileReader();
            fr.onload = e => {
                this.$store
                    .dispatch("importData", JSON.parse(e.target.result))
                    .then(() => {
                        this.isLoading = false;
                        this.file = null;
                        console.log("Success!")
                        })
                    .catch(error => {
                        this.isLoading = false;
                        this.file = null;
                        console.log("fail...", error.response)
                        });
            };
            fr.readAsText(this.file);
        }
    }
};
</script>

<style></style>
