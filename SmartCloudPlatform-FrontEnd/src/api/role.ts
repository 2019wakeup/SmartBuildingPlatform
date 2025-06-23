import request from './request'
import type { Role, ApiResponse, PageQuery } from './types'

// 获取角色列表
export function getRoleList(params?: Partial<Role & PageQuery>): Promise<ApiResponse> {
  return request.get('/system/role/list', { params })
}

// 获取角色详情
export function getRoleInfo(roleId: number): Promise<ApiResponse> {
  return request.get(`/system/role/${roleId}`)
}

// 新增角色
export function addRole(data: Role): Promise<ApiResponse> {
  // 转换为蛇形命名法以匹配后端配置
  const submitData = {
    role_name: data.roleName,
    remark: data.remark
  }
  return request.post('/system/role', submitData)
}

// 修改角色
export function updateRole(data: Role): Promise<ApiResponse> {
  // 转换为蛇形命名法以匹配后端配置
  const submitData = {
    role_id: data.roleId,
    role_name: data.roleName,
    remark: data.remark
  }
  return request.put('/system/role', submitData)
}

// 删除角色
export function deleteRole(roleIds: number[]): Promise<ApiResponse> {
  return request.delete(`/system/role/${roleIds.join(',')}`)
}

// 获取角色选项
export function getRoleOptionSelect(): Promise<ApiResponse> {
  return request.get('/system/role/optionselect')
} 