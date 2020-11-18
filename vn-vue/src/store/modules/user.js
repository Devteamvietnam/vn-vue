import { login, logout, getInfo } from '@/services/api/login'
import { getToken, setToken, removeToken } from '@/utils/auth'

const user = {
  state: {
    token: getToken(),
    name: '',
    avatar: '',
    roles: [],
    permissions: []
  },

  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_NAME: (state, name) => {
      state.name = name
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
    },
    SET_PERMISSIONS: (state, permissions) => {
      state.permissions = permissions
    }
  },

  actions: {
  // log in
  Login({ commit }, userInfo) {
    const username = userInfo.username.trim()
    const password = userInfo.password
    const code = userInfo.code
    const uuid = userInfo.uuid
    return new Promise((resolve, reject) => {
      login(username, password, code, uuid).then(res => {
        setToken(res.token)
        commit('SET_TOKEN', res.token)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // Get user information
  GetInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      getInfo(state.token).then(res => {
        const user = res.user
        const avatar = user.avatar == ""? require("@/assets/image/avatar.jpg"): process.env.VUE_APP_BASE_API + user.avatar;
        if (res.roles && res.roles.length> 0) {// verify that the returned roles is a non-empty array
          commit('SET_ROLES', res.roles)
          commit('SET_PERMISSIONS', res.permissions)
        } else {
          commit('SET_ROLES', ['ROLE_DEFAULT'])
        }
        commit('SET_NAME', user.userName)
        commit('SET_AVATAR', avatar)
        resolve(res)
      }).catch(error => {
        reject(error)
      })
    })
  },
  
  // Exit system
  LogOut({ commit, state }) {
    return new Promise((resolve, reject) => {
      logout(state.token).then(() => {
        commit('SET_TOKEN','')
        commit('SET_ROLES', [])
        commit('SET_PERMISSIONS', [])
        removeToken()
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // Frontend logout
  FedLogOut({ commit }) {
    return new Promise(resolve => {
      commit('SET_TOKEN','')
      removeToken()
      resolve()
    })
  }
}
}

export default user
