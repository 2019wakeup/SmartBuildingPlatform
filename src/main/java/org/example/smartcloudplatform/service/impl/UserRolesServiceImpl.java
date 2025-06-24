package org.example.smartcloudplatform.service.impl;

import org.example.smartcloudplatform.entity.UserRoles;
import org.example.smartcloudplatform.mapper.UserRolesMapper;
import org.example.smartcloudplatform.service.IUserRolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户角色关系 业务层处理
 * 
 * @author SmartCloudPlatform
 */
@Service
public class UserRolesServiceImpl implements IUserRolesService {
    @Autowired
    private UserRolesMapper userRolesMapper;

    /**
     * 根据用户ID查询用户角色关系列表
     * 
     * @param userId 用户ID
     * @return 用户角色关系列表
     */
    @Override
    public List<UserRoles> selectUserRolesByUserId(Long userId) {
        return userRolesMapper.selectUserRolesByUserId(userId);
    }

    /**
     * 根据角色ID查询用户角色关系列表
     * 
     * @param roleId 角色ID
     * @return 用户角色关系列表
     */
    @Override
    public List<UserRoles> selectUserRolesByRoleId(Long roleId) {
        return userRolesMapper.selectUserRolesByRoleId(roleId);
    }

    /**
     * 新增用户角色关系
     * 
     * @param userRoles 用户角色关系信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertUserRoles(UserRoles userRoles) {
        return userRolesMapper.insertUserRoles(userRoles);
    }

    /**
     * 批量新增用户角色关系
     * 
     * @param userId 用户ID
     * @param roleIds 角色ID数组
     * @return 结果
     */
    @Override
    @Transactional
    public int batchInsertUserRoles(Long userId, Long[] roleIds) {
        return userRolesMapper.batchInsertUserRoles(userId, roleIds);
    }

    /**
     * 删除用户角色关系
     * 
     * @param userId 用户ID
     * @param roleId 角色ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteUserRoles(Long userId, Long roleId) {
        return userRolesMapper.deleteUserRoles(userId, roleId);
    }

    /**
     * 根据用户ID删除用户角色关系
     * 
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteUserRolesByUserId(Long userId) {
        return userRolesMapper.deleteUserRolesByUserId(userId);
    }

    /**
     * 根据角色ID删除用户角色关系
     * 
     * @param roleId 角色ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteUserRolesByRoleId(Long roleId) {
        return userRolesMapper.deleteUserRolesByRoleId(roleId);
    }

    /**
     * 批量删除用户角色关系
     * 
     * @param userIds 用户ID数组
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteUserRolesByUserIds(Long[] userIds) {
        return userRolesMapper.deleteUserRolesByUserIds(userIds);
    }
} 