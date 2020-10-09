<template>
<v-container class="user-profile">
    <UserProfileForm @openTwoFactorModal="openModal"/>
    <GenerateTwoFactorModal :active="displayModal" @closeModal="closeModal" />
</v-container>
</template>

<script>
import UserProfileForm from './UserProfileForm';
import GenerateTwoFactorModal from './GenerateTwoFactorModal'

export default {
    name: 'UserPage',
    components: {
        UserProfileForm,
        GenerateTwoFactorModal
    },
    data() {
        return {
            displayModal: false,
        }
    },
    methods: {
        openModal() {
            this.displayModal = true;
        },
        closeModal() {
            this.displayModal = false;
        }
    },
}
</script>

<style scoped>
.user-profile{
    display: flex;
    margin-top: 100px;
    justify-content: center;
}
</style>