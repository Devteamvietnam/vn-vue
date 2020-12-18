import request from '@/utils/request'

// Query parameter list
export function listConfig(query) {
  return request({
    url: '/system/config/list',
    method: 'get',
    params: query
  })
}

// Detailed query parameters
export function getConfig(configId) {
  return request({
    url: '/system/config/' + configId,
    method: 'get'
  })
}

// Query the parameter value according to the parameter key name
export function getConfigKey(configKey) {
  return request({
    url: '/system/config/configKey/' + configKey,
    method: 'get'
  })
}

// New parameter configuration
export function addConfig(data) {
  return request({
    url: '/system/config',
    method: 'post',
    data: data
  })
}

// Modify parameter configuration
export function updateConfig(data) {
  return request({
    url: '/system/config',
    method: 'put',
    data: data
  })
}

// Delete parameter configuration
export function delConfig(configId) {
  return request({
    url: '/system/config/' + configId,
    method: 'delete'
  })
}

// Clean up the parameter cache
export function clearCache() {
  return request({
    url: '/system/config/clearCache',
    method: 'delete'
  })
}

// Export parameters
export function exportConfig(query) {
  return request({
    url: '/system/config/export',
    method: 'get',
    params: query
  })
}
