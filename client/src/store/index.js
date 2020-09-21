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
    },
    getters: {
        loggedIn(state) {
            return state.token !== null;
        },
    },
    mutations: {
        retrieveToken(state, token) {
            state.token = token;
        },
        destoyToken(state) {
            state.token = null;
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
                    const token = response.data.data.token;
                    localStorage.setItem('access_token', token);
                    context.commit('retrieveToken', token);
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
                context.commit('destoyToken');
            }
        }
    }
});
