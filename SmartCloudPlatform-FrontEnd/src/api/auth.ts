import request from './request'
import type { AjaxResult } from './types'

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
export function login(data: LoginRequest): Promise<AjaxResult> {
  return request({
    url: '/auth/login',
    method: 'post',
    data
  })
}

// 用户登出
export function logout(): Promise<AjaxResult> {
  return request({
    url: '/auth/logout',
    method: 'post'
  })
}

// 获取当前用户信息
export function getUserInfo(): Promise<AjaxResult> {
  return request({
    url: '/auth/userinfo',
    method: 'get'
  })
}

// 修改密码
export function changePassword(data: ChangePasswordRequest): Promise<AjaxResult> {
  return request({
    url: '/auth/changePassword',
    method: 'post',
    data
  })
} 