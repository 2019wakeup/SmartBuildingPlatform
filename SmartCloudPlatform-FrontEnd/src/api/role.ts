import request from './request'
import type { Role, AjaxResult, TableDataInfo, PageQuery } from './types'

// 获取角色列表
export function getRoleList(params: Role & PageQuery): Promise<TableDataInfo> {
  return request({
    url: '/system/role/list',
    method: 'get',
    params
  })
}

// 获取角色详情
export function getRoleInfo(roleId: number): Promise<AjaxResult> {
  return request({
    url: `/system/role/${roleId}`,
    method: 'get'
  })
}

// 新增角色
export function addRole(data: Role): Promise<AjaxResult> {
  return request({
    url: '/system/role',
    method: 'post',
    data
  })
}

// 修改角色
export function updateRole(data: Role): Promise<AjaxResult> {
  return request({
    url: '/system/role',
    method: 'put',
    data
  })
}

// 删除角色
export function deleteRole(roleIds: number[]): Promise<AjaxResult> {
  return request({
    url: `/system/role/${roleIds.join(',')}`,
    method: 'delete'
  })
}

// 获取角色选项
export function getRoleOptionSelect(): Promise<AjaxResult> {
  return request({
    url: '/system/role/optionselect',
    method: 'get'
  })
} 