package org.example.smartcloudplatform.integration;

import org.example.smartcloudplatform.entity.User;
import org.example.smartcloudplatform.service.IUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 用户模块集成测试
 * 测试用户管理的完整业务流程
 */
@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
class UserIntegrationTest {

    @Autowired
    private IUserService userService;

    private static User testUser;

    @Test
    @Order(1)
    void testCreateUser() {
        // 创建测试用户
        testUser = new User();
        testUser.setUserName("集成测试用户");
        testUser.setAccount("integration_test");
        testUser.setEmail("integration@test.com");
        testUser.setPhone("13999999999");
        testUser.setPassword("test123456");

        // 验证用户名唯一性
        assertTrue(userService.checkUserNameUnique(testUser));
        assertTrue(userService.checkPhoneUnique(testUser));
        assertTrue(userService.checkEmailUnique(testUser));

        // 创建用户
        int result = userService.insertUser(testUser);
        assertEquals(1, result);
        
        // 验证用户创建成功
        User createdUser = userService.selectUserByAccount("integration_test");
        assertNotNull(createdUser);
        assertEquals("集成测试用户", createdUser.getUserName());
        assertEquals("integration_test", createdUser.getAccount());
        
        // 保存用户ID供后续测试使用
        testUser.setUserId(createdUser.getUserId());
    }

    @Test
    @Order(2)
    void testQueryUser() {
        // 根据ID查询用户
        User user = userService.selectUserById(testUser.getUserId());
        assertNotNull(user);
        assertEquals("集成测试用户", user.getUserName());

        // 根据用户名查询用户
        User userByName = userService.selectUserByUserName("集成测试用户");
        assertNotNull(userByName);
        assertEquals("integration_test", userByName.getAccount());

        // 根据账号查询用户
        User userByAccount = userService.selectUserByAccount("integration_test");
        assertNotNull(userByAccount);
        assertEquals("集成测试用户", userByAccount.getUserName());
    }

    @Test
    @Order(3)
    void testUpdateUser() {
        // 更新用户信息
        testUser.setUserName("更新后的用户名");
        testUser.setEmail("updated@test.com");
        
        int result = userService.updateUser(testUser);
        assertEquals(1, result);

        // 验证更新成功
        User updatedUser = userService.selectUserById(testUser.getUserId());
        assertNotNull(updatedUser);
        assertEquals("更新后的用户名", updatedUser.getUserName());
        assertEquals("updated@test.com", updatedUser.getEmail());
    }

    @Test
    @Order(4)
    void testUserValidation() {
        // 测试用户名重复验证
        User duplicateUser = new User();
        duplicateUser.setUserName("更新后的用户名");
        assertFalse(userService.checkUserNameUnique(duplicateUser));

        // 测试邮箱重复验证
        duplicateUser.setEmail("updated@test.com");
        assertFalse(userService.checkEmailUnique(duplicateUser));

        // 测试手机号重复验证
        duplicateUser.setPhone("13999999999");
        assertFalse(userService.checkPhoneUnique(duplicateUser));
    }

    @Test
    @Order(5)
    void testUserPermissions() {
        // 测试普通用户权限检查
        assertDoesNotThrow(() -> {
            userService.checkUserAllowed(testUser);
        });

        // 测试管理员用户权限检查
        User adminUser = new User();
        adminUser.setUserId(1L);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.checkUserAllowed(adminUser);
        });
        assertEquals("不允许操作超级管理员用户", exception.getMessage());
    }

    @Test
    @Order(6)
    void testUserRoleGroup() {
        // 测试用户角色组查询
        String roleGroup = userService.selectUserRoleGroup("integration_test");
        assertNotNull(roleGroup);
        assertEquals("管理员", roleGroup);
    }

    @Test
    @Order(7)
    void testPasswordOperations() {
        // 测试重置密码
        testUser.setPassword("newpassword123");
        int result = userService.resetPwd(testUser);
        assertEquals(1, result);

        // 测试用户注册
        User registerUser = new User();
        registerUser.setUserName("注册用户");
        registerUser.setAccount("register_test");
        registerUser.setEmail("register@test.com");
        registerUser.setPhone("13888888888");
        registerUser.setPassword("register123");
        
        boolean registerResult = userService.registerUser(registerUser);
        assertTrue(registerResult);
    }

    @Test
    @Order(8)
    void testDeleteUser() {
        // 删除单个用户
        int result = userService.deleteUserById(testUser.getUserId());
        assertEquals(1, result);

        // 验证用户已删除
        User deletedUser = userService.selectUserById(testUser.getUserId());
        assertNull(deletedUser);
    }

    @Test
    @Order(9)
    void testBatchOperations() {
        // 创建多个测试用户
        User user1 = new User();
        user1.setUserName("批量测试用户1");
        user1.setAccount("batch_test1");
        user1.setEmail("batch1@test.com");
        user1.setPhone("13777777777");
        user1.setPassword("batch123");
        userService.insertUser(user1);

        User user2 = new User();
        user2.setUserName("批量测试用户2");
        user2.setAccount("batch_test2");
        user2.setEmail("batch2@test.com");
        user2.setPhone("13666666666");
        user2.setPassword("batch123");
        userService.insertUser(user2);

        // 获取创建的用户ID
        User createdUser1 = userService.selectUserByAccount("batch_test1");
        User createdUser2 = userService.selectUserByAccount("batch_test2");
        
        // 批量删除用户
        Long[] userIds = {createdUser1.getUserId(), createdUser2.getUserId()};
        int deleteResult = userService.deleteUserByIds(userIds);
        assertEquals(2, deleteResult);

        // 验证用户已删除
        assertNull(userService.selectUserById(createdUser1.getUserId()));
        assertNull(userService.selectUserById(createdUser2.getUserId()));
    }
} 