<template>
    <v-main>
        <v-container fluid>
        <v-row align="center" justify="space-around">
            <create-ip-modal/>
            <delete-ineligible-modal/>
            <create-request-modal/>
            <view-request-modal/>
        </v-row>
        </v-container>
        
        <dashboard-calendar />
    </v-main>
</template>

<script>
import CreateIPModal from './CreateIPModal';
import CreateRequestModal from './CreateRequestModal';
import DashboardCalendar from './DashboardCalendar'
import ViewRequestModal from '@/components/shared/ViewRequestModal'
import DeleteIneligibleModal from './DeleteIneligibleModal'
export default {
    name: 'DashboardPage',
    components: {
        'dashboard-calendar': DashboardCalendar,
        'create-ip-modal': CreateIPModal,
        'create-request-modal': CreateRequestModal,
        'view-request-modal': ViewRequestModal,
        'delete-ineligible-modal': DeleteIneligibleModal
    },
    data() {
        return {
            ipModal: false,
            requestModal: false
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
        openRequestModal() {
            this.requestModal = true;
        }
    },
};
</script>

<style></style>
