import request from '@/utils/request'

// Query role list
export function listRole(query) {
  return request({
    url:'/system/role/list',
    method:'get',
    params: query
  })
}

// Query role details
export function getRole(roleId) {
  return request({
    url:'/system/role/' + roleId,
    method:'get'
  })
}

// New role
export function addRole(data) {
  return request({
    url:'/system/role',
    method:'post',
    data: data
  })
}

// modify role
export function updateRole(data) {
  return request({
    url:'/system/role',
    method:'put',
    data: data
  })
}

// role data permissions
export function dataScope(data) {
  return request({
    url:'/system/role/dataScope',
    method:'put',
    data: data
  })
}

// role status modification
export function changeRoleStatus(roleId, status) {
  const data = {
    roleId,
    status
  }
  return request({
    url:'/system/role/changeStatus',
    method:'put',
    data: data
  })
}

// delete role
export function delRole(roleId) {
  return request({
    url:'/system/role/' + roleId,
    method:'delete'
  })
}

// Export role
export function exportRole(query) {
  return request({
    url:'/system/role/export',
    method:'get',
    params: query
  })
}