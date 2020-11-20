import request from '@/utils/request'

// Query online user list
export function list(query) {
  return request({
    url:'/monitor/online/list',
    method:'get',
    params: query
  })
}

// Forcibly log out the user
export function forceLogout(tokenId) {
  return request({
    url:'/monitor/online/' + tokenId,
    method:'delete'
  })
}
