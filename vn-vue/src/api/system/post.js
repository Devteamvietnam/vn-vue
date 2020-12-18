import request from '@/utils/request'

// Query job list
export function listPost(query) {
  return request({
    url: '/system/post/list',
    method: 'get',
    params: query
  })
}

// Query job details
export function getPost(postId) {
  return request({
    url: '/system/post/' + postId,
    method: 'get'
  })
}

// New position
export function addPost(data) {
  return request({
    url: '/system/post',
    method: 'post',
    data: data
  })
}

// modify position
export function updatePost(data) {
  return request({
    url: '/system/post',
    method: 'put',
    data: data
  })
}

// delete post
export function delPost(postId) {
  return request({
    url: '/system/post/' + postId,
    method: 'delete'
  })
}

// export position
export function exportPost(query) {
  return request({
    url: '/system/post/export',
    method: 'get',
    params: query
  })
}
