import request from './request'
import type { User, ApiResponse, PageQuery } from './types'

// 获取用户列表
export function getUserList(params?: Partial<User & PageQuery>): Promise<ApiResponse> {
  return request.get('/system/user/list', { params })
}

// 获取用户个人信息
export function getUserProfile(): Promise<ApiResponse> {
  return request.get('/system/user/profile')
}

// 修改用户个人信息
export function updateUserProfile(data: User): Promise<ApiResponse> {
  return request.put('/system/user/profile', data)
}

// 用户头像上传
export function uploadAvatar(data: FormData): Promise<ApiResponse> {
  return request.post('/system/user/avatar', data)
}

// 获取用户详情
export function getUserInfo(userId?: number): Promise<ApiResponse> {
  if (userId) {
    return request.get(`/system/user/${userId}`)
  } else {
    return request.get('/system/user/')
  }
}

// 新增用户
export function addUser(data: User): Promise<ApiResponse> {
  return request.post('/system/user', data)
}

// 修改用户
export function updateUser(data: User): Promise<ApiResponse> {
  return request.put('/system/user', data)
}

// 删除用户
export function deleteUser(userIds: number[]): Promise<ApiResponse> {
  return request.delete(`/system/user/${userIds.join(',')}`)
}

// 修改用户状态
export function changeUserStatus(data: User): Promise<ApiResponse> {
  return request.put('/system/user/changeStatus', data)
}

// 重置密码
export function resetUserPwd(data: User): Promise<ApiResponse> {
  return request.put('/system/user/resetPwd', data)
}

// 获取用户角色
export function getUserRole(userId: number): Promise<ApiResponse> {
  return request.get(`/system/user/authRole/${userId}`)
}

// 用户授权角色
export function authUserRole(userId: number, roleIds: number[]): Promise<ApiResponse> {
  return request.put(`/system/user/authRole?userId=${userId}`, roleIds)
}

// 获取部门树
export function getDeptTree(): Promise<ApiResponse> {
  return request.get('/system/user/deptTree')
}