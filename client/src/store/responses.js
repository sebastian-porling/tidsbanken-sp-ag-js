import axios from "axios";
import { API_URL } from "@/constants/"
axios.defaults.baseURL = API_URL;

export default {
    state: {
        isAlert: false,
        response: null,
        typeIsError: null
    },
    getters: {
        getIsAlert(state) {
            return state.isAlert;
        },
        getResponse(state) {
            return state.response;
        },
        getTypeIsError(state) {
            return state.typeIsError;
        }
    }, 
    mutations: {
        setIsAlert(state, bool) {
            state.isAlert = bool;
        },
        setResponse(state, response) {
            state.response = response;
        },
        setTypeIsError(state, bool) {
            state.typeIsError = bool;
        }
    },
    actions: {
        /**
         * Disables the alert
         * @param {Object} context Store context 
         */
        disableIsAlert(context) {
            context.commit("setIsAlert", false);
            context.commit("setResponse", null);
            context.commit("setTypeIsError", false);
        }
    }
}