<template>
    <v-row justify="center">
        <v-data-table
            :headers="headers"
            :items="requests"
            :items-per-page="10"
            :sort-by="['created_at']"
            :sort-desc="[true]"
            class="elevation-1"
            @click:row="launchModal"
        >
            <template v-slot:item.status="{ item }">
                <v-chip :color="getColor(item.status.status)" dark>{{
                    item.status.status
                }}</v-chip>
            </template>
        </v-data-table>
        <view-request-modal
            :active="activateModal"
            :request="request"
            @closeModal="closeModal"
        />
    </v-row>
</template>

<script>
import ViewRequestModal from "../../components/shared/ViewRequestModal";
export default {
    name: "RequestHistoryTable",
    components: {
        "view-request-modal": ViewRequestModal
    },
    props: [ 'user', 'userId' ],
    data() {
        return {
            headers: [
                {
                    text: "Title",
                    align: "start",
                    sortable: false,
                    value: "title"
                },
                { text: "Start", value: "start" },
                { text: "End", value: "end" },
                { text: "Status", value: "status" },
                {
                    text: "Created",
                    value: "created_at"
                }
            ],
            activateModal: false,
            request: {}
        };
    },
    /**
     * Instansiates and saves request history for a specific user to state
     * @param {Number} id
     */
    created() {
        this.$store.dispatch(
            "retrieveRequestHistory", this.userId
        );
    },
    /**
     * Fetches the request history
     */
    computed: {
        requests: {
            get() {
                return this.$store.getters.getRequestHistory;
            },
            set() {
                return this.$store.getters.getRequestHistory;
            }
        }
    },
    methods: {
        /**
         * Sets the colors for the different statuses a 
         * request can have
         */
        getColor(status) {
            switch (status) {
                case "Pending":
                    return "orange";
                case "Approved":
                    return "green";
                case "Denied":
                    return "red";
            }
        },
        launchModal(value) {
            this.request = value;
            this.$store.dispatch("retrieveComments", value.id);
            this.activateModal = true;
        },
        closeModal() {
            this.activateModal = false;
            this.request = {};
        }
    }
};
</script>

<style></style>
