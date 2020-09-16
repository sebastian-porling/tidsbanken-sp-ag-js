<template>
    <v-dialog v-model="dialog" width="600px">
        <template v-slot:activator="{ on, attrs }">
            <v-btn v-bind="attrs" v-on="on">
                View Request
            </v-btn>
        </template>
        <view-request-info v-show='!this.editMode' @closeModal='closeModal' @changeMode='changeMode'/>
        <view-request-edit-form v-show='this.editMode' @closeModal='closeModal' @changeMode='changeMode'/>
    </v-dialog>
</template>

<script>
import ViewRequestInfo from './ViewRequestInfo';
import ViewRequestEditForm from './ViewRequestEditForm';
export default {
    name: "ViewRequestModal",
    components: {
        'view-request-info': ViewRequestInfo,
        'view-request-edit-form': ViewRequestEditForm,

    },
    data() {
        return {
            dialog: false,
            editMode: false,
        };
    },
    methods: {
        closeModal() {
            this.dialog = false;
        },
        changeMode() {
            this.editMode = !this.editMode;
        }
    },
};
</script>

<style></style>
