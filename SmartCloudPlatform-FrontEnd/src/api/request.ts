import axios from 'axios'
import { ElMessage } from 'element-plus'
import { getToken, clearAuth } from '@/utils/auth'
import router from '@/router'

const request = axios.create({
  baseURL: '/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
request.interceptors.request.use(
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
request.interceptors.response.use(
  (response) => {
    const { data } = response
    
    // 根据后端返回的AjaxResult结构处理
    if (data.success === false) {
      ElMessage.error(data.msg || '请求失败')
      return Promise.reject(new Error(data.msg || '请求失败'))
    }
    
    return data
  },
  (error) => {
    if (error.response) {
      const { status } = error.response
      
      // 处理401未授权错误
      if (status === 401) {
        ElMessage.error('登录已过期，请重新登录')
        clearAuth()
        router.push('/login')
        return Promise.reject(error)
      }
      
      // 处理403禁止访问错误
      if (status === 403) {
        ElMessage.error('没有权限访问该资源')
        return Promise.reject(error)
      }
      
      // 处理其他HTTP错误
      ElMessage.error(error.response.data?.msg || `请求失败: ${status}`)
    } else {
      ElMessage.error(error.message || '网络错误')
    }
    
    return Promise.reject(error)
  }
)

export default request 