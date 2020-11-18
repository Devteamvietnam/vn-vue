import request from '@/utils/request'

// Login method
export function login(username, password, code, uuid) {
  const data = {
    username,
    password,
    code,
    uuid
  }
  return request({
    url:'/login',
    method:'post',
    data: data
  })
}

// Get user details
export function getInfo() {
  return request({
    url:'/getInfo',
    method:'get'
  })
}

// Exit method
export function logout() {
  return request({
    url:'/logout',
    method:'post'
  })
}

// get verification code
export function getCodeImg() {
  return request({
    url:'/captchaImage',
    method:'get'
  })
}