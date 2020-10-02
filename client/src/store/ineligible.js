import axios from "axios";
import Vue from 'vue';
axios.defaults.baseURL = "http://localhost:3400";
const headers = (token) => {return {headers: {authorization: `Bearer ${token}`}}};

export default {
    state: {
        ineligiblePeriods: []
    },
    getters: {
        getIneligiblePeriods(state) {
            return state.ineligiblePeriods;
        }
    },
    mutations: {
        setIneligiblePeriods(state, ineligiblePeriods) {
            state.ineligiblePeriods = ineligiblePeriods;
        },
        removeIneligiblePeriod(state, ineligiblePeriodId) {
            state.ineligiblePeriods = state.ineligiblePeriods.filter(ip => ip.id !== ineligiblePeriodId);
        },
        replaceIneligiblePeriod(state, ineligiblePeriod) {
            const index = state.ineligiblePeriods.findIndex(ip => ip.id === ineligiblePeriod.id)
            Vue.set(state.ineligiblePeriods, index, ineligiblePeriod);
        },
        addIneligiblePeriod(state, ineligiblePeriod) {
            state.ineligiblePeriods = [...state.ineligiblePeriods, ineligiblePeriod];
        }
    },
    actions: {
        retrieveIneligiblePeriods({commit, rootGetters}) {
            axios
                .get('/ineligible', headers(rootGetters.getToken))
                .then(response => commit("setIneligiblePeriods", response.data.data))
                .catch(error => console.log(error.response));
        },
        deleteIneligiblePeriod({commit, rootGetters}, ineligiblePeriodId) {
            return new Promise((resolve, reject) => {
                axios.delete(`/ineligible/${ineligiblePeriodId}`, headers(rootGetters.getToken))
                .then((response) => {
                    commit('removeIneligiblePeriod', ineligiblePeriodId);
                    resolve(response);
                    commit("setResponse", response.data.message);
                    commit("setIsAlert", true);
                })
                .catch(error => {
                    console.log(error.response);
                    reject(error.response);
                    commit("setResponse", error.response.data.message);
                    commit("setTypeIsError", true)
                    commit("setIsAlert", true);
                });
            });
        },
        updateIneligiblePeriod({commit, rootGetters}, ineligiblePeriod) {
            return new Promise((resolve, reject) => {
                axios.patch(`/ineligible/${ineligiblePeriod.id}`, ineligiblePeriod, headers(rootGetters.getToken))
                .then(response => {
                    commit('replaceIneligiblePeriod', response.data.data);
                    resolve(response.data.data);
                    commit("setResponse", response.data.message);
                    commit("setIsAlert", true);
                })
                .catch(error => {
                    console.log(error.response);
                    reject(error.response);
                    commit("setResponse", error.response.data.message);
                    commit("setTypeIsError", true)
                    commit("setIsAlert", true);
                });
            });
        },
        createIneligiblePeriod({commit, rootGetters}, ineligiblePeriod) {
            return new Promise((resolve, reject) => {
                axios.post('/ineligible', ineligiblePeriod, headers(rootGetters.getToken))
                .then(response => {
                    console.log('CREATED', response.data.data);
                    commit('addIneligiblePeriod', response.data.data);
                    resolve(response.data.data);
                    commit("setResponse", response.data.message);
                    commit("setIsAlert", true);
                })
                .catch(error => {
                    console.log(error.response);
                    reject(error.response);
                    commit("setResponse", error.response.data.message);
                    commit("setTypeIsError", true)
                    commit("setIsAlert", true);
                })
            })
            
        }
    }
};
