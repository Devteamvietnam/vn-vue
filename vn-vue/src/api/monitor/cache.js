import request from '@/utils/request'

// get Cache
export function getCache() {
  return request({
    url: '/monitor/cache',
    method: 'get'
  })
}
