import request from '@/utils/request'

// Query the announcement list
export function listNotice(query) {
  return request({
    url:'/system/notice/list',
    method:'get',
    params: query
  })
}

// Query announcement details
export function getNotice(noticeId) {
  return request({
    url:'/system/notice/' + noticeId,
    method:'get'
  })
}

// New announcement
export function addNotice(data) {
  return request({
    url:'/system/notice',
    method:'post',
    data: data
  })
}

// Modification announcement
export function updateNotice(data) {
  return request({
    url:'/system/notice',
    method:'put',
    data: data
  })
}

// delete announcement
export function delNotice(noticeId) {
  return request({
    url:'/system/notice/' + noticeId,
    method:'delete'
  })
}