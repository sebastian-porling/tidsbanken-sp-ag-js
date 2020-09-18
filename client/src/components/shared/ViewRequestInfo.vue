<template>
    <v-card>
        <v-card-title>
            <span class="headline">{{ request.title }}</span>
                <v-chip class="ma-2" :color="getColor(request.status)" text-color="white">
                    {{ request.status }}
                </v-chip>
        </v-card-title>
        <v-card-text>
            <v-container>
                <v-row>
                    <v-col cols="12">
                        <v-btn text>
                            <v-avatar color="light-blue" size="36">
                                <!-- <span class="white--text headline">UU</span> -->
                                <img :src="request.owner.profile_pic" alt="profilePic">
                            </v-avatar>
                            <strong style="margin-left: 5px">
                                {{ request.owner.name }}</strong
                            >
                        </v-btn>
                    </v-col>
                    <!-- Not implemented period start and end -->
                    <v-col cols="12" sm="6" md="6">
                        <v-text-field
                            type="date"
                            label="Start"
                            v-model="period_start"
                            disabled
                        ></v-text-field>
                    </v-col>
                    <v-col cols="12" sm="6" md="6">
                        <v-text-field
                            type="date"
                            label="End"
                            v-model="period_end"
                            disabled
                        ></v-text-field>
                    </v-col>
                    <v-col cols="12">
                        <view-request-comments :request_id="request_id" />
                        <view-request-comment-form />
                    </v-col>
                </v-row>
            </v-container>
        </v-card-text>
        <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="red darken-1" text @click="closeModal">Cancel</v-btn>
            <v-btn color="primary darken-1" text @click="changeMode">Edit</v-btn>
        </v-card-actions>
    </v-card>
</template>

<script>
import ViewRequestCommentForm from './ViewRequestCommentForm';
import ViewRequestComments from './ViewRequestComments';
import response from '../../../mock_data/get_request_id';

export default {
    name: "ViewRequestInfo",
    components: {
        'view-request-comment-form': ViewRequestCommentForm,
        'view-request-comments': ViewRequestComments
    },
    data () {
        return {
            request: response.data,
            period_start: response.data.period_start,
            period_end: response.data.period_end
        }
    },
    props: [
        'request_id'
    ],
    methods: {
        closeModal() {
            this.$emit("closeModal");
        },
        changeMode() {
            this.$emit("changeMode");
        },
        getColor (status) {
          switch(status) {
            case 'Pending':
                  return 'orange';
            case 'Approved':
                return 'green';
            case 'Denied':
                return 'red';
          }
        }
    }
};
</script>

<style></style>
