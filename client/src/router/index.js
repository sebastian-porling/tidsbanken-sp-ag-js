import Vue from 'vue'
import Router from 'vue-router'
import AdminPage from '../pages/admin-page/AdminPage'
import DashboardPage from '../pages/dashboard-page/DashboardPage'
import LoginPage from '../pages/login-page/LoginPage'
import RequestHistoryPage from '../pages/request-history-page/RequestHistoryPage'
import UserPage from '../pages/user-profile-page/UserPage'
import TableSelect from '../pages/admin-page/TableSelect'
import UserTable from '../pages/admin-page/UserTable'

Vue.use(Router)

export default new Router ({
    routes: [
        {
            path:'/',
            name: 'Dashboard',
            component: DashboardPage,
            meta: {
                requiresAuth: true
            }
        },
        {
            path:'/login',
            name: 'Login',
            component: LoginPage,
            meta: {
                requiresVisitor: true
            }
        },
         {
            path:'/admin',
            name: 'Admin',
            component: AdminPage,
            meta: {
                requiresAdmin: true
            }
        }, 
        {
            path:'/history/:id',
            name: 'RequestHistory',
            component: RequestHistoryPage,
            meta: {
                requiresAuth: true
            }
        },
        {
            path:'/profile',
            name: 'UserProfile',
            component: UserPage,
            meta: {
                requiresAuth: true
            }
        },
        {
            path:'/requests',
            name: 'ViewRequests',
            component: TableSelect
        },
        {
            path: '/users',
            name: 'ViewUsers',
            component: UserTable
        }
    ]
})
