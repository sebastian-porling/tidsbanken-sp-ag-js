import axios from "axios";
axios.defaults.baseURL = "http://localhost:3400/";
export default {
    state: {
        requestHistory: []
    },
    getters: {
        getRequestHistory(state) {
            return state.requestHistory;
        }
    },
    mutations: {
        setRequestHistory(state, requests) {
            state.requestHistory = requests;
        }
    },
    actions: {
        retrieveRequestHistory(context, userId) {
            axios
                .get(`user/${userId}/requests`, {
                    headers: {
                        authorization: `Bearer ${context.rootGetters.getToken}`
                    }
                })
                .then(response => {
                    const requestHistory = response.data.data;
                    context.commit("setRequestHistory", requestHistory);
                })
                .catch(error => {
                    console.log(error);
                });
        }
    }
};
