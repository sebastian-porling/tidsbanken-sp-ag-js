import Vuex from "vuex";
import Vue from "vue";

import comments from "./comments";
import login from "./login";
import requestHistory from "./requestHistory";
import requests from "./requests";
import users from "./users";
import settings from "./settings";

// Load vuex
Vue.use(Vuex);

// Create store
export const store = new Vuex.Store({
    modules: [
        login, 
        requestHistory, 
        comments,
        requests,
        users,
        settings,
    ]
});
