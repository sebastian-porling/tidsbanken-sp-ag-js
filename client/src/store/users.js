import axios from "axios";
import Vue from 'vue';
import { API_URL } from "@/constants/"
axios.defaults.baseURL = API_URL;

export default {
  state: {
    allUsers: [],
    fetchedUser: {}
  },
  getters: {
    getAllUsers(state) {
      return state.allUsers;
    },
    getFetchedUser(state) {
      return state.fetchedUser;
    }
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
    },
    removeUser(state, userId) {
      state.allUsers = state.allUsers.filter(u => u.id != userId);
    },
    setFetchedUser(state, user) {
      state.fetchedUser = user;
    }
  },
  actions: {
    /**
     * Retrieves all users
     * @param {Object} context Store context 
     */
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
    /**
     * 
     * @param {Object} context Store context 
     * @param {*} userId 
     */
    retrieveUser(context, userId) {
      axios
        .get(`/user/${userId}`, {
          headers: {
            authorization: `Bearer ${context.rootGetters.getToken}`,
          },
        })
        .then((response) => {
          const user = response.data.data;
          context.commit("setFetchedUser", user);
        })
        .catch((error) => {
          console.log(error.response);
        });
    },
    /**
     * Creates a new user
     * @param {Object} context Store context
     * @param {Object} newUser 
     */
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
            context.commit("setResponse", response.data.message);
            context.commit("setIsAlert", true);
            resolve(response);
          })
          .catch((error) => {
            reject(error.response);
            context.commit("setResponse", error.response.data.message);
            context.commit("setTypeIsError", true)
            context.commit("setIsAlert", true);
          });
      });
    },
    /**
     * Updates the user with the given user data
     * @param {Object} context Store context
     * @param {Object} user User data
     */
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
              context.commit("updateCurrentUser", updUser);
              context.commit("setResponse", response.data.message);
              context.commit("setIsAlert", true);
              resolve(response);
            })
            .catch((error) => {
              reject(error.response);
              context.commit("setResponse", error.response.data.message);
              context.commit("setTypeIsError", true)
              context.commit("setIsAlert", true);
            });
        });
      },
      /**
       * 
       * @param {Object} context Store context
       * @param {Number} userId user to update password
       * @param {String} password new user password
       */
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
              context.commit("setResponse", response.data.message);
              context.commit("setIsAlert", true);
            })
            .catch((error) => {
              reject(error.response);
              context.commit("setResponse", error.response.data.message);
              context.commit("setTypeIsError", true)
              context.commit("setIsAlert", true);
            });
        });
      },
      /**
       * Deactivates the user on the given id 
       * @param {Object} context The store context
       * @param {Number} userId User id
       */
      deactivateUser(context, userId) {
        axios
            .delete(`user/${userId}`, {
              headers: {
                authorization: `Bearer ${context.rootGetters.getToken}`,
              },
            })
            .then((response) => {
              context.commit("removeUser", userId);
              context.commit("setResponse", response.data.message);
              context.commit("setIsAlert", true);
            })
            .catch((error) => {
              context.commit("setResponse", error.response.data.message);
              context.commit("setTypeIsError", true)
              context.commit("setIsAlert", true);
            });
      }
  },
};
