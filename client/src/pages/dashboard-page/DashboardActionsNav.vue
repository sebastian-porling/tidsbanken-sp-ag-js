<template>
    <v-container fluid>
        <v-row align="center" justify="space-around">
            <v-btn @click='this.openRequestModal'><v-icon>mdi-plus</v-icon> Request Vacation</v-btn>
            <v-btn @click='this.openIPModal'><v-icon>mdi-plus</v-icon> Ineligible Period</v-btn>
        </v-row>
    </v-container>
</template>

<script>
export default {
    name: 'DashboardActionsNav',
    methods: {
        openRequestModal() {
            this.$emit('openRequestModal');
        },
        openIPModal() {
            this.$emit('openIPModal');
        }
    },
};
</script>

<style></style>
