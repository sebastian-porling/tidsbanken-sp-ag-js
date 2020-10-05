<template>
  <v-card>
        <v-card-title>
            <span class="headline">Application Settings</span>
        </v-card-title>
        <v-card-text>
            <v-form v-model="valid">
                <v-row justify="end">
                    <v-col cols="12" sm="6" md="6">
                        <v-text-field
                            label="Setting"
                            v-model="key"
                            :rules="keyRules"
                        ></v-text-field>
                    </v-col>
                    <v-col cols="12" sm="6" md="6">
                        <v-text-field
                            label="Value"
                            v-model="value"
                            :rules="valueRules"
                        ></v-text-field>
                    </v-col>

                    <v-progress-circular
                    v-if="isLoading"
                    indeterminate
                    color="green"
                    ></v-progress-circular>
                    <v-btn v-if="!isLoading" color="green" text v-on:click="addSetting" :disabled="!valid">Add</v-btn>
                </v-row>
            </v-form>

            <v-data-table
                :headers="headers"
                :items="settings"
                sort-by="key"
                class="elevation-1"
                hide-default-footer
            >
                <template v-slot:item.actions="{ item }">
                    <v-icon small class="mr-2" @click="editSetting(item)">mdi-pencil</v-icon>
                    <v-icon small @click="deleteSetting(item.id)">mdi-delete</v-icon>
                </template>
            </v-data-table>
        </v-card-text>
        <v-card-actions>
            <v-row justify="end" style="margin-right: 10px;">
                <v-btn color="red darken-1" text @click="closeModal">Cancel</v-btn>
            </v-row>
        </v-card-actions>
    </v-card>
</template>

<script>
export default {
    name: 'ApplicationSettingsInfo',
    data() {
        return {
            isLoading: false,
            valid: true,
            key: null,
            value: null,
            headers: [
                { text: 'Setting', align: 'start', value: 'key'},
                { text: 'Value', value: 'value' },
                { text: 'Actions', value: 'actions', sortable: false },
            ],
            keyRules: [
                (v) => !!v || "Field is required",
            ],
            valueRules: [
                (v) => !!v || "Field is required",
            ],
        };
    },
    props: ["active"],
    created() {
        this.$store.dispatch("retrieveAllSettings");
    },
    computed: {
        settings: {
            get() {
                return this.$store.getters.getAllSettings;
            }
        }
    },
    methods: {
        closeModal() {
            this.$emit("closeModal");
        },
        openModal() {
            this.dialog = true;
        },
        deleteSetting(settingId) {
          this.$store.dispatch('deleteSetting', settingId);
        },
        addSetting() {
            this.isLoading = true;
          if(this.key && this.value) {
            console.log({key: this.key, value: this.value})
            this.$store.dispatch('createSetting', {key: this.key, value: this.value})
            .then(() => {
            setTimeout(() => {
              this.isLoading = false;
            }, 500);
          })
          .catch(() => {
            setTimeout(() => {
              this.isLoading = false;
            }, 500);
          });
            this.key = null;
            this.value = null;
          }
        },
        editSetting(setting) {
            this.$emit('editSetting', setting);
        }
    }
}
</script>

<style>
.addbutton {
    padding-left: 10%;
    padding-top: 2.8%;
}
.editbutton {
    padding-left: 10%;
    padding-top: 1%;
}
.set {
    padding-left: 3%;
    padding-bottom: 1%;
    padding-right: 10%;
}
.set,
.val {
    display: inline;
}
</style>