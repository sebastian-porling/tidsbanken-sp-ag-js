import Vuex from 'vuex';
import Vue from 'vue';
import axios from 'axios';

// Load vuex
Vue.use(Vuex);
axios.defaults.baseURL = "http://localhost:3400/";

// Create store
export const store = new Vuex.Store({
    state: {
        token: localStorage.getItem('access_token') || null,
        user: localStorage.getItem('user') || null,
        requestHistory: [],
        qrCode: null
    },
    getters: {
        loggedIn(state) {
            return state.token !== null;
        },
        currentUser(state) {
            return state.user !== null;
        },
        getRequestHistory(state){
            return state.requestHistory
        }
    },
    mutations: {
        retrieveCurrentUser(state, user) {
            state.currentUser = user.user
            state.token = user.token
        },
        multiAuthQrCode(state, qrCode) {
            state.qrCode = qrCode;
        },
        destroyToken(state) {
            state.token = null;
        },
        setRequestHistory(state, requests) {
            state.requestHistory = requests;
        }
    },
    actions: {
        retrieveToken(context, credentials) {
            return new Promise((resolve, reject) => {
                axios.post('login', {
                    email: credentials.email,
                    password: credentials.password,
                    code: credentials.code
                })
                .then(response => {
                    const data = response.data.data;
                    localStorage.setItem('access_token', data.token);
                    localStorage.setItem('user', JSON.stringify(data.user));
                    context.commit('retrieveCurrentUser', data);
                    resolve(response);
                })
                .catch (error => {
                    reject(error.response);
                })
            })
        },
        retrieveQr(context, credentials) {
            return new Promise((resolve, reject) => {
                axios.post('login', {
                    email: credentials.email,
                    password: credentials.password
                })
                .then(response => {
                    const data = response.data.data;
                    context.commit('multiAuthQrCode', data);
                    resolve(response);
                })
                .catch (error => {
                    reject(error.response);
                })
            })
        },
        destroyToken(context) {
            if(context.getters.loggedIn) {
                localStorage.removeItem('access_token');
                localStorage.removeItem('user');
                context.commit('destroyToken');
            }
        },
        retrieveRequestHistory(context, userid) {
                axios.get(`user/${userid}/requests`, {
                    headers: {
                        'authorization': `Bearer ${this.state.token}`
                    }
                })
                .then(response => {
                    const requestHistory = response.data.data;
                    console.log(requestHistory);
                    context.commit('setRequestHistory', requestHistory);
                })
                .catch(error => {
                    console.log(error);
                })

        }
    }
});
