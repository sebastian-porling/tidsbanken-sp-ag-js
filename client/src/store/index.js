import Vuex from "vuex";
import Vue from "vue";
import axios from "axios";

// Load vuex
Vue.use(Vuex);
axios.defaults.baseURL = "http://localhost:3400/";

// Create store
export const store = new Vuex.Store({
  state: {
    token: localStorage.getItem("access_token") || null,
    user: JSON.parse(localStorage.getItem("user")) || null,
    requestHistory: [],
    comments: [],
    qrCode: null,
    allRequests: [],
    allUsers: [],
  },
  getters: {
    loggedIn(state) {
      return state.token !== null;
    },
    getCurrentUser(state) {
      return state.user;
    },
    getRequestHistory(state) {
        return state.requestHistory;
    },
    getComments(state) {
        return state.comments;
    },
    getAllRequests(state) {
        return state.allRequests;
    },
    getAllUsers(state) {
        return state.allUsers;
    }
  },
  mutations: {
    setCurrentUser(state, user) {
      state.user = user.user;
      state.token = user.token;
    },
    multiAuthQrCode(state, qrCode) {
      state.qrCode = qrCode;
    },
    destroyToken(state) {
      state.token = null;
    },
    setRequestHistory(state, requests) {
      state.requestHistory = requests;
    },
    setComments(state, comments) {
      state.comments = comments;
    },
    setAllRequests(state, requests) {
        state.allRequests = requests;
    },
    setAllUsers(state, users) {
        state.allUsers = users;
    }
},
    actions: {
      retrieveToken(context, credentials) {
        return new Promise((resolve, reject) => {
          axios
            .post("login", {
              email: credentials.email,
              password: credentials.password,
              code: credentials.code,
            })
            .then((response) => {
              const data = response.data.data;
              localStorage.setItem("access_token", data.token);
              localStorage.setItem("user", JSON.stringify(data.user));
              context.commit("setCurrentUser", data);
              resolve(response);
            })
            .catch((error) => {
              reject(error.response);
            });
        });
      },
      retrieveQr(context, credentials) {
        return new Promise((resolve, reject) => {
          axios
            .post("login", {
              email: credentials.email,
              password: credentials.password,
            })
            .then((response) => {
              const data = response.data.data;
              context.commit("multiAuthQrCode", data);
              resolve(response);
            })
            .catch((error) => {
              reject(error.response);
            });
        });
      },
      destroyToken(context) {
        if (context.getters.loggedIn) {
          localStorage.removeItem("access_token");
          localStorage.removeItem("user");
          context.commit("destroyToken");
        }
      },
      retrieveRequestHistory(context, userId) {
        axios
          .get(`user/${userId}/requests`, {
            headers: {
              authorization: `Bearer ${this.state.token}`,
            },
          })
          .then((response) => {
            const requestHistory = response.data.data;
            context.commit("setRequestHistory", requestHistory);
          })
          .catch((error) => {
            console.log(error);
          });
      },
      retrieveAllRequests(context) {
        axios
          .get(`/request`, {
            headers: {
              authorization: `Bearer ${this.state.token}`,
            },
          })
          .then((response) => {
            const allRequests = response.data.data;
            context.commit("setAllRequests", allRequests);
          })
          .catch((error) => {
            console.log(error);
          });
      },
      retrieveAllUsers(context) {
        axios
          .get(`/user/all`, {
            headers: {
              authorization: `Bearer ${this.state.token}`,
            },
          })
          .then((response) => {
            const allUsers = response.data.data;
            context.commit("setAllUsers", allUsers);
          })
          .catch((error) => {
            console.log(error);
          });
      },
    },
    retrieveComments(context, requestId) {
      axios
        .get(`request/${requestId}/comment`, {
          headers: {
            authorization: `Bearer ${this.state.token}`,
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
  });
