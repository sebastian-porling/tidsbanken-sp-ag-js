<template>
    <v-container fill-height fluid>
        <v-row align="center" justify="center">
            <v-col style="text-align: center;">
                <h1>404 Page Not Found 😭</h1>
                <br />
                <v-btn router-link :to="'/'" text color="blue darken-3">Go to main page</v-btn>
            </v-col>
        </v-row>
    </v-container>
</template>

<script>
export default {
    name: "NotFound"
};
</script>

<style></style>
