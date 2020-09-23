import axios from "axios";
axios.defaults.baseURL = "http://localhost:3400/";
export default {
    state: {
        token: localStorage.getItem("access_token") || null,
        user: JSON.parse(localStorage.getItem("user")) || null,
        qrCode: null
    },
    getters: {
        loggedIn(state) {
            return state.token !== null;
        },
        getCurrentUser(state) {
            return state.user;
        },
        getToken(state) {
            return state.token;
        },
        getQrCode(state) {
            return state.qrCode;
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
                        context.commit("multiAuthQrCode", data);
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
