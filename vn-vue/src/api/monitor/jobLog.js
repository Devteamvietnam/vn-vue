import request from '@/utils/request'

// Query the schedule log list
export function listJobLog(query) {
  return request({
    url: '/monitor/jobLog/list',
    method: 'get',
    params: query
  })
}

// Delete scheduling log
export function delJobLog(jobLogId) {
  return request({
    url: '/monitor/jobLog/' + jobLogId,
    method: 'delete'
  })
}

// Air conditioning log
export function cleanJobLog() {
  return request({
    url: '/monitor/jobLog/clean',
    method: 'delete'
  })
}

// Export scheduling log
export function exportJobLog(query) {
  return request({
    url: '/monitor/jobLog/export',
    method: 'get',
    params: query
  })
}
