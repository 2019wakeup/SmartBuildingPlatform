package org.example.smartcloudplatform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.smartcloudplatform.config.TestMybatisPlusConfig;
import org.example.smartcloudplatform.config.TestSecurityConfig;
import org.example.smartcloudplatform.controller.system.SysRoleController;
import org.example.smartcloudplatform.entity.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 角色控制器测试类
 */
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureWebMvc
@Import({TestMybatisPlusConfig.class, TestSecurityConfig.class})
class SysRoleControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    private Role testRole;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        testRole = new Role();
        testRole.setRoleId(1L);
        testRole.setRoleName("测试角色");
        testRole.setUserId(1L);
        testRole.setPermissionId(1L);
    }

    @Test
    void testGetRoleList() throws Exception {
        // 执行测试
        mockMvc.perform(get("/system/role/list")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("查询成功"))
                .andExpect(jsonPath("$.rows").isArray())
                .andExpect(jsonPath("$.rows[0].role_name").exists())
                .andExpect(jsonPath("$.total").exists());
    }

    @Test
    void testGetRoleInfo() throws Exception {
        // 执行测试
        mockMvc.perform(get("/system/role/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.role_id").value(1))
                .andExpect(jsonPath("$.data.role_name").exists());
    }

    @Test
    void testAddRole() throws Exception {
        // 执行测试
        mockMvc.perform(post("/system/role")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testRole)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("新增角色成功"));
    }

    @Test
    void testEditRole() throws Exception {
        // 执行测试
        mockMvc.perform(put("/system/role")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testRole)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("修改角色成功"));
    }

    @Test
    void testRemoveRole() throws Exception {
        // 执行测试
        mockMvc.perform(delete("/system/role/1,2,3")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("删除角色成功"));
    }

    @Test
    void testOptionSelect() throws Exception {
        // 执行测试
        mockMvc.perform(get("/system/role/optionselect")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].role_name").exists());
    }
} 