// 基础实体类型
export interface BaseEntity {
  searchValue?: string
  createBy?: string
  createTime?: string
  updateBy?: string
  updateTime?: string
  remark?: string
}

// 用户信息类型
export interface User extends BaseEntity {
  userId?: number
  userName: string
  account: string
  email?: string
  phone?: string
  roleId?: number
  sampleId?: number
  role?: Role
  breathSample?: BreathSample
  userRoles?: UserRoles[]
  admin?: boolean
}

// 角色信息类型
export interface Role extends BaseEntity {
  roleId?: number
  roleName: string
  userId?: number
  permissionId?: number
  user?: User
  permission?: Permission
  rolePermissions?: RolePermissions[]
  admin?: boolean
}

// 权限信息类型
export interface Permission extends BaseEntity {
  permissionId?: number
  code: string
  rolePermissions?: RolePermissions[]
}

// 用户角色关系类型
export interface UserRoles extends BaseEntity {
  userId: number
  roleId: number
  user?: User
  role?: Role
}

// 角色权限关系类型
export interface RolePermissions extends BaseEntity {
  roleId: number
  permissionId: number
  role?: Role
  permission?: Permission
}

// 呼吸样本类型
export interface BreathSample extends BaseEntity {
  sampleId?: number
  dataTaken: string
  userId: number
  user?: User
}

// API响应类型 - 匹配后端AjaxResult结构
export interface AjaxResult {
  code: number
  msg: string
  data?: any
}

// 前端统一响应类型
export interface ApiResponse {
  success: boolean
  msg: string
  data?: any
}

// 分页数据类型
export interface TableDataInfo {
  total: number
  rows: any[]
  code: number
  msg: string
}

// 分页查询参数
export interface PageQuery {
  pageNum?: number
  pageSize?: number
} 