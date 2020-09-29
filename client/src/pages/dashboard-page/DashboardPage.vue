<template>
    <v-main>
        <v-container fluid>
        <v-row align="center" justify="space-around" class="header">
            <create-ip-modal v-if="isAdmin" />
            <create-request-modal @openRequestModal="openRequestModal"/>
            <view-request-modal :active="requestModal" :request="request" @closeModal="closeRequestModal"/>
        </v-row>
        <dashboard-calendar />
        </v-container>
        
    </v-main>
</template>

<script>
import CreateIPModal from './CreateIPModal';
import CreateRequestModal from './CreateRequestModal';
import ViewRequestModal from '@/components/shared/ViewRequestModal';
import DashboardCalendar from './DashboardCalendar';
export default {
    name: 'DashboardPage',
    components: {
        'dashboard-calendar': DashboardCalendar,
        'create-ip-modal': CreateIPModal,
        'create-request-modal': CreateRequestModal,
        'view-request-modal': ViewRequestModal,
    },
    data() {
        return {
            ipModal: false,
            requestModal: false,
            request: null
        }
    },
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
            setTimeout(() => {
                this.request = null;
            }, 500);
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
    },
};
</script>

<style>
.header{
    margin-top: 30px;
}
</style>
