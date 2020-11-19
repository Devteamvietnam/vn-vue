import request from '@/utils/request'

// Query department list
export function listDept(query) {
  return request({
    url:'/system/dept/list',
    method:'get',
    params: query
  })
}

// Query the department list (exclude nodes)
export function listDeptExcludeChild(deptId) {
  return request({
    url:'/system/dept/list/exclude/' + deptId,
    method:'get'
  })
}

// Query department details
export function getDept(deptId) {
  return request({
    url:'/system/dept/' + deptId,
    method:'get'
  })
}

// Query the department drop-down tree structure
export function treeselect() {
  return request({
    url:'/system/dept/treeselect',
    method:'get'
  })
}

// Query the department tree structure based on the role ID
export function roleDeptTreeselect(roleId) {
  return request({
    url:'/system/dept/roleDeptTreeselect/' + roleId,
    method:'get'
  })
}

// Add department
export function addDept(data) {
  return request({
    url:'/system/dept',
    method:'post',
    data: data
  })
}

// modify department
export function updateDept(data) {
  return request({
    url:'/system/dept',
    method:'put',
    data: data
  })
}

// delete department
export function delDept(deptId) {
  return request({
    url:'/system/dept/' + deptId,
    method:'delete'
  })
}