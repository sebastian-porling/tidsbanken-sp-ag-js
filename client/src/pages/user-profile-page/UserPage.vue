<template>
<div>
    <UserProfileForm @openTwoFactorModal="openModal"/>
    <GenerateTwoFactorModal :active="displayModal" @closeModal="closeModal" />
</div>
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