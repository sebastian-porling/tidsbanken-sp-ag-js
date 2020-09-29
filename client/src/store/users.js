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
    addUser(state, user) {
      state.allUsers = [...state.allUsers, user];
    }
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
            context.commit("addUser", user);
            resolve(response);
          })
          .catch((error) => {
            reject(error.response);
          });
      });
    },
    updateUser(context, user) {
        return new Promise((resolve, reject) => {
          axios
            .patch(`user/${user.id}`, user, {
              headers: {
                authorization: `Bearer ${context.rootGetters.getToken}`,
              },
            })
            .then((response) => {
              const updUser = response.data.data;
              context.commit("updateAllUsers", updUser);
              resolve(response);
            })
            .catch((error) => {
              reject(error.response);
            });
        });
      },
      changePassword(context, userId, password) {
        return new Promise((resolve, reject) => {
          axios
            .post(`user/${userId}/update_password`, {password}, {
              headers: {
                authorization: `Bearer ${context.rootGetters.getToken}`,
              },
            })
            .then((response) => {
              resolve(response);
            })
            .catch((error) => {
              reject(error.response);
            });
        });
      },
  },
};
