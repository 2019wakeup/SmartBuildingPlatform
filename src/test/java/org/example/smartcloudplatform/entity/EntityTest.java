package org.example.smartcloudplatform.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 实体类测试
 */
class EntityTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testUserEntity() {
        // 测试User实体类的基本功能
        User user = new User();
        user.setUserId(1L);
        user.setUserName("测试用户");
        user.setAccount("testuser");
        user.setEmail("test@example.com");
        user.setPhone("13888888888");
        user.setPassword("123456");
        user.setRoleId(1L);
        user.setSampleId(1L);

        // 验证getter和setter
        assertEquals(1L, user.getUserId());
        assertEquals("测试用户", user.getUserName());
        assertEquals("testuser", user.getAccount());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("13888888888", user.getPhone());
        assertEquals("123456", user.getPassword());
        assertEquals(1L, user.getRoleId());
        assertEquals(1L, user.getSampleId());

        // 测试isAdmin方法
        assertTrue(user.isAdmin()); // userId为1L是管理员
        
        User normalUser = new User(2L);
        assertFalse(normalUser.isAdmin()); // userId为2L不是管理员
        
        // 测试静态方法
        assertTrue(User.isAdmin(1L));
        assertFalse(User.isAdmin(2L));
        assertFalse(User.isAdmin(null));
    }

    @Test
    void testUserValidation() {
        // 测试用户实体的验证注解
        User user = new User();
        
        // 测试空值验证
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.size() > 0);
        
        // 设置有效数据
        user.setUserName("有效用户名");
        user.setAccount("validaccount");
        user.setEmail("valid@example.com");
        user.setPhone("13888888888");
        
        violations = validator.validate(user);
        assertEquals(0, violations.size());
        
        // 测试无效邮箱格式
        user.setEmail("invalid-email");
        violations = validator.validate(user);
        assertTrue(violations.size() > 0);
        
        // 测试无效手机号格式
        user.setEmail("valid@example.com");
        user.setPhone("12345");
        violations = validator.validate(user);
        assertTrue(violations.size() > 0);
    }

    @Test
    void testRoleEntity() {
        // 测试Role实体类
        Role role = new Role();
        role.setRoleId(1L);
        role.setRoleName("管理员");
        role.setUserId(1L);
        role.setPermissionId(1L);

        // 验证getter和setter
        assertEquals(1L, role.getRoleId());
        assertEquals("管理员", role.getRoleName());
        assertEquals(1L, role.getUserId());
        assertEquals(1L, role.getPermissionId());

        // 测试构造方法
        Role roleWithId = new Role(2L);
        assertEquals(2L, roleWithId.getRoleId());
    }

    @Test
    void testBreathSampleEntity() {
        // 测试BreathSample实体类
        BreathSample sample = new BreathSample();
        sample.setSampleId(1L);
        sample.setUserId(1L);
        sample.setDataTaken(LocalDateTime.now());

        // 验证getter和setter
        assertEquals(1L, sample.getSampleId());
        assertEquals(1L, sample.getUserId());
        assertNotNull(sample.getDataTaken());

        // 测试构造方法
        BreathSample sampleWithId = new BreathSample(2L);
        assertEquals(2L, sampleWithId.getSampleId());
    }

    @Test
    void testBreathSampleValidation() {
        // 测试BreathSample实体的验证注解
        BreathSample sample = new BreathSample();
        
        // 测试空值验证
        Set<ConstraintViolation<BreathSample>> violations = validator.validate(sample);
        assertTrue(violations.size() > 0);
        
        // 设置有效数据
        sample.setDataTaken(LocalDateTime.now());
        sample.setUserId(1L);
        
        violations = validator.validate(sample);
        assertEquals(0, violations.size());
    }

    @Test
    void testPermissionEntity() {
        // 测试Permission实体类
        Permission permission = new Permission();
        permission.setPermissionId(1L);
        permission.setCode("user:list");

        // 验证getter和setter
        assertEquals(1L, permission.getPermissionId());
        assertEquals("user:list", permission.getCode());

        // 测试构造方法
        Permission permissionWithId = new Permission(2L);
        assertEquals(2L, permissionWithId.getPermissionId());
    }

    @Test
    void testUserRolesEntity() {
        // 测试UserRoles实体类
        UserRoles userRoles = new UserRoles();
        userRoles.setUserId(1L);
        userRoles.setRoleId(1L);

        // 验证getter和setter
        assertEquals(1L, userRoles.getUserId());
        assertEquals(1L, userRoles.getRoleId());

        // 测试构造方法
        UserRoles userRolesWithIds = new UserRoles(1L, 2L);
        assertEquals(1L, userRolesWithIds.getUserId());
        assertEquals(2L, userRolesWithIds.getRoleId());
    }

    @Test
    void testRolePermissionsEntity() {
        // 测试RolePermissions实体类
        RolePermissions rolePermissions = new RolePermissions();
        rolePermissions.setRoleId(1L);
        rolePermissions.setPermissionId(1L);

        // 验证getter和setter
        assertEquals(1L, rolePermissions.getRoleId());
        assertEquals(1L, rolePermissions.getPermissionId());

        // 测试构造方法
        RolePermissions rolePermissionsWithIds = new RolePermissions(1L, 2L);
        assertEquals(1L, rolePermissionsWithIds.getRoleId());
        assertEquals(2L, rolePermissionsWithIds.getPermissionId());
    }

    @Test
    void testEntityEqualsAndHashCode() {
        // 测试实体类的equals和hashCode方法（由Lombok生成）
        User user1 = new User();
        user1.setUserId(1L);
        user1.setUserName("测试用户");
        
        User user2 = new User();
        user2.setUserId(1L);
        user2.setUserName("测试用户");
        
        User user3 = new User();
        user3.setUserId(2L);
        user3.setUserName("其他用户");

        // 测试equals方法
        assertEquals(user1, user2);
        assertNotEquals(user1, user3);
        
        // 测试hashCode方法
        assertEquals(user1.hashCode(), user2.hashCode());
        assertNotEquals(user1.hashCode(), user3.hashCode());
    }

    @Test
    void testEntityToString() {
        // 测试实体类的toString方法（由Lombok生成）
        User user = new User();
        user.setUserId(1L);
        user.setUserName("测试用户");
        user.setAccount("testuser");

        String userString = user.toString();
        assertNotNull(userString);
        assertTrue(userString.contains("测试用户"));
        assertTrue(userString.contains("testuser"));
    }
} 