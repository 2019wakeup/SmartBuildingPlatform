-- 智能云平台数据库表结构 (重构版本)
-- 创建数据库
CREATE DATABASE IF NOT EXISTS cloudplatform DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE cloudplatform;

-- 1. 用户表 (User)
DROP TABLE IF EXISTS `User`;
CREATE TABLE `User` (
    `userID` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户唯一标识',
    `userName` VARCHAR(50) NOT NULL COMMENT '用户名称',
    `account` VARCHAR(50) NOT NULL UNIQUE COMMENT '账号',
    `password` VARCHAR(255) NOT NULL COMMENT '密码',
    `email` VARCHAR(100) COMMENT '邮箱地址',
    `phone` VARCHAR(20) COMMENT '手机号',
    `roleID` BIGINT COMMENT '关联到Role表的外键',
    `sampleID` BIGINT COMMENT '关联到BreathSample表的外键，用于健康检测',
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

-- 2. 角色表 (Role)
DROP TABLE IF EXISTS `Role`;
CREATE TABLE `Role` (
    `roleID` BIGINT NOT NULL AUTO_INCREMENT COMMENT '角色唯一标识',
    `roleName` VARCHAR(50) NOT NULL COMMENT '角色名称',
    `userID` BIGINT COMMENT '关联到User表的外键',
    `permissionID` BIGINT COMMENT '关联到Permission表的外键，角色拥有权限',
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

-- 3. 呼吸样本表 (BreathSample)
DROP TABLE IF EXISTS `BreathSample`;
CREATE TABLE `BreathSample` (
    `sampleID` BIGINT NOT NULL AUTO_INCREMENT COMMENT '样本唯一标识',
    `dataTaken` DATETIME NOT NULL COMMENT '样本采集时间',
    `userID` BIGINT NOT NULL COMMENT '关联到User表的外键，用于追踪用户样本',
    `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`sampleID`),
    INDEX `idx_user_id` (`userID`),
    INDEX `idx_data_taken` (`dataTaken`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='呼吸样本表';

-- 4. 权限表 (Permission)
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

-- 5. 用户角色关系表 (UserRoles)
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

-- 6. 角色权限关系表 (RolePermissions)
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

-- 添加外键约束
ALTER TABLE `User` ADD CONSTRAINT `fk_user_role` FOREIGN KEY (`roleID`) REFERENCES `Role`(`roleID`) ON DELETE SET NULL;
ALTER TABLE `User` ADD CONSTRAINT `fk_user_sample` FOREIGN KEY (`sampleID`) REFERENCES `BreathSample`(`sampleID`) ON DELETE SET NULL;
ALTER TABLE `Role` ADD CONSTRAINT `fk_role_user` FOREIGN KEY (`userID`) REFERENCES `User`(`userID`) ON DELETE CASCADE;
ALTER TABLE `Role` ADD CONSTRAINT `fk_role_permission` FOREIGN KEY (`permissionID`) REFERENCES `Permission`(`permissionID`) ON DELETE CASCADE;
ALTER TABLE `BreathSample` ADD CONSTRAINT `fk_sample_user` FOREIGN KEY (`userID`) REFERENCES `User`(`userID`) ON DELETE CASCADE;
ALTER TABLE `UserRoles` ADD CONSTRAINT `fk_user_roles_user` FOREIGN KEY (`userID`) REFERENCES `User`(`userID`) ON DELETE CASCADE;
ALTER TABLE `UserRoles` ADD CONSTRAINT `fk_user_roles_role` FOREIGN KEY (`roleID`) REFERENCES `Role`(`roleID`) ON DELETE CASCADE;
ALTER TABLE `RolePermissions` ADD CONSTRAINT `fk_role_permissions_role` FOREIGN KEY (`roleID`) REFERENCES `Role`(`roleID`) ON DELETE CASCADE;
ALTER TABLE `RolePermissions` ADD CONSTRAINT `fk_role_permissions_permission` FOREIGN KEY (`permissionID`) REFERENCES `Permission`(`permissionID`) ON DELETE CASCADE;

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

-- 用户数据
INSERT INTO `User` (`userID`, `userName`, `account`, `password`, `email`, `phone`, `create_by`) VALUES
(1, '系统管理员', 'admin', '$2a$10$7JB720yubVSOfvVWHWK8VeKTfVCgR0AhMZUgLXjKQlRnxdJdYGOZK', 'admin@example.com', '13888888888', 'system'),
(2, '普通用户', 'user', '$2a$10$7JB720yubVSOfvVWHWK8VeKTfVCgR0AhMZUgLXjKQlRnxdJdYGOZK', 'user@example.com', '13999999999', 'system');

-- 角色数据
INSERT INTO `Role` (`roleID`, `roleName`, `userID`, `permissionID`, `create_by`) VALUES
(1, '超级管理员', 1, 1, 'system'),
(2, '普通用户', 2, 9, 'system');

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