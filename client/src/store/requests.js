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
        }
    },
    actions: {
        retrieveAllRequests(context) {
            axios
                .get('/request', {
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
        }
    }
};
