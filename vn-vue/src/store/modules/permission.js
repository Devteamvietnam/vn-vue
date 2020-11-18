import {constantRoutes} from'@/router'
import {getRouters} from'@/services/menu'
import Layout from'@/layouts'

const permission = {
  state: {
    routes: [],
    addRoutes: []
  },
  mutations: {
    SET_ROUTES: (state, routes) => {
      state.addRoutes = routes
      state.routes = constantRoutes.concat(routes)
    }
  },
  actions: {
    // Generate route
    GenerateRoutes({ commit }) {
      return new Promise(resolve => {
        // Request routing data from the backend
        getRouters().then(res => {
          const accessedRoutes = filterAsyncRouter(res.data)
          accessedRoutes.push({ path:'*', redirect:'/404', hidden: true })
          commit('SET_ROUTES', accessedRoutes)
          resolve(accessedRoutes)
        })
      })
    }
  }
}

// Traverse the routing string from the background and convert it into a component object
function filterAsyncRouter(asyncRouterMap) {
  return asyncRouterMap.filter(route => {
    if (route.component) {
      // Layout component special treatment
      if (route.component ==='Layout') {
        route.component = Layout
      } else {
        route.component = loadView(route.component)
      }
    }
    if (route.children != null && route.children && route.children.length) {
      route.children = filterAsyncRouter(route.children)
    }
    return true
  })
}

export const loadView = (view) => {// Route lazy loading
  return (resolve) => require([`@/pages/${view}`], resolve)
}

export default permission