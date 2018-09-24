import Vue from 'vue'
import Router from 'vue-router'
import Hello from '@/components/Hello'
import Secured from '@/components/Secured'
import Books from '@/components/Books'
import Login from '@/components/auth/Login'
import Logout from '@/components/auth/Logout'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Hello',
      component: Hello
    },
    {
      path: '/secured',
      name: 'Secured',
      component: Secured,
      meta: {
        requiresAuth: true, requiresAdmin: true
      }
    },
    {
      path: '/books',
      name: 'Books',
      component: Books,
      meta: {
        requiresAuth: true
      }
    },
    {
      path: '/login',
      name: 'Login',
      component: Login
    },
    {
      path: '/logout',
      name: 'Logout',
      component: Logout
    }
  ]
})
