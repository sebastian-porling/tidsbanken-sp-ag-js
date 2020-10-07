<template>
<v-container>
    <v-sheet
      v-if="isLoading"
      class="pa-10"
    >
      <v-skeleton-loader
        v-bind="attrs"
        class="mx-auto"
        min-width="600"
        max-height="800"
        type="image, table-tfoot"
      ></v-skeleton-loader>
    </v-sheet>

    <transition name="fade">
      <v-row v-if="!isLoading">
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
        </transition>
  </v-container>
</template>

<script>
import ViewRequestModal from "../../components/shared/ViewRequestModal";
export default {
    name: "RequestHistoryTable",
    components: {
        "view-request-modal": ViewRequestModal
    },
      inject: {
    theme: {
      default: { isDark: false },
    },
  },
    props: [ 'user', 'userId' ],
    data() {
        return {
             isLoading: true,
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
    created() {
        this.$store.dispatch(
            "retrieveRequestHistory", this.user.id,
            setTimeout(() => (this.isLoading = false), 500)
        );
    },
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
