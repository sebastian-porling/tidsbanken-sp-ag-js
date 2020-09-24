<template>
    <v-dialog v-model="active" persistent width="600px">
        <edit-user-modal :user="user" v-show='!this.editPasswordMode' @closeModal='closeModal' @changeMode='changeMode'/>
        <change-password-modal v-show='this.editPasswordMode' :user="user" @closeModal='closeModal' @changeMode='changeMode'/>
    </v-dialog>
</template>

<script>
import EditUserModal from './EditUserModal';
import ChangePasswordModal from './ChangePasswordModal';
export default {
    name: "ViewRequestModal",
    components: {
        'edit-user-modal': EditUserModal,
        'change-password-modal': ChangePasswordModal,
    },
    data() {
        return {
            dialog: false,
            editPasswordMode: false,
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
            this.editPasswordMode = !this.editPasswordMode;
        }
    },
    props: [
        'active',
        'user'
    ]
};
</script>

<style></style>
