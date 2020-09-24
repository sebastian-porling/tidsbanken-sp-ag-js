import axios from "axios";
import Vue from 'vue';

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
        },
        updateComments(state, comment) {
            const index = state.comments.findIndex(x => x.id == comment.id)
            Vue.set(state.omments, index, comment);
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
        },
        createComment(context, requestId, message) {
            return new Promise((resolve, reject) => {
              axios
                .post(`request/${requestId}/comment`, {
                    message
                }, {
                  headers: {
                    authorization: `Bearer ${context.rootGetters.getToken}`,
                  },
                })
                .then((response) => {
                  const comment = response.data.data;
                  context.commit("updateComments", comment);
                  resolve(response);
                })
                .catch((error) => {
                  reject(error.response);
                });
            });
          },
    }
};
