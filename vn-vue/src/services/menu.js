import request from '@/utils/request'

// Get routing
export const getRouters = () => {
  return request({
    url: '/getRouters',
    method: 'get'
  })
}