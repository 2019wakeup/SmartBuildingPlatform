package org.example.smartcloudplatform.service.impl;

import org.example.smartcloudplatform.entity.Permission;
import org.example.smartcloudplatform.mapper.PermissionMapper;
import org.example.smartcloudplatform.service.IPermissionService;
import org.example.smartcloudplatform.service.IRolePermissionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 权限 业务层处理
 * 
 * @author SmartCloudPlatform
 */
@Service
public class PermissionServiceImpl implements IPermissionService {
    @Autowired
    private PermissionMapper permissionMapper;
    
    @Autowired
    private IRolePermissionsService rolePermissionsService;

    /**
     * 根据条件分页查询权限列表
     * 
     * @param permission 权限信息
     * @return 权限信息集合信息
     */
    @Override
    public List<Permission> selectPermissionList(Permission permission) {
        return permissionMapper.selectPermissionList(permission);
    }

    /**
     * 根据角色ID查询权限
     * 
     * @param roleId 角色ID
     * @return 权限列表
     */
    @Override
    public List<Permission> selectPermissionsByRoleId(Long roleId) {
        return permissionMapper.selectPermissionsByRoleId(roleId);
    }

    /**
     * 查询所有权限
     * 
     * @return 权限列表
     */
    @Override
    public List<Permission> selectPermissionAll() {
        return permissionMapper.selectPermissionAll();
    }

    /**
     * 通过权限ID查询权限
     * 
     * @param permissionId 权限ID
     * @return 权限对象信息
     */
    @Override
    public Permission selectPermissionById(Long permissionId) {
        return permissionMapper.selectPermissionById(permissionId);
    }

    /**
     * 根据权限代码查询权限
     * 
     * @param code 权限代码
     * @return 权限对象信息
     */
    @Override
    public Permission selectPermissionByCode(String code) {
        return permissionMapper.selectPermissionByCode(code);
    }

    /**
     * 校验权限代码是否唯一
     * 
     * @param permission 权限信息
     * @return 结果
     */
    @Override
    public boolean checkPermissionCodeUnique(Permission permission) {
        Long permissionId = permission.getPermissionId() == null ? -1L : permission.getPermissionId();
        Permission info = permissionMapper.checkPermissionCodeUnique(permission.getCode());
        return info == null || info.getPermissionId().equals(permissionId);
    }

    /**
     * 新增权限信息
     * 
     * @param permission 权限信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertPermission(Permission permission) {
        return permissionMapper.insertPermission(permission);
    }

    /**
     * 修改权限信息
     * 
     * @param permission 权限信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updatePermission(Permission permission) {
        return permissionMapper.updatePermission(permission);
    }

    /**
     * 通过权限ID删除权限
     * 
     * @param permissionId 权限ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deletePermissionById(Long permissionId) {
        // 删除权限时同时删除角色权限关系
        rolePermissionsService.deleteRolePermissionsByPermissionId(permissionId);
        return permissionMapper.deletePermissionById(permissionId);
    }

    /**
     * 批量删除权限信息
     * 
     * @param permissionIds 需要删除的权限ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deletePermissionByIds(Long[] permissionIds) {
        // 删除权限时同时删除角色权限关系
        for (Long permissionId : permissionIds) {
            rolePermissionsService.deleteRolePermissionsByPermissionId(permissionId);
        }
        return permissionMapper.deletePermissionByIds(permissionIds);
    }
} 