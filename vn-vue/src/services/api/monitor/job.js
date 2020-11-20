import request from '@/utils/request'

// Query the schedule of scheduled tasks
export function listJob(query) {
  return request({
    url:'/monitor/job/list',
    method:'get',
    params: query
  })
}

// Query timing task scheduling details
export function getJob(jobId) {
  return request({
    url:'/monitor/job/' + jobId,
    method:'get'
  })
}

// Add timing task scheduling
export function addJob(data) {
  return request({
    url:'/monitor/job',
    method:'post',
    data: data
  })
}

// Modify the scheduled task schedule
export function updateJob(data) {
  return request({
    url:'/monitor/job',
    method:'put',
    data: data
  })
}

// Delete the scheduled task schedule
export function delJob(jobId) {
  return request({
    url:'/monitor/job/' + jobId,
    method:'delete'
  })
}

// Export timing task schedule
export function exportJob(query) {
  return request({
    url:'/monitor/job/export',
    method:'get',
    params: query
  })
}

// Task status modification
export function changeJobStatus(jobId, status) {
  const data = {
    jobId,
    status
  }
  return request({
    url:'/monitor/job/changeStatus',
    method:'put',
    data: data
  })
}


// Timed tasks are executed immediately
export function runJob(jobId, jobGroup) {
  const data = {
    jobId,
    jobGroup
  }
  return request({
    url:'/monitor/job/run',
    method:'put',
    data: data
  })
}