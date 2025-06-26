import axios from 'axios'
import { ElMessage } from 'element-plus'
import { getToken, clearAuth } from '@/utils/auth'
import router from '@/router'
import type { AjaxResult, ApiResponse } from './types'

const axiosInstance = axios.create({
  baseURL: '/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
axiosInstance.interceptors.request.use(
  (config) => {
    // 添加JWT token到请求头
    const token = getToken()
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 响应拦截器
axiosInstance.interceptors.response.use(
  (response) => {
    return response.data
  },
  (error) => {
    console.error('API请求错误:', error)
    
    if (error.response) {
      const { status, data } = error.response
      
      // Handle 401 Unauthorized error
      if (status === 401) {
        ElMessage.error('Login expired, please login again')
        clearAuth()
        router.push('/login')
        return Promise.reject(error)
      }
      
      // Handle 403 Forbidden error
      if (status === 403) {
        const errorMsg = data?.message || data?.msg || 'No permission to access this resource'
        ElMessage.error(errorMsg)
        console.error('403 error details:', data)
        return Promise.reject(new Error(errorMsg))
      }
      
      // Handle 400 error (usually validation error)
      if (status === 400) {
        const errorMsg = data?.message || data?.msg || 'Request parameter error'
        ElMessage.error(errorMsg)
        console.error('400 error details:', data)
        return Promise.reject(new Error(errorMsg))
      }
      
      // Handle other HTTP errors
      const errorMsg = data?.msg || data?.message || `Request failed: ${status}`
      ElMessage.error(errorMsg)
      return Promise.reject(new Error(errorMsg))
    } else {
      ElMessage.error(error.message || 'Network error')
    }
    
    return Promise.reject(error)
  }
)

// 封装请求方法，统一处理后端返回的数据格式
const request = {
  get: async (url: string, config?: any): Promise<ApiResponse> => {
    const response: AjaxResult = await axiosInstance.get(url, config)
    return handleResponse(response)
  },
  
  post: async (url: string, data?: any, config?: any): Promise<ApiResponse> => {
    const response: AjaxResult = await axiosInstance.post(url, data, config)
    return handleResponse(response)
  },
  
  put: async (url: string, data?: any, config?: any): Promise<ApiResponse> => {
    const response: AjaxResult = await axiosInstance.put(url, data, config)
    return handleResponse(response)
  },
  
  delete: async (url: string, config?: any): Promise<ApiResponse> => {
    const response: AjaxResult = await axiosInstance.delete(url, config)
    return handleResponse(response)
  }
}

// 统一处理后端响应
function handleResponse(response: any): ApiResponse {
  // 处理TableDataInfo格式 (用户列表等分页数据)
  if (response.total !== undefined && response.rows !== undefined && response.code !== undefined) {
    if (response.code !== 200) {
      ElMessage.error(response.msg || '请求失败')
      throw new Error(response.msg || '请求失败')
    }
    
    return {
      success: true,
      msg: response.msg,
      data: {
        total: response.total,
        rows: response.rows
      }
    }
  }
  
  // 处理标准AjaxResult格式: { code: 200, msg: "成功", data: {...} }
  if (response.code !== 200) {
    ElMessage.error(response.msg || '请求失败')
    throw new Error(response.msg || '请求失败')
  }
  
  return {
    success: true,
    msg: response.msg,
    data: response.data
  }
}

export default request 