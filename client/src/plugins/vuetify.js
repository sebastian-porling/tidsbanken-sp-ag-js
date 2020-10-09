import Vue from 'vue'
import Vuetify from 'vuetify'
import 'vuetify/dist/vuetify.min.css'

/**
 * Add Vuetify Plugin to the Vue instance
 */
Vue.use(Vuetify)

/**
 * Any Options
 */
const opts = {}

/**
 * Export the vuetify instance
 */
export default new Vuetify(opts)