import axios from 'axios'
import { Notification, MessageBox, Message } from 'element-ui'
import store from '@/store'
import { getToken } from '@/utils/auth'
import errorCode from '@/utils/errorCode'

axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8'
// Create axios instance
const service = axios.create({
  // The request is configured with the baseURL option in axios, which indicates the public part of the request URL
  baseURL: process.env.VUE_APP_BASE_API,
  // time out
  timeout: 10000
})
// request interceptor
service.interceptors.request.use(config => {
  // Do you need to set a token
  const isToken = (config.headers || {}).isToken === false
  if (getToken() && !isToken) {
    config.headers['Authorization'] = 'Bearer ' + getToken() // Let each request carry a custom token, please modify it according to the actual situation
  }
  // get request mapping params parameters
  if (config.method === 'get' && config.params) {
    let url = config.url + '?'
    for (const propName of Object.keys(config.params)) {
      const value = config.params[propName]
      var part = encodeURIComponent(propName) + '='
      if (value && typeof (value) !== 'undefined') {
        if (typeof value === 'object') {
          for (const key of Object.keys(value)) {
            const params = propName + '[' + key + ']'
            var subPart = encodeURIComponent(params) + '='
            url += subPart + encodeURIComponent(value[key]) + '&'
          }
        } else {
          url += part + encodeURIComponent(value) + '&'
        }
      }
    }
    url = url.slice(0, -1)
    config.params = {}
    config.url = url
  }
  return config
}, error => {
  console.log(error)
  Promise.reject(error)
})

// response interceptor
service.interceptors.response.use(res => {
  // If the status code is not set, the default success status is
  const code = res.data.code || 200
  // Get error information
  const msg = errorCode[code] || res.data.msg || errorCode['default']
  if (code === 401) {
    MessageBox.confirm('Login status has expired, you can stay on this page or log in again', 'System prompt', {
      confirmButtonText: 'Login again',
      cancelButtonText: 'Cancel',
      type: 'warning'
    }
    ).then(() => {
      store.dispatch('LogOut').then(() => {
        location.href = '/index'
      })
    })
  } else if (code === 500) {
    Message({
      message: msg,
      type: 'error'
    })
    return Promise.reject(new Error(msg))
  } else if (code !== 200) {
    Notification.error({
      title: msg
    })
    return Promise.reject('error')
  } else {
    return res.data
  }
},
error => {
  console.log('err' + error)
  let { message } = error
  if (message === 'Network Error') {
    message = 'Back-end interface connection is abnormal'
  } else if (message.includes('timeout')) {
    message = 'System interface request timeout'
  } else if (message.includes('Request failed with status code')) {
    message = 'System interface' + message.substr(message.length - 3) + 'Exception'
  }
  Message({
    message: message,
    type: 'error',
    duration: 5 * 1000
  })
  return Promise.reject(error)
}
)

export default service
