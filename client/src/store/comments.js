import axios from "axios";
axios.defaults.baseURL = "http://localhost:3400/";
export default {
    state: {
        comments: []
    },
    getters: {
        getComments(state) {
            return state.comments;
        }
    },
    mutations: {
        setComments(state, comments) {
            state.comments = comments;
        }
    },
    actions: {
        retrieveComments(context, requestId) {
            axios
                .get(`request/${requestId}/comment`, {
                    headers: {
                        authorization: `Bearer ${context.rootGetters.getToken}`
                    }
                })
                .then(response => {
                    const comments = response.data.data;
                    context.commit("setComments", comments);
                })
                .catch(error => {
                    console.log(error);
                });
        }
    }
};
