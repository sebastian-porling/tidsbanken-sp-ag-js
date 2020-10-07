import Vue from 'vue'
import Router from 'vue-router'
const AdminPage = () => import('../pages/admin-page/AdminPage')
const DashboardPage = () => import('../pages/dashboard-page/DashboardPage')
const LoginPage = () => import('../pages/login-page/LoginPage')
const RequestHistoryPage = () => import('../pages/request-history-page/RequestHistoryPage')
const UserPage = () => import('../pages/user-profile-page/UserPage')
const NotFound = () => import('../pages/error/NotFound')

Vue.use(Router)

export default new Router ({
    mode: "history",
    routes: [
        {
            path:'/',
            name: 'Dashboard',
            component: DashboardPage,
            props: true,
            meta: {
                requiresAuth: true
            }
        },
        {
            path:'/login',
            name: 'Login',
            component: LoginPage,
            props: true,
            meta: {
                requiresVisitor: true
            }
        },
         {
            path:'/admin',
            name: 'Admin',
            component: AdminPage,
            props: true,
            meta: {
                requiresAuth: true,
                requiresAdmin: true
            }
        }, 
        {
            path:'/history/:id',
            name: 'RequestHistory',
            component: RequestHistoryPage,
            props: true,
            meta: {
                requiresAuth: true
            }
        },
        {
            path:'/profile',
            name: 'UserProfile',
            component: UserPage,
            props: true,
            meta: {
                requiresAuth: true
            }
        },
        {
            path: '*',
            name: '404',
            component: NotFound,
            meta: {
                requiresAuth: true
            }
        }

    ]
})
