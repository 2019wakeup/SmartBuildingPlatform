package org.example.smartcloudplatform.service;

import org.example.smartcloudplatform.entity.UserRoles;

import java.util.List;

/**
 * 用户角色关系 业务层
 * 
 * @author SmartCloudPlatform
 */
public interface IUserRolesService {
    /**
     * 根据用户ID查询用户角色关系列表
     * 
     * @param userId 用户ID
     * @return 用户角色关系列表
     */
    List<UserRoles> selectUserRolesByUserId(Long userId);

    /**
     * 根据角色ID查询用户角色关系列表
     * 
     * @param roleId 角色ID
     * @return 用户角色关系列表
     */
    List<UserRoles> selectUserRolesByRoleId(Long roleId);

    /**
     * 新增用户角色关系
     * 
     * @param userRoles 用户角色关系信息
     * @return 结果
     */
    int insertUserRoles(UserRoles userRoles);

    /**
     * 批量新增用户角色关系
     * 
     * @param userId 用户ID
     * @param roleIds 角色ID数组
     * @return 结果
     */
    int batchInsertUserRoles(Long userId, Long[] roleIds);

    /**
     * 删除用户角色关系
     * 
     * @param userId 用户ID
     * @param roleId 角色ID
     * @return 结果
     */
    int deleteUserRoles(Long userId, Long roleId);

    /**
     * 根据用户ID删除用户角色关系
     * 
     * @param userId 用户ID
     * @return 结果
     */
    int deleteUserRolesByUserId(Long userId);

    /**
     * 根据角色ID删除用户角色关系
     * 
     * @param roleId 角色ID
     * @return 结果
     */
    int deleteUserRolesByRoleId(Long roleId);

    /**
     * 批量删除用户角色关系
     * 
     * @param userIds 用户ID数组
     * @return 结果
     */
    int deleteUserRolesByUserIds(Long[] userIds);
} 