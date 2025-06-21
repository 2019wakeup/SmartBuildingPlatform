package org.example.smartcloudplatform.service;

import org.example.smartcloudplatform.entity.User;
import org.example.smartcloudplatform.mapper.UserMapper;
import org.example.smartcloudplatform.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * 用户服务测试类
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUserId(1L);
        testUser.setUserName("测试用户");
        testUser.setAccount("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPhone("13888888888");
        testUser.setPassword("123456");
    }

    @Test
    void testSelectUserList() {
        // 准备测试数据
        List<User> expectedUsers = Arrays.asList(testUser);
        when(userMapper.selectUserList(any(User.class))).thenReturn(expectedUsers);

        // 执行测试
        List<User> result = userService.selectUserList(new User());

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("测试用户", result.get(0).getUserName());
        verify(userMapper, times(1)).selectUserList(any(User.class));
    }

    @Test
    void testSelectUserByUserName() {
        // 准备测试数据
        when(userMapper.selectUserByUserName("testuser")).thenReturn(testUser);

        // 执行测试
        User result = userService.selectUserByUserName("testuser");

        // 验证结果
        assertNotNull(result);
        assertEquals("测试用户", result.getUserName());
        assertEquals("testuser", result.getAccount());
        verify(userMapper, times(1)).selectUserByUserName("testuser");
    }

    @Test
    void testSelectUserById() {
        // 准备测试数据
        when(userMapper.selectUserById(1L)).thenReturn(testUser);

        // 执行测试
        User result = userService.selectUserById(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals(1L, result.getUserId());
        assertEquals("测试用户", result.getUserName());
        verify(userMapper, times(1)).selectUserById(1L);
    }

    @Test
    void testCheckUserNameUnique_NewUser() {
        // 准备测试数据 - 新用户，用户名不存在
        User newUser = new User();
        newUser.setUserName("newuser");
        when(userMapper.checkUserNameUnique("newuser")).thenReturn(null);

        // 执行测试
        boolean result = userService.checkUserNameUnique(newUser);

        // 验证结果
        assertTrue(result);
        verify(userMapper, times(1)).checkUserNameUnique("newuser");
    }

    @Test
    void testCheckUserNameUnique_ExistingUser() {
        // 准备测试数据 - 用户名已存在
        User existingUser = new User();
        existingUser.setUserName("testuser");
        when(userMapper.checkUserNameUnique("testuser")).thenReturn(testUser);

        // 执行测试
        boolean result = userService.checkUserNameUnique(existingUser);

        // 验证结果
        assertFalse(result);
        verify(userMapper, times(1)).checkUserNameUnique("testuser");
    }

    @Test
    void testCheckPhoneUnique_NewPhone() {
        // 准备测试数据 - 新手机号
        User newUser = new User();
        newUser.setPhone("13999999999");
        when(userMapper.checkPhoneUnique("13999999999")).thenReturn(null);

        // 执行测试
        boolean result = userService.checkPhoneUnique(newUser);

        // 验证结果
        assertTrue(result);
        verify(userMapper, times(1)).checkPhoneUnique("13999999999");
    }

    @Test
    void testCheckEmailUnique_NewEmail() {
        // 准备测试数据 - 新邮箱
        User newUser = new User();
        newUser.setEmail("new@example.com");
        when(userMapper.checkEmailUnique("new@example.com")).thenReturn(null);

        // 执行测试
        boolean result = userService.checkEmailUnique(newUser);

        // 验证结果
        assertTrue(result);
        verify(userMapper, times(1)).checkEmailUnique("new@example.com");
    }

    @Test
    void testInsertUser() {
        // 准备测试数据
        when(userMapper.insertUser(any(User.class))).thenReturn(1);

        // 执行测试
        int result = userService.insertUser(testUser);

        // 验证结果
        assertEquals(1, result);
        verify(userMapper, times(1)).insertUser(any(User.class));
        // 验证密码是否被加密
        assertNotEquals("123456", testUser.getPassword());
    }

    @Test
    void testRegisterUser() {
        // 准备测试数据
        when(userMapper.insertUser(any(User.class))).thenReturn(1);

        // 执行测试
        boolean result = userService.registerUser(testUser);

        // 验证结果
        assertTrue(result);
        verify(userMapper, times(1)).insertUser(any(User.class));
    }

    @Test
    void testUpdateUser() {
        // 准备测试数据
        when(userMapper.updateUser(any(User.class))).thenReturn(1);

        // 执行测试
        int result = userService.updateUser(testUser);

        // 验证结果
        assertEquals(1, result);
        verify(userMapper, times(1)).updateUser(testUser);
    }

    @Test
    void testDeleteUserById() {
        // 准备测试数据
        when(userMapper.deleteUserById(1L)).thenReturn(1);

        // 执行测试
        int result = userService.deleteUserById(1L);

        // 验证结果
        assertEquals(1, result);
        verify(userMapper, times(1)).deleteUserById(1L);
    }

    @Test
    void testDeleteUserByIds() {
        // 准备测试数据
        Long[] userIds = {1L, 2L, 3L};
        when(userMapper.deleteUserByIds(userIds)).thenReturn(3);

        // 执行测试
        int result = userService.deleteUserByIds(userIds);

        // 验证结果
        assertEquals(3, result);
        verify(userMapper, times(1)).deleteUserByIds(userIds);
    }

    @Test
    void testIsAdmin() {
        // 测试管理员用户
        assertTrue(User.isAdmin(1L));
        
        // 测试非管理员用户
        assertFalse(User.isAdmin(2L));
        assertFalse(User.isAdmin(null));
    }

    @Test
    void testCheckUserAllowed_AdminUser() {
        // 准备测试数据 - 管理员用户
        User adminUser = new User();
        adminUser.setUserId(1L);

        // 执行测试并验证异常
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.checkUserAllowed(adminUser);
        });

        assertEquals("不允许操作超级管理员用户", exception.getMessage());
    }

    @Test
    void testCheckUserAllowed_NormalUser() {
        // 准备测试数据 - 普通用户
        User normalUser = new User();
        normalUser.setUserId(2L);

        // 执行测试 - 不应该抛出异常
        assertDoesNotThrow(() -> {
            userService.checkUserAllowed(normalUser);
        });
    }

    @Test
    void testSelectUserRoleGroup() {
        // 执行测试
        String result = userService.selectUserRoleGroup("testuser");

        // 验证结果
        assertEquals("管理员", result);
    }
} 