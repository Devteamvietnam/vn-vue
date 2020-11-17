import Home from '../pages/Home.vue'

const  routes = [
    {
    path: '/login',
    component: (resolve) => require(['@/pages/login'], resolve),
    hidden: true
    },
    {
      path: '/',
      name: 'Home',
      component: Home
    },
    {
      path: '/about',
      name: 'About',
      // route level code-splitting
      // this generates a separate chunk (about.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import(/* webpackChunkName: "about" */ '../pages/About.vue')
    }
  ]
  
  export default routes