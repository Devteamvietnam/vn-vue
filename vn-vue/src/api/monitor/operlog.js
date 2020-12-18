import request from '@/utils/request'

// Query operation log list
export function list(query) {
  return request({
    url: '/monitor/operlog/list',
    method: 'get',
    params: query
  })
}

// Delete operation log
export function delOperlog(operId) {
  return request({
    url: '/monitor/operlog/' + operId,
    method: 'delete'
  })
}

// Clear operation log
export function cleanOperlog() {
  return request({
    url: '/monitor/operlog/clean',
    method: 'delete'
  })
}

// Export operation log
export function exportOperlog(query) {
  return request({
    url: '/monitor/operlog/export',
    method: 'get',
    params: query
  })
}
