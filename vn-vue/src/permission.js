import router from './router'
import store from './store'
import { Message } from 'element-ui'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import { getToken } from '@/utils/auth'

NProgress.configure({ showSpinner: false })

const whiteList = ['/login', '/auth-redirect', '/bind', '/register']

router.beforeEach((to, from, next) => {
  NProgress.start()
  if (getToken()) {
    /* has token*/
    if (to.path ==='/login') {
      next({ path:'/' })
      NProgress.done()
    } else {
      if (store.getters.roles.length === 0) {
        // Determine whether the current user has pulled user_info information
        store.dispatch('GetInfo').then(res => {
          // Pull user_info
          const roles = res.roles
          store.dispatch('GenerateRoutes', {roles }).then(accessRoutes => {
          // Test the default static page
          // store.dispatch('permission/generateRoutes', {roles }).then(accessRoutes => {
            // Generate an accessible routing table based on roles permissions
            router.addRoutes(accessRoutes) // dynamically add accessible routing table
            next({ ...to, replace: true }) // hack method to ensure that addRoutes is completed
          })
        })
          .catch(err => {
            store.dispatch('FedLogOut').then(() => {
              Message.error(err)
              next({ path:'/' })
            })
          })
      } else {
        next()
        //If there is no need to dynamically change permissions, you can directly delete the permission judgment below by next() ↓
        if (hasPermission(store.getters.roles, to.meta.roles)) {
        next()
        } else {
        next({ path:'/401', replace: true, query: {noGoBack: true }})
        }
        //Can be deleted ↑
      }
    }
  } else {
    // no token
    if (whiteList.indexOf(to.path) !== -1) {
      // In the whitelist of free login, enter directly
      next()
    } else {
      next(`/login?redirect=${to.fullPath}`) // Otherwise all redirect to the login page
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  NProgress.done()
})
