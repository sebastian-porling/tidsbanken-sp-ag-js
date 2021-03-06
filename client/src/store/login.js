import axios from "axios";
import { API_URL } from "@/constants/"
axios.defaults.baseURL = API_URL;
export default {
    state: {
        token: localStorage.getItem("access_token") || null,
        user: JSON.parse(localStorage.getItem("user")) || null,
    },
    getters: {
        loggedIn(state) {
            return state.token !== null;
        },
        isAdmin(state) {
            return state.user.is_admin;
        },
        getCurrentUser(state) {
            return state.user;
        },
        getToken(state) {
            return state.token;
        },    
    },
    mutations: {
        setCurrentUser(state, user) {
            state.user = user.user;
            state.token = user.token;
        },
        destroyToken(state) {
            state.token = null;
        },
        updateCurrentUser(state, user) {
            if (user.id === state.user.id) {
                localStorage.setItem("user", JSON.stringify(user));
                state.user = user;
            }
        }
    },
    actions: {
        /**
         * Logs in the user and retrieves the token
         * @param {Object} context Store context 
         * @param {Object} credentials data with email, password and two auth code
         */
        retrieveToken(context, credentials) {
            return new Promise((resolve, reject) => {
                axios
                    .post("login", {
                        email: credentials.email,
                        password: credentials.password,
                        code: credentials.code
                    })
                    .then(response => {
                        const data = response.data.data;
                        localStorage.setItem("access_token", data.token);
                        localStorage.setItem("user", JSON.stringify(data.user));
                        context.commit("setCurrentUser", data);
                        resolve(response);
                    })
                    .catch(error => {
                        reject(error.response);
                    });
            });
        },
        /**
         * Retrieves qr code if it is the first time login in
         * @param {Object} context Store context 
         * @param {Object} credentials data with email and password
         */
        retrieveQr(context, credentials) {
            return new Promise((resolve, reject) => {
                axios
                    .post("login", {
                        email: credentials.email,
                        password: credentials.password
                    })
                    .then(response => {
                        const data = response.data.data;
                        context.commit("setMultiAuth", data);
                        resolve(response);
                    })
                    .catch(error => {
                        reject(error.response);
                        if (!error.respnse.data.message.inclues("code")) {
                            context.commit("setResponse", error.response.data.message);
                            context.commit("setTypeIsError", true)
                            context.commit("setIsAlert", true);
                        }
                    });
            });
        },
        /**
         * Removes the token and user data from local storage
         * @param {Object} context Store context 
         */
        destroyToken(context) {
            if (context.getters.loggedIn) {
                localStorage.removeItem("access_token");
                localStorage.removeItem("user");
                context.commit("destroyToken");
            }
        }
    }
};
