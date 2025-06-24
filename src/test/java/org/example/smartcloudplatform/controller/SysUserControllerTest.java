package org.example.smartcloudplatform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.smartcloudplatform.config.TestMybatisPlusConfig;
import org.example.smartcloudplatform.config.TestSecurityConfig;
import org.example.smartcloudplatform.controller.system.SysUserController;
import org.example.smartcloudplatform.entity.User;
import org.example.smartcloudplatform.service.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 用户控制器测试类
 */
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureWebMvc
@Import({TestMybatisPlusConfig.class, TestSecurityConfig.class})
class SysUserControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private IUserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        testUser = new User();
        testUser.setUserId(1L);
        testUser.setUserName("测试用户");
        testUser.setAccount("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPhone("13888888888");
    }

    @Test
    void testGetUserList() throws Exception {
        // 准备测试数据
        List<User> users = Arrays.asList(testUser);
        when(userService.selectUserList(any(User.class))).thenReturn(users);

        // 执行测试
        mockMvc.perform(get("/system/user/list")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rows").isArray())
                .andExpect(jsonPath("$.rows[0].user_name").exists())
                .andExpect(jsonPath("$.rows[0].account").exists());

        verify(userService, times(1)).selectUserList(any(User.class));
    }

    @Test
    void testGetUserInfo() throws Exception {
        // 准备测试数据
        when(userService.selectUserById(1L)).thenReturn(testUser);

        // 执行测试
        mockMvc.perform(get("/system/user/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.user_name").exists())
                .andExpect(jsonPath("$.data.account").exists());

        verify(userService, times(1)).selectUserById(1L);
    }

    @Test
    void testGetUserInfo_WithoutUserId() throws Exception {
        // 执行测试 - 不传用户ID
        mockMvc.perform(get("/system/user/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(userService, never()).selectUserById(any());
    }

    @Test
    void testAddUser_Success() throws Exception {
        // 准备测试数据
        when(userService.checkUserNameUnique(any(User.class))).thenReturn(true);
        when(userService.checkPhoneUnique(any(User.class))).thenReturn(true);
        when(userService.checkEmailUnique(any(User.class))).thenReturn(true);
        when(userService.insertUser(any(User.class))).thenReturn(1);

        // 执行测试
        mockMvc.perform(post("/system/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(1));

        verify(userService, times(1)).checkUserNameUnique(any(User.class));
        verify(userService, times(1)).checkPhoneUnique(any(User.class));
        verify(userService, times(1)).checkEmailUnique(any(User.class));
        verify(userService, times(1)).insertUser(any(User.class));
    }

    @Test
    void testAddUser_DuplicateUserName() throws Exception {
        // 准备测试数据 - 用户名重复
        when(userService.checkUserNameUnique(any(User.class))).thenReturn(false);

        // 执行测试
        mockMvc.perform(post("/system/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.msg").value("新增用户'测试用户'失败，登录账号已存在"));

        verify(userService, times(1)).checkUserNameUnique(any(User.class));
        verify(userService, never()).insertUser(any(User.class));
    }

    @Test
    void testAddUser_DuplicatePhone() throws Exception {
        // 准备测试数据 - 手机号重复
        when(userService.checkUserNameUnique(any(User.class))).thenReturn(true);
        when(userService.checkPhoneUnique(any(User.class))).thenReturn(false);

        // 执行测试
        mockMvc.perform(post("/system/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.msg").value("新增用户'测试用户'失败，手机号码已存在"));

        verify(userService, times(1)).checkUserNameUnique(any(User.class));
        verify(userService, times(1)).checkPhoneUnique(any(User.class));
        verify(userService, never()).insertUser(any(User.class));
    }

    @Test
    void testEditUser_Success() throws Exception {
        // 准备测试数据
        when(userService.checkUserNameUnique(any(User.class))).thenReturn(true);
        when(userService.checkPhoneUnique(any(User.class))).thenReturn(true);
        when(userService.checkEmailUnique(any(User.class))).thenReturn(true);
        when(userService.updateUser(any(User.class))).thenReturn(1);
        doNothing().when(userService).checkUserAllowed(any(User.class));
        doNothing().when(userService).checkUserDataScope(any(Long.class));

        // 执行测试
        mockMvc.perform(put("/system/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(1));

        verify(userService, times(1)).checkUserAllowed(any(User.class));
        verify(userService, times(1)).checkUserDataScope(any(Long.class));
        verify(userService, times(1)).updateUser(any(User.class));
    }

    @Test
    void testRemoveUser_Success() throws Exception {
        // 准备测试数据
        Long[] userIds = {2L, 3L}; // 非管理员用户
        when(userService.deleteUserByIds(userIds)).thenReturn(2);

        // 执行测试
        mockMvc.perform(delete("/system/user/2,3")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(2));

        verify(userService, times(1)).deleteUserByIds(userIds);
    }

    @Test
    void testRemoveUser_AdminUser() throws Exception {
        // 执行测试 - 尝试删除管理员用户
        mockMvc.perform(delete("/system/user/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.msg").value("不能删除超级管理员用户"));

        verify(userService, never()).deleteUserByIds(any());
    }

    @Test
    void testResetPassword() throws Exception {
        // 准备测试数据
        when(userService.resetPwd(any(User.class))).thenReturn(1);
        doNothing().when(userService).checkUserAllowed(any(User.class));
        doNothing().when(userService).checkUserDataScope(any(Long.class));

        // 执行测试
        mockMvc.perform(put("/system/user/resetPwd")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(1));

        verify(userService, times(1)).checkUserAllowed(any(User.class));
        verify(userService, times(1)).checkUserDataScope(any(Long.class));
        verify(userService, times(1)).resetPwd(any(User.class));
    }

    @Test
    void testChangeStatus() throws Exception {
        // 准备测试数据
        when(userService.updateUserStatus(any(User.class))).thenReturn(1);
        doNothing().when(userService).checkUserAllowed(any(User.class));
        doNothing().when(userService).checkUserDataScope(any(Long.class));

        // 执行测试
        mockMvc.perform(put("/system/user/changeStatus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(1));

        verify(userService, times(1)).checkUserAllowed(any(User.class));
        verify(userService, times(1)).checkUserDataScope(any(Long.class));
        verify(userService, times(1)).updateUserStatus(any(User.class));
    }

    @Test
    void testAuthRole() throws Exception {
        // 准备测试数据
        when(userService.selectUserById(1L)).thenReturn(testUser);

        // 执行测试
        mockMvc.perform(get("/system/user/authRole/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.user").exists());

        verify(userService, times(1)).selectUserById(1L);
    }

    @Test
    void testInsertAuthRole() throws Exception {
        // 准备测试数据
        doNothing().when(userService).insertUserAuth(anyLong(), any(Long[].class));

        // 执行测试
        mockMvc.perform(put("/system/user/authRole")
                .param("userId", "1")
                .param("roleIds", "1,2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(userService, times(1)).insertUserAuth(eq(1L), any(Long[].class));
    }

    @Test
    void testDeptTree() throws Exception {
        // 执行测试
        mockMvc.perform(get("/system/user/deptTree")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }
} 