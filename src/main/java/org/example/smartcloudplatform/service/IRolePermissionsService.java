package org.example.smartcloudplatform.service;

import org.example.smartcloudplatform.entity.RolePermissions;

import java.util.List;

/**
 * 角色权限关系 业务层
 * 
 * @author SmartCloudPlatform
 */
public interface IRolePermissionsService {
    /**
     * 根据角色ID查询角色权限关系列表
     * 
     * @param roleId 角色ID
     * @return 角色权限关系列表
     */
    List<RolePermissions> selectRolePermissionsByRoleId(Long roleId);

    /**
     * 根据权限ID查询角色权限关系列表
     * 
     * @param permissionId 权限ID
     * @return 角色权限关系列表
     */
    List<RolePermissions> selectRolePermissionsByPermissionId(Long permissionId);

    /**
     * 新增角色权限关系
     * 
     * @param rolePermissions 角色权限关系信息
     * @return 结果
     */
    int insertRolePermissions(RolePermissions rolePermissions);

    /**
     * 批量新增角色权限关系
     * 
     * @param roleId 角色ID
     * @param permissionIds 权限ID数组
     * @return 结果
     */
    int batchInsertRolePermissions(Long roleId, Long[] permissionIds);

    /**
     * 删除角色权限关系
     * 
     * @param roleId 角色ID
     * @param permissionId 权限ID
     * @return 结果
     */
    int deleteRolePermissions(Long roleId, Long permissionId);

    /**
     * 根据角色ID删除角色权限关系
     * 
     * @param roleId 角色ID
     * @return 结果
     */
    int deleteRolePermissionsByRoleId(Long roleId);

    /**
     * 根据权限ID删除角色权限关系
     * 
     * @param permissionId 权限ID
     * @return 结果
     */
    int deleteRolePermissionsByPermissionId(Long permissionId);

    /**
     * 批量删除角色权限关系
     * 
     * @param roleIds 角色ID数组
     * @return 结果
     */
    int deleteRolePermissionsByRoleIds(Long[] roleIds);
} 