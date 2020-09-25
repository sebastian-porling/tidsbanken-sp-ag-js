<template>
    <v-card v-if="setting">
        <v-card-title>
            <span class="headline">Edit Application Setting</span>
        </v-card-title>
        <v-card-text>
            <v-container>
                <v-row justify="end">
                    <v-col cols="12" sm="6" md="6">
                        <v-text-field
                            label="Setting"
                            v-model="setting.key"
                        ></v-text-field>
                    </v-col>
                    <v-col cols="12" sm="6" md="6">
                        <v-text-field
                            label="Value"
                            v-model="setting.value"
                        ></v-text-field>
                    </v-col>
                    
                </v-row>
            </v-container>
        </v-card-text>
        <v-card-actions >
            <v-row justify="end" style="margin-right: 10px;">
                <v-btn color="red darken-1" text @click="closeEdit">Cancel</v-btn>
                <v-btn color="green" text v-on:click="editSetting">Edit</v-btn>
            </v-row>
        </v-card-actions>
    </v-card>
</template>

<script>
export default {
    name: 'ApplicationSettingsEditForm',
    props: ['setting'],
    methods: {
        editSetting() {
            if (this.setting.key && this.setting.value) {
                this.$store.dispatch('patchSetting', {id: this.setting.id, key: this.setting.key, value: this.setting.value});
                this.$emit('showSettings');
            }
        },
        closeEdit() {
            this.$emit('showSettings');
        }
    },

};
</script>

<style></style>
