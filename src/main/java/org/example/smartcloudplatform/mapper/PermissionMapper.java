package org.example.smartcloudplatform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.smartcloudplatform.entity.Permission;

import java.util.List;

/**
 * 权限表 数据层
 * 
 * @author SmartCloudPlatform
 */
@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {
    /**
     * 根据条件分页查询权限列表
     * 
     * @param permission 权限信息
     * @return 权限信息集合信息
     */
    List<Permission> selectPermissionList(Permission permission);

    /**
     * 根据角色ID查询权限
     * 
     * @param roleId 角色ID
     * @return 权限列表
     */
    List<Permission> selectPermissionsByRoleId(Long roleId);

    /**
     * 查询所有权限
     * 
     * @return 权限列表
     */
    List<Permission> selectPermissionAll();

    /**
     * 通过权限ID查询权限
     * 
     * @param permissionId 权限ID
     * @return 权限对象信息
     */
    Permission selectPermissionById(Long permissionId);

    /**
     * 根据权限代码查询权限
     * 
     * @param code 权限代码
     * @return 权限对象信息
     */
    Permission selectPermissionByCode(String code);

    /**
     * 校验权限代码是否唯一
     * 
     * @param code 权限代码
     * @return 权限信息
     */
    Permission checkPermissionCodeUnique(String code);

    /**
     * 新增权限信息
     * 
     * @param permission 权限信息
     * @return 结果
     */
    int insertPermission(Permission permission);

    /**
     * 修改权限信息
     * 
     * @param permission 权限信息
     * @return 结果
     */
    int updatePermission(Permission permission);

    /**
     * 通过权限ID删除权限
     * 
     * @param permissionId 权限ID
     * @return 结果
     */
    int deletePermissionById(Long permissionId);

    /**
     * 批量删除权限信息
     * 
     * @param permissionIds 需要删除的权限ID
     * @return 结果
     */
    int deletePermissionByIds(Long[] permissionIds);
} 