import axios from "axios";
axios.defaults.baseURL = "http://localhost:3400/";
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
            localStorage.setItem("user", JSON.stringify(user));
            state.user = user;
        }
    },
    actions: {
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
                    });
            });
        },
        destroyToken(context) {
            if (context.getters.loggedIn) {
                localStorage.removeItem("access_token");
                localStorage.removeItem("user");
                context.commit("destroyToken");
            }
        }
    }
};
