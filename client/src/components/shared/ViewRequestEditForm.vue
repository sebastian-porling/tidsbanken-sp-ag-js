<template>
  <v-card>
        <v-card-title>
            <span class="headline">Spa week</span>
            <v-chip class="ma-2" color="green" text-color="white">
                Approved
            </v-chip>
            <v-chip class="ma-2" color="warning" text-color="white">
                Pending
            </v-chip>
            <v-chip class="ma-2" color="red" text-color="white">
                Denied
            </v-chip>
        </v-card-title>
        <v-card-text>
            <v-container>
                <v-row>
                    <v-col cols="12">
                        <v-btn text>
                            <v-avatar color="light-blue" size="36">
                                <span class="white--text headline">UU</span>
                            </v-avatar>
                            <strong style="margin-left: 5px">
                                User Usersson</strong
                            >
                        </v-btn>
                    </v-col>
                     <v-col cols="12">
                        <v-text-field
                            type="text"
                            label="Title"
                            value="Spa week"
                        ></v-text-field>
                    </v-col>
                    <v-col cols="12" sm="6" md="6">
                        <v-text-field
                            type="date"
                            label="Start*"
                            value="2020-10-01"
                        ></v-text-field>
                    </v-col>
                    <v-col cols="12" sm="6" md="6">
                        <v-text-field
                            type="date"
                            label="End*"
                            value="2020-10-07"
                        ></v-text-field>
                    </v-col>
                </v-row>
            </v-container>
        </v-card-text>
        <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="red darken-1" text @click="changeMode"
                >Cancel</v-btn
            >
            <v-btn color="primary darken-1" text @click="closeModal"
                >Submit</v-btn
            >
        </v-card-actions>
    </v-card>
</template>

<script>
export default {
    name: 'ViewRequestEditForm',
    methods: {
        closeModal() {
            this.$emit('closeModal');
        },
        changeMode() {
            this.$emit('changeMode');
        }
    },
}
</script>

<style>

</style>