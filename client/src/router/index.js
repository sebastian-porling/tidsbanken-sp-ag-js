import Vue from 'vue'
import Router from 'vue-router'
/* import AdminPage from '../pages/admin-page/AdminPage'
 */import DashboardPage from '../pages/dashboard-page/DashboardPage'
import LoginPage from '../pages/login-page/LoginPage'
import RequestHistoryPage from '../pages/request-history-page/RequestHistoryPage'
import UserProfilePage from '../pages/user-profile-page/UserProfilePage'

Vue.use(Router)

export default new Router ({
    routes: [
        {
            path:'/',
            name: 'Dashboard',
            component: DashboardPage
        },
        {
            path:'/login',
            name: 'Login',
            component: LoginPage
        },
/*         {
            path:'/admin',
            name: 'Admin',
            component: AdminPage
        }, */
        {
            path:'/history',
            name: 'Request History',
            component: RequestHistoryPage
        },
        {
            path:'/profile',
            name: 'User Profile',
            component: UserProfilePage
        }
    ]
})