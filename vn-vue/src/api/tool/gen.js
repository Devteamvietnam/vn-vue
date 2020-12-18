import request from '@/utils/request'

// Query and generate table data
export function listTable(query) {
  return request({
    url: '/tool/gen/list',
    method: 'get',
    params: query
  })
}
// Query db database list
export function listDbTable(query) {
  return request({
    url: '/tool/gen/db/list',
    method: 'get',
    params: query
  })
}

// Query table details
export function getGenTable(tableId) {
  return request({
    url: '/tool/gen/' + tableId,
    method: 'get'
  })
}

// Modify code generation information
export function updateGenTable(data) {
  return request({
    url: '/tool/gen',
    method: 'put',
    data: data
  })
}

// Import table
export function importTable(data) {
  return request({
    url: '/tool/gen/importTable',
    method: 'post',
    params: data
  })
}

// Preview the generated code
export function previewTable(tableId) {
  return request({
    url: '/tool/gen/preview/' + tableId,
    method: 'get'
  })
}

// Delete table data
export function delTable(tableId) {
  return request({
    url: '/tool/gen/' + tableId,
    method: 'delete'
  })
}

// Generate code (custom path)
export function genCode(tableName) {
  return request({
    url: '/tool/gen/genCode/' + tableName,
    method: 'get'
  })
}

// Synchronize the database
export function synchDb(tableName) {
  return request({
    url: '/tool/gen/synchDb/' + tableName,
    method: 'get'
  })
}
