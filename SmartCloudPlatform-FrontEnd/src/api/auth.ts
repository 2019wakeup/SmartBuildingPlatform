import request from './request'
import type { ApiResponse } from './types'

// 登录请求参数类型
export interface LoginRequest {
  account: string
  password: string
}

// 登录响应数据类型
export interface LoginResponse {
  token: string
  user: {
    userId: number
    userName: string
    account: string
    email?: string
    phone?: string
    roles?: string[]
  }
}

// 修改密码请求参数类型
export interface ChangePasswordRequest {
  oldPassword: string
  newPassword: string
}

// 用户登录
export function login(data: LoginRequest): Promise<ApiResponse> {
  return request.post('/auth/login', data)
}

// 用户登出
export function logout(): Promise<ApiResponse> {
  return request.post('/auth/logout')
}

// 获取当前用户信息
export function getUserInfo(): Promise<ApiResponse> {
  return request.get('/auth/userinfo')
}

// 修改密码
export function changePassword(data: ChangePasswordRequest): Promise<ApiResponse> {
  return request.post('/auth/changePassword', data)
} 