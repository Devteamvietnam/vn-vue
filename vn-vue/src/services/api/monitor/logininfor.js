import request from '@/utils/request'

// Query log list
export function list(query) {
  return request({
    url:'/monitor/logininfor/list',
    method:'get',
    params: query
  })
}

// Delete login log
export function delLogininfor(infoId) {
  return request({
    url:'/monitor/logininfor/' + infoId,
    method:'delete'
  })
}

// Clear login log
export function cleanLogininfor() {
  return request({
    url:'/monitor/logininfor/clean',
    method:'delete'
  })
}

// Export login log
export function exportLogininfor(query) {
  return request({
    url:'/monitor/logininfor/export',
    method:'get',
    params: query
  })
}