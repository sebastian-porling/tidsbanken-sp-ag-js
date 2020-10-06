import axios from "axios";
axios.defaults.baseURL = "http://localhost:3400/";

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
        disableIsAlert(context) {
            context.commit("setIsAlert", false);
            context.commit("setResponse", null);
            context.commit("setTypeIsError", false);
        }
    }
}