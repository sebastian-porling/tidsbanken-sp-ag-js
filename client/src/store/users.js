import axios from "axios";
axios.defaults.baseURL = "http://localhost:3400/";

export default {
    state: {
        allUsers: []
    },
    getters: {
        getAllUsers(state) {
            return state.allUsers;
        }
    },
    mutations: {
        setAllUsers(state, users) {
            state.allUsers = users;
        }
    },
    actions: {
        retrieveAllUsers(context) {
            axios
                .get(`/user/all`, {
                    headers: {
                        authorization: `Bearer ${context.rootGetters.getToken}`
                    }
                })
                .then(response => {
                    const allUsers = response.data.data;
                    context.commit("setAllUsers", allUsers);
                })
                .catch(error => {
                    console.log(error.response);
                });
        }
    }
};
