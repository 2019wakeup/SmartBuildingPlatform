package org.example.smartcloudplatform.service;

import org.example.smartcloudplatform.entity.Role;
import org.example.smartcloudplatform.mapper.RoleMapper;
import org.example.smartcloudplatform.service.impl.RoleServiceImpl;
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
 * 角色服务测试类
 */
@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock
    private RoleMapper roleMapper;

    @Mock
    private IRolePermissionsService rolePermissionsService;

    @InjectMocks
    private RoleServiceImpl roleService;

    private Role testRole;

    @BeforeEach
    void setUp() {
        testRole = new Role();
        testRole.setRoleId(1L);
        testRole.setRoleName("管理员");
        testRole.setUserId(1L);
        testRole.setPermissionId(1L);
    }

    @Test
    void testSelectRoleList() {
        // 准备测试数据
        List<Role> expectedRoles = Arrays.asList(testRole);
        when(roleMapper.selectRoleList(any(Role.class))).thenReturn(expectedRoles);

        // 执行测试
        List<Role> result = roleService.selectRoleList(new Role());

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("管理员", result.get(0).getRoleName());
        verify(roleMapper, times(1)).selectRoleList(any(Role.class));
    }

    @Test
    void testSelectRolesByUserId() {
        // 准备测试数据
        List<Role> expectedRoles = Arrays.asList(testRole);
        when(roleMapper.selectRolePermissionByUserId(1L)).thenReturn(expectedRoles);

        // 执行测试
        List<Role> result = roleService.selectRolesByUserId(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("管理员", result.get(0).getRoleName());
        verify(roleMapper, times(1)).selectRolePermissionByUserId(1L);
    }

    @Test
    void testSelectRoleAll() {
        // 准备测试数据
        Role userRole = new Role();
        userRole.setRoleId(2L);
        userRole.setRoleName("普通用户");
        List<Role> expectedRoles = Arrays.asList(testRole, userRole);
        when(roleMapper.selectRoleAll()).thenReturn(expectedRoles);

        // 执行测试
        List<Role> result = roleService.selectRoleAll();

        // 验证结果
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(roleMapper, times(1)).selectRoleAll();
    }

    @Test
    void testSelectRoleById() {
        // 准备测试数据
        when(roleMapper.selectRoleById(1L)).thenReturn(testRole);

        // 执行测试
        Role result = roleService.selectRoleById(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals(1L, result.getRoleId());
        assertEquals("管理员", result.getRoleName());
        verify(roleMapper, times(1)).selectRoleById(1L);
    }

    @Test
    void testSelectRolesByUserName() {
        // 准备测试数据
        List<Role> expectedRoles = Arrays.asList(testRole);
        when(roleMapper.selectRolesByUserName("admin")).thenReturn(expectedRoles);

        // 执行测试
        List<Role> result = roleService.selectRolesByUserName("admin");

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("管理员", result.get(0).getRoleName());
        verify(roleMapper, times(1)).selectRolesByUserName("admin");
    }

    @Test
    void testCheckRoleNameUnique_NewRole() {
        // 准备测试数据 - 新角色，角色名不存在
        Role newRole = new Role();
        newRole.setRoleName("新角色");
        when(roleMapper.checkRoleNameUnique("新角色")).thenReturn(null);

        // 执行测试
        boolean result = roleService.checkRoleNameUnique(newRole);

        // 验证结果
        assertTrue(result);
        verify(roleMapper, times(1)).checkRoleNameUnique("新角色");
    }

    @Test
    void testCheckRoleNameUnique_ExistingRole() {
        // 准备测试数据 - 角色名已存在
        Role existingRole = new Role();
        existingRole.setRoleName("管理员");
        when(roleMapper.checkRoleNameUnique("管理员")).thenReturn(testRole);

        // 执行测试
        boolean result = roleService.checkRoleNameUnique(existingRole);

        // 验证结果
        assertFalse(result);
        verify(roleMapper, times(1)).checkRoleNameUnique("管理员");
    }

    @Test
    void testInsertRole() {
        // 准备测试数据
        when(roleMapper.insertRole(any(Role.class))).thenReturn(1);

        // 执行测试
        int result = roleService.insertRole(testRole);

        // 验证结果
        assertEquals(1, result);
        verify(roleMapper, times(1)).insertRole(testRole);
    }

    @Test
    void testUpdateRole() {
        // 准备测试数据
        when(roleMapper.updateRole(any(Role.class))).thenReturn(1);

        // 执行测试
        int result = roleService.updateRole(testRole);

        // 验证结果
        assertEquals(1, result);
        verify(roleMapper, times(1)).updateRole(testRole);
    }

    @Test
    void testDeleteRoleById() {
        // 准备测试数据
        when(roleMapper.deleteRoleById(1L)).thenReturn(1);
        doNothing().when(rolePermissionsService).deleteRolePermissionsByRoleId(1L);

        // 执行测试
        int result = roleService.deleteRoleById(1L);

        // 验证结果
        assertEquals(1, result);
        verify(rolePermissionsService, times(1)).deleteRolePermissionsByRoleId(1L);
        verify(roleMapper, times(1)).deleteRoleById(1L);
    }

    @Test
    void testDeleteRoleByIds() {
        // 准备测试数据
        Long[] roleIds = {1L, 2L, 3L};
        when(roleMapper.deleteRoleByIds(roleIds)).thenReturn(3);
        doNothing().when(rolePermissionsService).deleteRolePermissionsByRoleIds(roleIds);

        // 执行测试
        int result = roleService.deleteRoleByIds(roleIds);

        // 验证结果
        assertEquals(3, result);
        verify(rolePermissionsService, times(1)).deleteRolePermissionsByRoleIds(roleIds);
        verify(roleMapper, times(1)).deleteRoleByIds(roleIds);
    }

    @Test
    void testInsertRoleAuth() {
        // 准备测试数据
        Long roleId = 1L;
        Long[] permissionIds = {1L, 2L, 3L};
        doNothing().when(rolePermissionsService).deleteRolePermissionsByRoleId(roleId);
        doNothing().when(rolePermissionsService).batchInsertRolePermissions(roleId, permissionIds);

        // 执行测试
        roleService.insertRoleAuth(roleId, permissionIds);

        // 验证结果
        verify(rolePermissionsService, times(1)).deleteRolePermissionsByRoleId(roleId);
        verify(rolePermissionsService, times(1)).batchInsertRolePermissions(roleId, permissionIds);
    }

    @Test
    void testInsertRoleAuth_EmptyPermissions() {
        // 准备测试数据
        Long roleId = 1L;
        Long[] permissionIds = {};
        doNothing().when(rolePermissionsService).deleteRolePermissionsByRoleId(roleId);

        // 执行测试
        roleService.insertRoleAuth(roleId, permissionIds);

        // 验证结果
        verify(rolePermissionsService, times(1)).deleteRolePermissionsByRoleId(roleId);
        verify(rolePermissionsService, never()).batchInsertRolePermissions(any(), any());
    }

    @Test
    void testInsertRoleAuth_NullPermissions() {
        // 准备测试数据
        Long roleId = 1L;
        Long[] permissionIds = null;
        doNothing().when(rolePermissionsService).deleteRolePermissionsByRoleId(roleId);

        // 执行测试
        roleService.insertRoleAuth(roleId, permissionIds);

        // 验证结果
        verify(rolePermissionsService, times(1)).deleteRolePermissionsByRoleId(roleId);
        verify(rolePermissionsService, never()).batchInsertRolePermissions(any(), any());
    }
} 