import Vue from 'vue'
import App from './App.vue'
import vuetify from '@/plugins/vuetify'
import router from './router'
import { store } from './store/index'

Vue.config.productionTip = false

// Redirects user to other page if not authorized
router.beforeEach((to, from, next) => {

  // Page requires the user to be logged in, if the user is not, redirects
  if(to.matched.some(record => record.meta.requiresAuth)) {
    if (!store.getters.loggedIn){
      next({
        name: 'Login',
      })
    } else {
      next()
    }
  } 

  // Page require the user to not be logged in, if the user is though, redirects
  else if (to.matched.some(record => record.meta.requiresVisitor)) {
    if (store.getters.loggedIn){
      next({
        name: 'Dashboard',
      })
    } else {
      next ()
    }
  } 

   // Page requires admin
   else if (to.matched.some(record => record.meta.requiresAdmin)) {
    if (!store.getters.getCurrentUser.is_admin){
      next({
        name: 'Dashboard',
      })
    } else {
      next ()
    }
  } 
  
  else {
    next ()
  }

})

new Vue({
  store,
  vuetify,
  router,
  render: h => h(App),
}).$mount('#app')
