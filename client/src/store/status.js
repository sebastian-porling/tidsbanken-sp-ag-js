import axios from "axios";
import { API_URL } from "@/constants/"
axios.defaults.baseURL = API_URL;

export default {
  state: {
    status: [],
  },
  getters: {
    getStatus(state) {
      return state.status;
    },
  },
  mutations: {
    setStatus(state, status) {
      state.status = status;
    },
  },
  actions: {
    retrieveStatus(context) {
      axios
        .get('/status', {
          headers: {
            authorization: `Bearer ${context.rootGetters.getToken}`,
          },
        })
        .then((response) => {
          const status = response.data.data;
          context.commit("setStatus", status);
        })
        .catch((error) => {
          console.log(error.response);
        });
    },
  },
};
