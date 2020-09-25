import axios from "axios";
axios.defaults.baseURL = "http://localhost:3400/";

export default {
    state: {
        ineligiblePeriod: []
    },
    getters: {
        getIneligiblePeriod(state) {
            return state.ineligiblePeriod;
        }
    },
    mutations: {
        setIneligiblePeriod(state, ineligible) {
            state.ineligiblePeriod = ineligible;
        }
    },
    actions: {
        retrieveIneligiblePeriod(context) {
            axios
                .get('/ineligible', {
                    headers: {
                        authorization: `Bearer ${context.rootGetters.getToken}`
                    }
                })
                .then(response => {
                    const ineligiblePeriod = response.data.data;
                    context.commit("setIneligiblePeriod", ineligiblePeriod);
                })
                .catch(error => {
                    console.log(error.response);
                });
        }
    }
};
