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
        user: JSON.parse(localStorage.getItem('user')) || null,
        requestHistory: [],
    },
    getters: {
        loggedIn(state) {
            return state.token !== null;
        },
        getCurrentUser(state) {
            return state.user;
        },
        getRequestHistory(state){
            return state.requestHistory
        }
    },
    mutations: {
        setCurrentUser(state, user) {
            state.user = user.user
            state.token = user.token
        },
        destoyToken(state) {
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
                    password: credentials.password
                })
                .then(response => {
                    const user = response.data.data;
                    localStorage.setItem('access_token', user.token);
                    localStorage.setItem('user', JSON.stringify(user.user));
                    context.commit('setCurrentUser', user)
                    resolve(response);
                })
                .catch (error => {
                    console.log(error);
                    reject(error);
                })
            })
        },
        destroyToken(context) {
            if(context.getters.loggedIn) {
                localStorage.removeItem('access_token');
                localStorage.removeItem('user');
                context.commit('destoyToken');
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
                    context.commit('setRequestHistory', requestHistory);
                })
                .catch(error => {
                    console.log(error);
                })

        }
    }
});
