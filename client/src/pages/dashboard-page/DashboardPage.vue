<template>
    <v-main>
        <v-container fluid>
        <v-row align="center" justify="space-around">
            <create-ip-modal/>
            <ineligible-modal/>
            <create-request-modal/>
            <view-request-modal/>
        </v-row>
        </v-container>
        
        <dashboard-calander />
    </v-main>
</template>

<script>
import CreateIPModal from './CreateIPModal';
import CreateRequestModal from './CreateRequestModal';
import DashboardCalander from './DashboardCalander'
import ViewRequestModal from '@/components/shared/ViewRequestModal'
import IneligibleModal from './IneligibleModal'
export default {
    name: 'DashboardPage',
    components: {
        'dashboard-calander': DashboardCalander,
        'create-ip-modal': CreateIPModal,
        'create-request-modal': CreateRequestModal,
        'view-request-modal': ViewRequestModal,
        'ineligible-modal': IneligibleModal
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
