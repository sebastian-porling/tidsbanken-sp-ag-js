import axios from "axios";
import Vue from 'vue';
axios.defaults.baseURL = "http://localhost:3400/";

export default {
    state: {
        allRequests: []
    },
    getters: {
        getAllRequests(state) {
            return state.allRequests;
        }
    },
    mutations: {
        setAllRequests(state, requests) {
            state.allRequests = requests;
        },
        updateRequests(state, request) {
            const index = state.allRequests.findIndex(x => x.id == request.id)
            Vue.set(state.allRequests, index, request);
        },
        addRequest(state, request) {
            state.allRequests = [...state.allRequests, request];
        }
    },
    actions: {
        retrieveAllRequests(context) {
            axios
                .get('request', {
                    headers: {
                        authorization: `Bearer ${context.rootGetters.getToken}`
                    }
                })
                .then(response => {
                    const allRequests = response.data.data;
                    context.commit("setAllRequests", allRequests);
                })
                .catch(error => {
                    console.log(error.response);
                });
        },
        patchRequest(context, request) {
            return new Promise((resolve, reject) => {
                axios
                    .patch(`request/${request.id}`, request, {
                        headers: {
                            authorization: `Bearer ${context.rootGetters.getToken}`
                        }
                    })
                    .then(response => {
                        const newRequest = response.data.data;
                        context.commit("updateRequests", newRequest);
                        resolve(response.data);
                    })
                    .catch(error => {
                        reject(error.response);
                    });
            });
        },
        createVacationRequest({commit, rootGetters}, request) {
            return new Promise((resolve, reject) => {
                axios
                    .post(`request`, request, {
                        headers: {
                            authorization: `Bearer ${rootGetters.getToken}`
                        }
                    })
                    .then(response => {
                        const newRequest = response.data.data;
                        commit("addRequest", newRequest);
                        resolve(newRequest);
                    })
                    .catch(error => {
                        reject(error.response);
                    });
            })
        }
    }
};
