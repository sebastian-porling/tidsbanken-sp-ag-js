import axios from "axios";
axios.defaults.baseURL = "http://localhost:3400/";

export default {
    state: {
        IneligiblePeriod: []
    },
    getters: {
        getIneligiblePeriod(state) {
            return state.IneligiblePeriod;
        }
    },
    mutations: {
        setIneligiblePeriod(state, ineligible) {
            state.IneligiblePeriod = ineligible;
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
                    const IneligiblePeriod = response.data.data;
                    context.commit("setIneligiblePeriod", IneligiblePeriod);
                })
                .catch(error => {
                    console.log(error.response);
                });
        }
    }
};
