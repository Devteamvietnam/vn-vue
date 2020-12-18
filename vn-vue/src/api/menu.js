import request from '@/utils/request'

// get router
export const getRouters = () => {
  return request({
    url: '/getRouters',
    method: 'get'
  })
}
