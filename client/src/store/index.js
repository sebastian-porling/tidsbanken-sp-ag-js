import Vuex from "vuex";
import Vue from "vue";

import comments from "./comments";
import login from "./login";
import requestHistory from "./requestHistory";
import requests from "./requests";
import users from "./users";
import settings from "./settings";
import notifications from "./notifications";
import ineligible from "./ineligible";
import import_export from "./import_export";
import status from "./status";

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
        notifications,
        ineligible,
        import_export,
        status,
    ]
});
