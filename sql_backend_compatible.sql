-- 智能云平台数据库表结构 (与后端代码匹配版本)
-- 创建数据库
CREATE DATABASE IF NOT EXISTS cloudplatform DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE cloudplatform;

-- 删除现有外键约束（如果存在）
SET FOREIGN_KEY_CHECKS = 0;

-- 1. 权限表 (Permission) - 与后端@TableName("Permission")匹配
DROP TABLE IF EXISTS `Permission`;
CREATE TABLE `Permission` (
    `permissionID` BIGINT NOT NULL AUTO_INCREMENT COMMENT '权限唯一标识',
    `code` VARCHAR(50) NOT NULL UNIQUE COMMENT '权限代码',
    `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`permissionID`),
    INDEX `idx_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='权限信息表';

-- 2. 角色表 (Role) - 与后端@TableName("Role")匹配
DROP TABLE IF EXISTS `Role`;
CREATE TABLE `Role` (
    `roleID` BIGINT NOT NULL AUTO_INCREMENT COMMENT '角色唯一标识',
    `roleName` VARCHAR(50) NOT NULL COMMENT '角色名称',
    `userID` BIGINT COMMENT '关联到User表的外键',
    `permissionID` BIGINT COMMENT '关联到Permission表的外键',
    `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`roleID`),
    INDEX `idx_role_name` (`roleName`),
    INDEX `idx_user_id` (`userID`),
    INDEX `idx_permission_id` (`permissionID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色信息表';

-- 3. 用户表 (User) - 与后端@TableName("User")匹配
DROP TABLE IF EXISTS `User`;
CREATE TABLE `User` (
    `userID` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户唯一标识',
    `userName` VARCHAR(50) NOT NULL COMMENT '用户名称',
    `account` VARCHAR(50) NOT NULL UNIQUE COMMENT '账号',
    `password` VARCHAR(255) NOT NULL COMMENT '密码（BCrypt加密）',
    `email` VARCHAR(100) COMMENT '邮箱地址',
    `phone` VARCHAR(20) COMMENT '手机号',
    `roleID` BIGINT COMMENT '关联到Role表的外键',
    `sampleID` BIGINT COMMENT '关联到BreathSample表的外键',
    `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`userID`),
    INDEX `idx_account` (`account`),
    INDEX `idx_email` (`email`),
    INDEX `idx_phone` (`phone`),
    INDEX `idx_role_id` (`roleID`),
    INDEX `idx_sample_id` (`sampleID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户信息表';

-- 4. 呼吸样本表 (BreathSample) - 与后端@TableName("BreathSample")匹配
DROP TABLE IF EXISTS `BreathSample`;
CREATE TABLE `BreathSample` (
    `sampleID` BIGINT NOT NULL AUTO_INCREMENT COMMENT '样本唯一标识',
    `dataTaken` DATETIME NOT NULL COMMENT '样本采集时间',
    `userID` BIGINT NOT NULL COMMENT '关联到User表的外键',
    `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`sampleID`),
    INDEX `idx_user_id` (`userID`),
    INDEX `idx_data_taken` (`dataTaken`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='呼吸样本表';

-- 5. 用户角色关系表 (UserRoles) - 与后端@TableName("UserRoles")匹配
DROP TABLE IF EXISTS `UserRoles`;
CREATE TABLE `UserRoles` (
    `userID` BIGINT NOT NULL COMMENT '关联到User表的外键',
    `roleID` BIGINT NOT NULL COMMENT '关联到Role表的外键',
    `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`userID`, `roleID`),
    INDEX `idx_user_id` (`userID`),
    INDEX `idx_role_id` (`roleID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关系表';

-- 6. 角色权限关系表 (RolePermissions) - 与后端@TableName("RolePermissions")匹配
DROP TABLE IF EXISTS `RolePermissions`;
CREATE TABLE `RolePermissions` (
    `roleID` BIGINT NOT NULL COMMENT '关联到Role表的外键',
    `permissionID` BIGINT NOT NULL COMMENT '关联到Permission表的外键',
    `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`roleID`, `permissionID`),
    INDEX `idx_role_id` (`roleID`),
    INDEX `idx_permission_id` (`permissionID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色权限关系表';

-- 重新启用外键检查
SET FOREIGN_KEY_CHECKS = 1;

-- 插入示例数据
-- 权限数据
INSERT INTO `Permission` (`permissionID`, `code`, `create_by`) VALUES
(1, 'user:list', 'system'),
(2, 'user:add', 'system'),
(3, 'user:edit', 'system'),
(4, 'user:delete', 'system'),
(5, 'role:list', 'system'),
(6, 'role:add', 'system'),
(7, 'role:edit', 'system'),
(8, 'role:delete', 'system'),
(9, 'sample:list', 'system'),
(10, 'sample:add', 'system');

-- 角色数据（先不关联用户和权限，避免循环依赖）
INSERT INTO `Role` (`roleID`, `roleName`, `create_by`) VALUES
(1, '超级管理员', 'system'),
(2, '普通用户', 'system');

-- 用户数据（使用BCrypt加密的密码）
-- admin/123456 的BCrypt加密结果
-- user/123456 的BCrypt加密结果
INSERT INTO `User` (`userID`, `userName`, `account`, `password`, `email`, `phone`, `roleID`, `create_by`) VALUES
(1, '系统管理员', 'admin', '$2a$10$7JB720yubVSOfvVWHWK8VeKTfVCgR0AhMZUgLXjKQlRnxdJdYGOZK', 'admin@example.com', '13888888888', 1, 'system'),
(2, '普通用户', 'user', '$2a$10$7JB720yubVSOfvVWHWK8VeKTfVCgR0AhMZUgLXjKQlRnxdJdYGOZK', 'user@example.com', '13999999999', 2, 'system');

-- 呼吸样本数据
INSERT INTO `BreathSample` (`sampleID`, `dataTaken`, `userID`, `create_by`) VALUES
(1, '2024-01-01 10:00:00', 1, 'system'),
(2, '2024-01-02 11:30:00', 2, 'system');

-- 用户角色关系数据
INSERT INTO `UserRoles` (`userID`, `roleID`, `create_by`) VALUES
(1, 1, 'system'),
(2, 2, 'system');

-- 角色权限关系数据
INSERT INTO `RolePermissions` (`roleID`, `permissionID`, `create_by`) VALUES
(1, 1, 'system'),
(1, 2, 'system'),
(1, 3, 'system'),
(1, 4, 'system'),
(1, 5, 'system'),
(1, 6, 'system'),
(1, 7, 'system'),
(1, 8, 'system'),
(1, 9, 'system'),
(1, 10, 'system'),
(2, 9, 'system'),
(2, 10, 'system');

-- 查询验证数据
SELECT '=== 用户信息 ===' as info;
SELECT userID, userName, account, email, phone, roleID FROM `User`;

SELECT '=== 角色信息 ===' as info;
SELECT roleID, roleName FROM `Role`;

SELECT '=== 用户角色关系 ===' as info;
SELECT u.account, r.roleName 
FROM `User` u 
JOIN `UserRoles` ur ON u.userID = ur.userID 
JOIN `Role` r ON ur.roleID = r.roleID;

SELECT '=== 权限信息 ===' as info;
SELECT permissionID, code FROM `Permission`;

-- 验证登录用户（这个查询应该能找到admin用户）
SELECT '=== 登录验证测试 ===' as info;
SELECT userID, userName, account, '密码已加密' as password_status 
FROM `User` 
WHERE account = 'admin';

-- 说明：
-- 1. 表名使用大写，与后端@TableName注解匹配
-- 2. 字段名使用驼峰命名，与后端实体类字段匹配
-- 3. 密码使用BCrypt加密存储，与后端验证逻辑匹配
-- 4. admin和user账号的密码都是123456，已经过BCrypt加密 