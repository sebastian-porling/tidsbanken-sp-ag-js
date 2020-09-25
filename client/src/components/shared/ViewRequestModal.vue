<template>
    <v-dialog v-model="active" persistent width="600px">
        <div v-if="request">
            <view-request-info :request="request" v-show='!this.editMode' @closeModal='closeModal' @changeMode='changeMode'/>
            <view-request-edit-form v-show='this.editMode' :request="request" @closeModal='closeModal' @changeMode='changeMode'/>
        </div>
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
            this.$emit("closeModal");
        },
        openModal() {
            this.dialog = true;
        },
        changeMode() {
            this.editMode = !this.editMode;
        }
    },
    props: [
        'active',
        'request'
    ]
};
</script>

<style></style>
