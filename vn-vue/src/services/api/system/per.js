import request from '@/utils/request'
import { praseStrEmpty } from "@/utils/devteam";

// Query user list
export function listUser(query) {
  return request({
    url:'/system/per/list',
    method:'get',
    params: query
  })
}

// Query user details
export function getUser(userId) {
  return request({
    url:'/system/per/' + praseStrEmpty(userId),
    method:'get'
  })
}

// New users
export function addUser(data) {
  return request({
    url:'/system/per',
    method:'post',
    data: data
  })
}

// modify user
export function updateUser(data) {
  return request({
    url:'/system/per',
    method:'put',
    data: data
  })
}

// delete users
export function delUser(userId) {
  return request({
    url:'/system/per/' + userId,
    method:'delete'
  })
}

// Export user
export function exportUser(query) {
  return request({
    url:'/system/per/export',
    method:'get',
    params: query
  })
}

// User password reset
export function resetUserPwd(userId, password) {
  const data = {
    userId,
    password
  }
  return request({
    url:'/system/per/resetPwd',
    method:'put',
    data: data
  })
}

// User status modification
export function changeUserStatus(userId, status) {
  const data = {
    userId,
    status
  }
  return request({
    url:'/system/per/changeStatus',
    method:'put',
    data: data
  })
}

// Query user personal information
export function getUserProfile() {
  return request({
    url:'/system/per/profile',
    method:'get'
  })
}

// Modify user personal information
export function updateUserProfile(data) {
  return request({
    url:'/system/per/profile',
    method:'put',
    data: data
  })
}

// User password reset
export function updateUserPwd(oldPassword, newPassword) {
  const data = {
    oldPassword,
    newPassword
  }
  return request({
    url:'/system/per/profile/updatePwd',
    method:'put',
    params: data
  })
}

// upload user avatar
export function uploadAvatar(data) {
  return request({
    url:'/system/per/profile/avatar',
    method:'post',
    data: data
  })
}

// Download user import template
export function importTemplate() {
  return request({
    url:'/system/per/importTemplate',
    method:'get'
  })
}
