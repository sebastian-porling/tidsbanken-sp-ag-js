import axios from "axios";
import Vue from 'vue';

axios.defaults.baseURL = "http://localhost:3400/";
export default {
  state: {
    comments: [],
  },
  getters: {
    getComments(state) {
      return state.comments;
    },
  },
  mutations: {
    setComments(state, comments) {
      state.comments = comments;
    },
    updateComments(state, comment) {
      state.comments = [...state.comments, comment];
    },
    removeComment(state, comment) {
      state.comments = state.comments.filter(c => c.id != comment.id);
    },
    patchComment(state, comment) {
      const index = state.comments.findIndex(x => x.id == comment.id)
      Vue.set(state.comments, index, comment);
    }
  },
  actions: {
    retrieveComments(context, requestId) {
      axios
        .get(`request/${requestId}/comment`, {
          headers: {
            authorization: `Bearer ${context.rootGetters.getToken}`,
          },
        })
        .then((response) => {
          const comments = response.data.data;
          context.commit("setComments", comments);
        })
        .catch((error) => {
          console.log(error);
        });
    },
    createComment(context, { requestId, message }) {
      return new Promise((resolve, reject) => {
        axios
          .post(
            `request/${requestId}/comment`,
            {
              message,
            },
            {
              headers: {
                authorization: `Bearer ${context.rootGetters.getToken}`,
              },
            }
          )
          .then((response) => {
            const comment = response.data.data;
            context.commit("updateComments", comment);
            resolve(response);
          })
          .catch((error) => {
            reject(error.response);
            context.commit("setResponse", error.response.data.message);
            context.commit("setTypeIsError", true);
            context.commit("setIsAlert", true);
          });
      });
    },
    updateComment(context, { requestId, message, commentId }) {
      axios
          .patch(
            `request/${requestId}/comment/${commentId}`,
            { message },
            {
              headers: {
                authorization: `Bearer ${context.rootGetters.getToken}`,
              },
            }
          )
          .then((response) => {
            const comment = response.data.data;
            context.commit("patchComment", comment);
            context.commit("setResponse", response.data.message);
            context.commit("setIsAlert", true);
          })
          .catch((error) => {
            context.commit("setResponse", error.response.data.message);
            context.commit("setTypeIsError", true);
            context.commit("setIsAlert", true);
          });
    },
    deleteComment(context, { requestId, commentId }) {
      axios
          .delete(
            `request/${requestId}/comment/${commentId}`,
            {
              headers: {
                authorization: `Bearer ${context.rootGetters.getToken}`,
              },
            }
          )
          .then((response) => {
            context.commit("removeComment", commentId);
            context.commit("setResponse", response.data.message);
            context.commit("setIsAlert", true);
          })
          .catch((error) => {
            context.commit("setResponse", error.response.data.message);
            context.commit("setTypeIsError", true);
            context.commit("setIsAlert", true);
          });
    }
  },
};
