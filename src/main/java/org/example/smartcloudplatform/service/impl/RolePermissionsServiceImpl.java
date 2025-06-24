package org.example.smartcloudplatform.service.impl;

import org.example.smartcloudplatform.entity.RolePermissions;
import org.example.smartcloudplatform.mapper.RolePermissionsMapper;
import org.example.smartcloudplatform.service.IRolePermissionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 角色权限关系 业务层处理
 * 
 * @author SmartCloudPlatform
 */
@Service
public class RolePermissionsServiceImpl implements IRolePermissionsService {
    @Autowired
    private RolePermissionsMapper rolePermissionsMapper;

    /**
     * 根据角色ID查询角色权限关系列表
     * 
     * @param roleId 角色ID
     * @return 角色权限关系列表
     */
    @Override
    public List<RolePermissions> selectRolePermissionsByRoleId(Long roleId) {
        return rolePermissionsMapper.selectRolePermissionsByRoleId(roleId);
    }

    /**
     * 根据权限ID查询角色权限关系列表
     * 
     * @param permissionId 权限ID
     * @return 角色权限关系列表
     */
    @Override
    public List<RolePermissions> selectRolePermissionsByPermissionId(Long permissionId) {
        return rolePermissionsMapper.selectRolePermissionsByPermissionId(permissionId);
    }

    /**
     * 新增角色权限关系
     * 
     * @param rolePermissions 角色权限关系信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertRolePermissions(RolePermissions rolePermissions) {
        return rolePermissionsMapper.insertRolePermissions(rolePermissions);
    }

    /**
     * 批量新增角色权限关系
     * 
     * @param roleId 角色ID
     * @param permissionIds 权限ID数组
     * @return 结果
     */
    @Override
    @Transactional
    public int batchInsertRolePermissions(Long roleId, Long[] permissionIds) {
        return rolePermissionsMapper.batchInsertRolePermissions(roleId, permissionIds);
    }

    /**
     * 删除角色权限关系
     * 
     * @param roleId 角色ID
     * @param permissionId 权限ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteRolePermissions(Long roleId, Long permissionId) {
        return rolePermissionsMapper.deleteRolePermissions(roleId, permissionId);
    }

    /**
     * 根据角色ID删除角色权限关系
     * 
     * @param roleId 角色ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteRolePermissionsByRoleId(Long roleId) {
        return rolePermissionsMapper.deleteRolePermissionsByRoleId(roleId);
    }

    /**
     * 根据权限ID删除角色权限关系
     * 
     * @param permissionId 权限ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteRolePermissionsByPermissionId(Long permissionId) {
        return rolePermissionsMapper.deleteRolePermissionsByPermissionId(permissionId);
    }

    /**
     * 批量删除角色权限关系
     * 
     * @param roleIds 角色ID数组
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteRolePermissionsByRoleIds(Long[] roleIds) {
        return rolePermissionsMapper.deleteRolePermissionsByRoleIds(roleIds);
    }
} 