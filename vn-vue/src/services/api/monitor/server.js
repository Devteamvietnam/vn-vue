import request from '@/utils/request'

// Query server details
export function getServer() {
  return request({
    url:'/monitor/server',
    method:'get'
  })
}