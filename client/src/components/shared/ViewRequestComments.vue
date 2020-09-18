<template>
    <v-list style="max-height: 225px" class="overflow-y-auto">
        <v-list-item v-for="comment in data" :key="comment.comment_id">
            <v-list-item-avatar>
                <v-avatar color="light-blue" size="36">
                    <img v-if="comment.user.profile_pic" :src="comment.user.profile_pic" :alt="comment.user.name | initials">
                    <span v-else class="white--text headline">{{comment.user.name | initials}}</span>
                </v-avatar>
            </v-list-item-avatar>
            <v-list-item-content>
                <v-list-item-title>{{comment.user.name}}</v-list-item-title>
                <v-list-item-subtitle>
                    <p>{{comment.modified_at}}</p>
                    <p style="color: black;">{{comment.message}}</p>
                </v-list-item-subtitle>
            </v-list-item-content>
        </v-list-item>
    </v-list>
</template>

<script>
import response from '../../../mock_data/get_request_id_comments';
export default {
    data() {
        return {
            data: response.data
        }
    },
    props: [
        'request_id'
    ],
    filters: {
        initials: (data) => {
            if (!data) return 'UU';
            data = data.toString();
            data = data.split(' ');
            if(data.length < 2) return 'UU';
            return  data[0].charAt(0).toUpperCase() + 
                    data[1].charAt(0).toUpperCase();
        }
    }
};
</script>

<style></style>
