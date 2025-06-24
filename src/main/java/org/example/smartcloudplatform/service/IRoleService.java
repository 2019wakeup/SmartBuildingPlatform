package org.example.smartcloudplatform.service;

import org.example.smartcloudplatform.entity.Role;

import java.util.List;

/**
 * 角色 业务层
 * 
 * @author SmartCloudPlatform
 */
public interface IRoleService {
    /**
     * 根据条件分页查询角色列表
     * 
     * @param role 角色信息
     * @return 角色信息集合信息
     */
    List<Role> selectRoleList(Role role);

    /**
     * 根据用户ID查询角色
     * 
     * @param userId 用户ID
     * @return 角色列表
     */
    List<Role> selectRolesByUserId(Long userId);

    /**
     * 查询所有角色
     * 
     * @return 角色列表
     */
    List<Role> selectRoleAll();

    /**
     * 通过角色ID查询角色
     * 
     * @param roleId 角色ID
     * @return 角色对象信息
     */
    Role selectRoleById(Long roleId);

    /**
     * 根据用户名查询角色
     * 
     * @param userName 用户名
     * @return 角色列表
     */
    List<Role> selectRolesByUserName(String userName);

    /**
     * 校验角色名称是否唯一
     * 
     * @param role 角色信息
     * @return 结果
     */
    boolean checkRoleNameUnique(Role role);

    /**
     * 新增角色信息
     * 
     * @param role 角色信息
     * @return 结果
     */
    int insertRole(Role role);

    /**
     * 修改角色信息
     * 
     * @param role 角色信息
     * @return 结果
     */
    int updateRole(Role role);

    /**
     * 通过角色ID删除角色
     * 
     * @param roleId 角色ID
     * @return 结果
     */
    int deleteRoleById(Long roleId);

    /**
     * 批量删除角色信息
     * 
     * @param roleIds 需要删除的角色ID
     * @return 结果
     */
    int deleteRoleByIds(Long[] roleIds);

    /**
     * 角色授权权限
     * 
     * @param roleId 角色ID
     * @param permissionIds 权限组
     */
    void insertRoleAuth(Long roleId, Long[] permissionIds);
} 