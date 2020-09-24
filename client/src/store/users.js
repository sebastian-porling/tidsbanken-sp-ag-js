import axios from "axios";
import Vue from 'vue';
axios.defaults.baseURL = "http://localhost:3400/";

export default {
  state: {
    allUsers: [],
  },
  getters: {
    getAllUsers(state) {
      return state.allUsers;
    },
  },
  mutations: {
    setAllUsers(state, users) {
      state.allUsers = users;
    },
    updateAllUsers(state, user) {
        const index = state.allUsers.findIndex(x => x.id == user.id)
        Vue.set(state.allUsers, index, user);
    },
  },
  actions: {
    retrieveAllUsers(context) {
      axios
        .get(`/user/all`, {
          headers: {
            authorization: `Bearer ${context.rootGetters.getToken}`,
          },
        })
        .then((response) => {
          const allUsers = response.data.data;
          context.commit("setAllUsers", allUsers);
        })
        .catch((error) => {
          console.log(error.response);
        });
    },
    createUser(context, newUser) {
      return new Promise((resolve, reject) => {
        axios
          .post("user", newUser, {
            headers: {
              authorization: `Bearer ${context.rootGetters.getToken}`,
            },
          })
          .then((response) => {
            const user = response.data.data;
            context.commit("updateAllUsers", user);
            resolve(response);
          })
          .catch((error) => {
            reject(error.response);
          });
      });
    },
  },
};
