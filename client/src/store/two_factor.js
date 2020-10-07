import axios from "axios";
import { API_URL } from "@/constants/"
axios.defaults.baseURL = API_URL;

export default {
    state: {
        qrCode: null,
        uri: '',
    },
    getters: {
        getQrCode(state) {
            return state.qrCode;
        },
        getUri(state) {
            return state.uri;
        }
    },
    mutations: {
        setMultiAuth(state, multiAuth) {
            state.qrCode = multiAuth.qr_code_png;
            state.uri = multiAuth.uri
        },
    },
    actions: {
        generateMultiAuth({commit, rootGetters}) {
            return new Promise((resolve, reject) => {
                axios.post(`/user/${rootGetters.getCurrentUser.id}/generate_two_factor`, {}, {
                    headers: {
                        authorization: `Bearer ${rootGetters.getToken}`
                    }
                })
                .then(response => {
                    commit('setMultiAuth', response.data.data);
                    resolve(response.data.data);
                })
                .catch(error => reject(error.response));
            })
        }
    }
}