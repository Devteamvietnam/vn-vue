import request from '@/utils/request'

// Query department list
export function listDeptWare(query) {
  return request({
    url:'/system/deptware/listware',
    method:'get',
    params: query
  })
}

// Query the department list (exclude nodes)
export function listDeptWareExcludeChild(deptId) {
  return request({
    url:'/system/deptware/listware/exclude/' + deptId,
    method:'get'
  })
}

// Query department details
export function getDeptWare(deptId) {
  return request({
    url:'/system/deptware/' + deptId,
    method:'get'
  })
}

// Query the department drop-down tree structure
export function treeselectWare() {
  return request({
    url:'/system/deptware/treeselectWare',
    method:'get'
  })
}

// Query the department tree structure based on the role ID
export function roleDeptWareTreeselect(roleId) {
  return request({
    url:'/system/deptware/roleDeptWareTreeselect/' + roleId,
    method:'get'
  })
}

// Add department
export function addDeptWare(data) {
  return request({
    url:'/system/deptware',
    method:'post',
    data: data
  })
}

// modify department
export function updateDeptWare(data) {
  return request({
    url:'/system/deptware',
    method:'put',
    data: data
  })
}

// delete department
export function delDeptWare(deptId) {
  return request({
    url:'/system/deptware/' + deptId,
    method:'delete'
  })
}