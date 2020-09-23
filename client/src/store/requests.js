import axios from "axios";
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
            state.allRequests[index] = request;
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
        }
    }
};
