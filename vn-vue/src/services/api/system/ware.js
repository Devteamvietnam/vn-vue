import request from '@/utils/request'

// Query job list
export function listWare(query) {
  return request({
    url:'/system/ware/list',
    method:'get',
    params: query
  })
}

// Query job details
export function getWare(wareId) {
  return request({
    url:'/system/ware/' + wareId,
    method:'get'
  })
}

// New position
export function addWare(data) {
  return request({
    url:'/system/ware',
    method:'post',
    data: data
  })
}

// modify position
export function updateWare(data) {
  return request({
    url:'/system/ware',
    method:'put',
    data: data
  })
}

// delete post
export function delWare(wareId) {
  return request({
    url:'/system/ware/' + wareId,
    method:'delete'
  })
}

// export position
export function exportWare(query) {
  return request({
    url:'/system/ware/export',
    method:'get',
    params: query
  })
}