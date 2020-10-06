import axios from "axios";
import Vue from "vue";
import { API_URL } from "@/constants/"
axios.defaults.baseURL = API_URL;

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
            const index = state.allRequests.findIndex(x => x.id == request.id);
            Vue.set(state.allRequests, index, request);
        },
        addRequest(state, request) {
            state.allRequests = [...state.allRequests, request];
        },
        removeRequest(state, requestId) {
            state.allRequests = state.allRequests.filter(
                x => x.id !== requestId
            );
        }
    },
    actions: {
        retrieveAllRequests(context) {
            axios
                .get("request", {
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
                        context.commit("setResponse", response.data.message);
                        context.commit("setIsAlert", true);
                    })
                    .catch(error => {
                        reject(error.response);
                        context.commit("setResponse", error.response.data.message);
                        context.commit("setTypeIsError", true)
                        context.commit("setIsAlert", true);
                    });
            });
        },
        createVacationRequest({ commit, rootGetters }, request) {
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
                        commit("setResponse", response.data.message);
                        commit("setIsAlert", true);
                    })
                    .catch(error => {
                        reject(error.response);
                        commit("setResponse", error.response.data.message);
                        commit("setTypeIsError", true)
                        commit("setIsAlert", true);
                    });
            });
        },
        deleteRequest(context, request) {
            return new Promise((resolve, reject) => {
                axios
                    .delete(`request/${request.id}`, {
                        headers: {
                            authorization: `Bearer ${context.rootGetters.getToken}`
                        }
                    })
                    .then(response => {
                        context.commit("removeRequest", request.id);
                        resolve(response);
                        context.commit("setResponse", response.data.message);
                        context.commit("setIsAlert", true);
                    })
                    .catch(error => {
                        reject(error.response);
                        context.commit("setResponse", error.response.data.message);
                        context.commit("setTypeIsError", true)
                        context.commit("setIsAlert", true);
                    });
            });
        }
    }
};
