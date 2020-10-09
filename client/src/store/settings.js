import axios from "axios";
import { API_URL } from "@/constants/"
axios.defaults.baseURL = API_URL;

export default {
    state: {
        settings: []
    },
    getters: {
        getAllSettings(state) {
            return state.settings;
        } 
    },
    mutations: {
        setAllSettings(state, settings) {
            state.settings = settings;
        },
        removeSettingById(state, settingId) {
            state.settings = state.settings.filter((s) =>  s.id !== settingId);
        },
        replaceSetting(state, setting) {
            state.settings = [...state.settings.filter((s) => s.id !== setting.id), setting];
        },
        addSetting(state, setting) {
            console.log(setting);
            state.settings = [...state.settings, setting];
        }
    },
    actions: {
        /**
         * Retrieves all settings
         * @param {Object} context Store context 
         */
        retrieveAllSettings({commit, rootGetters}) {
            axios.get('/setting', {
                headers: {
                    authorization: `Bearer ${rootGetters.getToken}`
                }
            })
            .then(response => {
                commit('setAllSettings', response.data.data)
            })
            .catch(error => console.log(error.response));
        },
        /**
         * Deletes settings by id
         * @param {Object} context Store context 
         * @param {Number} settingId setting id
         */
        deleteSetting({commit, rootGetters}, settingId) {
            axios.delete(`/setting/${settingId}`, {
                headers: {
                    authorization: `Bearer ${rootGetters.getToken}`
                }
            })
            .then(() => {
                commit('removeSettingById', settingId)
            })
            .catch(error => {
                console.log(error.response)
                commit("setResponse", error.response.data.message);
                commit("setTypeIsError", true)
                commit("setIsAlert", true);
            }
            );
        },
        /**
         * Retrieve Setting by id
         * @param {Object} context Store context 
         * @param {Number} settingId Setting id
         */
        retrieveSettingById({rootGetters}, settingId){
            return new Promise((resolve, reject) => {
                axios.get(`/setting/${settingId}`, {
                    headers: {
                        authorization: `Bearer ${rootGetters.getToken}`
                    }
                })
                .then(response => resolve(response.data.data))
                .catch(error => reject(error.response));
            });
        },
        /**
         * Update setting by id
         * @param {Object} context Store context 
         * @param {Object} setting Setting data
         */
        patchSetting({commit, rootGetters}, setting) {
            axios.patch(`/setting/${setting.id}`, setting, {
                headers: {
                    authorization: `Bearer ${rootGetters.getToken}`
                }
            })
            .then(response =>{
                commit('replaceSetting', response.data.data)
            })
            .catch(error => {
                console.log(error.response)
                commit("setResponse", error.response.data.message);
                commit("setTypeIsError", true)
                commit("setIsAlert", true);
            });
        },
        /**
         * Creates a new setting
         * @param {Object} context Store context 
         * @param {Object} setting Setting data
         */
        createSetting({commit, rootGetters}, setting) {
            axios.post(`/setting`, setting, {
                headers: {
                    authorization: `Bearer ${rootGetters.getToken}`
                }
            })
            .then(response => {
                commit('addSetting', response.data.data)
            })
            .catch(error => {
                console.log(error)
                commit("setResponse", error.response.data.message);
                commit("setTypeIsError", true)
                commit("setIsAlert", true)
            });
        }
    }
}