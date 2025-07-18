package org.example.smartcloudplatform.service;

import org.example.smartcloudplatform.entity.User;

import java.util.List;

/**
 * 用户 业务层
 * 
 * @author SmartCloudPlatform
 */
public interface IUserService {
    /**
     * 根据条件分页查询用户列表
     * 
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    List<User> selectUserList(User user);

    /**
     * 根据条件分页查询已分配用户角色列表
     * 
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    List<User> selectAllocatedList(User user);

    /**
     * 根据条件分页查询未分配用户角色列表
     * 
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    List<User> selectUnallocatedList(User user);

    /**
     * 通过用户名查询用户
     * 
     * @param userName 用户名
     * @return 用户对象信息
     */
    User selectUserByUserName(String userName);

    /**
     * 通过用户账号查询用户
     * 
     * @param account 账号
     * @return 用户对象信息
     */
    User selectUserByAccount(String account);

    /**
     * 通过用户ID查询用户
     * 
     * @param userId 用户ID
     * @return 用户对象信息
     */
    User selectUserById(Long userId);

    /**
     * 根据用户ID查询用户所属角色组
     * 
     * @param userName 用户名
     * @return 结果
     */
    String selectUserRoleGroup(String userName);

    /**
     * 校验用户名称是否唯一
     * 
     * @param user 用户信息
     * @return 结果
     */
    boolean checkUserNameUnique(User user);

    /**
     * 校验手机号码是否唯一
     * 
     * @param user 用户信息
     * @return 结果
     */
    boolean checkPhoneUnique(User user);

    /**
     * 校验email是否唯一
     * 
     * @param user 用户信息
     * @return 结果
     */
    boolean checkEmailUnique(User user);

    /**
     * 校验用户是否允许操作
     * 
     * @param user 用户信息
     */
    void checkUserAllowed(User user);

    /**
     * 校验用户是否有数据权限
     * 
     * @param userId 用户id
     */
    void checkUserDataScope(Long userId);

    /**
     * 新增用户信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    int insertUser(User user);

    /**
     * 注册用户信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    boolean registerUser(User user);

    /**
     * 修改用户信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    int updateUser(User user);

    /**
     * 用户授权角色
     * 
     * @param userId 用户ID
     * @param roleIds 角色组
     */
    void insertUserAuth(Long userId, Long[] roleIds);

    /**
     * 修改用户状态
     * 
     * @param user 用户信息
     * @return 结果
     */
    int updateUserStatus(User user);

    /**
     * 修改用户基本信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    int updateUserProfile(User user);

    /**
     * 修改用户头像
     * 
     * @param userId 用户ID
     * @param avatar 头像地址
     * @return 结果
     */
    boolean updateUserAvatar(Long userId, String avatar);

    /**
     * 重置用户密码
     * 
     * @param user 用户信息
     * @return 结果
     */
    int resetPwd(User user);

    /**
     * 重置用户密码
     * 
     * @param userName 用户名
     * @param password 密码
     * @return 结果
     */
    int resetUserPwd(String userName, String password);

    /**
     * 通过用户ID删除用户
     * 
     * @param userId 用户ID
     * @return 结果
     */
    int deleteUserById(Long userId);

    /**
     * 批量删除用户信息
     * 
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    int deleteUserByIds(Long[] userIds);

    /**
     * 导入用户数据
     * 
     * @param userList 用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importUser(List<User> userList, Boolean isUpdateSupport, String operName);

    /**
     * 用户登录验证
     * 
     * @param account 用户账号
     * @param password 用户密码
     * @return 用户信息，验证失败返回null
     */
    User loginUser(String account, String password);
} 