import axios from "axios";
axios.defaults.baseURL = "http://localhost:3400/";

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
        retrieveAllSettings({commit, rootGetters}) {
            axios.get('/setting', {
                headers: {
                    authorization: `Bearer ${rootGetters.getToken}`
                }
            })
            .then(response => commit('setAllSettings', response.data.data))
            .catch(error => console.log(error.response));
        },
        deleteSetting({commit, rootGetters}, settingId) {
            axios.delete(`/setting/${settingId}`, {
                headers: {
                    authorization: `Bearer ${rootGetters.getToken}`
                }
            })
            .then(commit('removeSettingById', settingId))
            .catch(error => console.log(error.response));
        },
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
        patchSetting({commit, rootGetters}, setting) {
            axios.patch(`/setting/${setting.id}`, setting, {
                headers: {
                    authorization: `Bearer ${rootGetters.getToken}`
                }
            })
            .then(response => commit('replaceSetting', response.data.data))
            .catch(error => console.log(error.response));
        },
        createSetting({commit, rootGetters}, setting) {
            axios.post(`/setting`, setting, {
                headers: {
                    authorization: `Bearer ${rootGetters.getToken}`
                }
            })
            .then(response => commit('addSetting', response.data.data))
            .catch(error => console.log(error));
        }
    }
}