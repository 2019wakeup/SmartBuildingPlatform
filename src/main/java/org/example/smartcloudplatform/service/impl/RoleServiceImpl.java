package org.example.smartcloudplatform.service.impl;

import org.example.smartcloudplatform.entity.Role;
import org.example.smartcloudplatform.mapper.RoleMapper;
import org.example.smartcloudplatform.service.IRoleService;
import org.example.smartcloudplatform.service.IRolePermissionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 角色 业务层处理
 * 
 * @author SmartCloudPlatform
 */
@Service
public class RoleServiceImpl implements IRoleService {
    @Autowired
    private RoleMapper roleMapper;
    
    @Autowired
    private IRolePermissionsService rolePermissionsService;

    /**
     * 根据条件分页查询角色列表
     * 
     * @param role 角色信息
     * @return 角色信息集合信息
     */
    @Override
    public List<Role> selectRoleList(Role role) {
        return roleMapper.selectRoleList(role);
    }

    /**
     * 根据用户ID查询角色
     * 
     * @param userId 用户ID
     * @return 角色列表
     */
    @Override
    public List<Role> selectRolesByUserId(Long userId) {
        return roleMapper.selectRolePermissionByUserId(userId);
    }

    /**
     * 查询所有角色
     * 
     * @return 角色列表
     */
    @Override
    public List<Role> selectRoleAll() {
        return roleMapper.selectRoleAll();
    }

    /**
     * 通过角色ID查询角色
     * 
     * @param roleId 角色ID
     * @return 角色对象信息
     */
    @Override
    public Role selectRoleById(Long roleId) {
        return roleMapper.selectRoleById(roleId);
    }

    /**
     * 根据用户名查询角色
     * 
     * @param userName 用户名
     * @return 角色列表
     */
    @Override
    public List<Role> selectRolesByUserName(String userName) {
        return roleMapper.selectRolesByUserName(userName);
    }

    /**
     * 校验角色名称是否唯一
     * 
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public boolean checkRoleNameUnique(Role role) {
        Long roleId = role.getRoleId() == null ? -1L : role.getRoleId();
        Role info = roleMapper.checkRoleNameUnique(role.getRoleName());
        return info == null || info.getRoleId().equals(roleId);
    }

    /**
     * 新增角色信息
     * 
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertRole(Role role) {
        return roleMapper.insertRole(role);
    }

    /**
     * 修改角色信息
     * 
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateRole(Role role) {
        return roleMapper.updateRole(role);
    }

    /**
     * 通过角色ID删除角色
     * 
     * @param roleId 角色ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteRoleById(Long roleId) {
        // 删除角色时同时删除角色权限关系
        rolePermissionsService.deleteRolePermissionsByRoleId(roleId);
        return roleMapper.deleteRoleById(roleId);
    }

    /**
     * 批量删除角色信息
     * 
     * @param roleIds 需要删除的角色ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteRoleByIds(Long[] roleIds) {
        // 删除角色时同时删除角色权限关系
        rolePermissionsService.deleteRolePermissionsByRoleIds(roleIds);
        return roleMapper.deleteRoleByIds(roleIds);
    }

    /**
     * 角色授权权限
     * 
     * @param roleId 角色ID
     * @param permissionIds 权限组
     */
    @Override
    @Transactional
    public void insertRoleAuth(Long roleId, Long[] permissionIds) {
        // 先删除原有的角色权限关系
        rolePermissionsService.deleteRolePermissionsByRoleId(roleId);
        // 重新插入角色权限关系
        if (permissionIds != null && permissionIds.length > 0) {
            rolePermissionsService.batchInsertRolePermissions(roleId, permissionIds);
        }
    }
} 