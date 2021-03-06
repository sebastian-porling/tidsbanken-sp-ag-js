import axios from "axios";
import { API_URL } from "@/constants/"
axios.defaults.baseURL = API_URL;

export default {
    state: {
        import_export: localStorage.getItem("import_export") || null
    },
    getters: {
        getData(state) {
            
            return state.import_export;
        }
    },
    mutations: {
        setData(state, data) {
            localStorage.setItem("import_export", data);
            state.import_export = data;
        }
    },
    actions: {
        /**
         * Exports vacations requests
         * @param {Object} context Store context 
         */
        exportData({commit, rootGetters}) {
            return new Promise((resolve, reject) => {
                axios.get('export', {
                    headers: {
                        authorization: `Bearer ${rootGetters.getToken}`
                    }
                })
                .then(response => {
                    const data = JSON.stringify(response.data.data, null, 2);
                    
                    commit('setData', data);
                    resolve(data);
                })
                .catch(error => {
                    reject(error);
                })
            })
        },
        /**
         * Imports vacation requests
         * @param {Object} context Store context 
         * @param {*} data 
         */
        importData({rootGetters, commit}, data) {
            return new Promise((resolve, reject) => {
                axios.post('import', data, {
                    headers: {
                        authorization: `Bearer ${rootGetters.getToken}`
                    }
                })
                .then(response => {
                    resolve(response);
                    commit("setResponse", response.data.message);
                    commit("setIsAlert", true);
                })
                .catch(error => {
                    reject(error);
                    commit("setResponse", error.response.data.message);
                    commit("setTypeIsError", true)
                    commit("setIsAlert", true);
                })
            })
        }
    }
}