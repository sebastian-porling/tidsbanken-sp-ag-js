<template>
    <UserProfileForm :user="user" />
</template>

<script>
import UserProfileForm from './UserProfileForm';
import response from '../../../mock_data/get_user_userid';

export default {
    name: 'UserProfilePage',
    components: {
        UserProfileForm
    },
    data () {
        return {
            user: response,
        }
    }
}
</script>