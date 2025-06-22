import request from './request'
import type { User, AjaxResult, TableDataInfo, PageQuery } from './types'

// 获取用户列表
export function getUserList(params: User & PageQuery): Promise<TableDataInfo> {
  return request({
    url: '/system/user/list',
    method: 'get',
    params
  })
}

// 获取用户详情
export function getUserInfo(userId?: number): Promise<AjaxResult> {
  if (userId) {
    return request({
      url: `/system/user/${userId}`,
      method: 'get'
    })
  } else {
    return request({
      url: '/system/user/',
      method: 'get'
    })
  }
}

// 新增用户
export function addUser(data: User): Promise<AjaxResult> {
  return request({
    url: '/system/user',
    method: 'post',
    data
  })
}

// 修改用户
export function updateUser(data: User): Promise<AjaxResult> {
  return request({
    url: '/system/user',
    method: 'put',
    data
  })
}

// 删除用户
export function deleteUser(userIds: number[]): Promise<AjaxResult> {
  return request({
    url: `/system/user/${userIds.join(',')}`,
    method: 'delete'
  })
}

// 修改用户状态
export function changeUserStatus(data: User): Promise<AjaxResult> {
  return request({
    url: '/system/user/changeStatus',
    method: 'put',
    data
  })
}

// 重置密码
export function resetUserPwd(data: User): Promise<AjaxResult> {
  return request({
    url: '/system/user/resetPwd',
    method: 'put',
    data
  })
}

// 获取用户角色
export function getUserRole(userId: number): Promise<AjaxResult> {
  return request({
    url: `/system/user/authRole/${userId}`,
    method: 'get'
  })
}

// 用户授权角色
export function authUserRole(userId: number, roleIds: number[]): Promise<AjaxResult> {
  return request({
    url: `/system/user/authRole?userId=${userId}`,
    method: 'put',
    data: roleIds
  })
}

// 获取部门树
export function getDeptTree(): Promise<AjaxResult> {
  return request({
    url: '/system/user/deptTree',
    method: 'get'
  })
} 