<template>
    <v-container fluid style="display: flex; flex-flow: column; height: 100%;">
        <v-row align="center" justify="space-around" class="header">
            <create-ip-modal v-if="isAdmin" />
            <create-request-modal @openRequestModal="openRequestModal" />
            <view-request-modal
                :active="requestModal"
                :request="request"
                @closeModal="closeRequestModal"
            />
        </v-row>
        <dashboard-calendar style="flex: 1 1 auto;" />
    </v-container>
</template>

<script>
import CreateIPModal from "./CreateIPModal";
import CreateRequestModal from "./CreateRequestModal";
import ViewRequestModal from "@/components/shared/ViewRequestModal";
import DashboardCalendar from "./DashboardCalendar";
export default {
    name: "DashboardPage",
    components: {
        "dashboard-calendar": DashboardCalendar,
        "create-ip-modal": CreateIPModal,
        "create-request-modal": CreateRequestModal,
        "view-request-modal": ViewRequestModal
    },
    data() {
        return {
            ipModal: false,
            requestModal: false,
            request: null
        };
    },
    /**
     * Fetches if the current user is admin
     */
    computed: {
        isAdmin() {
            return this.$store.getters.isAdmin;
        }
    },
    methods: {
        closeIPModal() {
            this.ipModal = false;
        },
        closeRequestModal() {
            this.requestModal = false;
        },
        openIPModal() {
            this.ipModal = true;
        },
        openRequestModal(request) {
            setTimeout(() => {
                this.request = request;
                this.requestModal = true;
            }, 500);
        }
    }
};
</script>

<style>
.header {
    margin-top: 100px;
}
</style>
