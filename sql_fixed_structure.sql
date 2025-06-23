-- 智能云平台数据库表结构 (修正版本)
-- 创建数据库
CREATE DATABASE IF NOT EXISTS cloudplatform DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE cloudplatform;

-- 1. 权限表 (Permission) - 优先创建，避免外键依赖
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission` (
    `permission_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '权限唯一标识',
    `code` VARCHAR(50) NOT NULL UNIQUE COMMENT '权限代码',
    `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`permission_id`),
    INDEX `idx_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='权限信息表';

-- 2. 角色表 (Role) - 移除对User的外键依赖
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
    `role_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '角色唯一标识',
    `role_name` VARCHAR(50) NOT NULL COMMENT '角色名称',
    `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`role_id`),
    INDEX `idx_role_name` (`role_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色信息表';

-- 3. 用户表 (User) - 移除对BreathSample的外键依赖
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
    `user_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户唯一标识',
    `user_name` VARCHAR(50) NOT NULL COMMENT '用户名称',
    `account` VARCHAR(50) NOT NULL UNIQUE COMMENT '账号',
    `password` VARCHAR(255) NOT NULL COMMENT '密码',
    `email` VARCHAR(100) COMMENT '邮箱地址',
    `phone` VARCHAR(20) COMMENT '手机号',
    `status` CHAR(1) DEFAULT '0' COMMENT '用户状态（0正常 1停用）',
    `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`user_id`),
    INDEX `idx_account` (`account`),
    INDEX `idx_email` (`email`),
    INDEX `idx_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户信息表';

-- 4. 呼吸样本表 (BreathSample)
DROP TABLE IF EXISTS `breath_sample`;
CREATE TABLE `breath_sample` (
    `sample_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '样本唯一标识',
    `data_taken` DATETIME NOT NULL COMMENT '样本采集时间',
    `user_id` BIGINT NOT NULL COMMENT '关联到User表的外键，用于追踪用户样本',
    `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`sample_id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_data_taken` (`data_taken`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='呼吸样本表';

-- 5. 用户角色关系表 (UserRoles)
DROP TABLE IF EXISTS `user_roles`;
CREATE TABLE `user_roles` (
    `user_id` BIGINT NOT NULL COMMENT '关联到User表的外键',
    `role_id` BIGINT NOT NULL COMMENT '关联到Role表的外键',
    `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`user_id`, `role_id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关系表';

-- 6. 角色权限关系表 (RolePermissions)
DROP TABLE IF EXISTS `role_permissions`;
CREATE TABLE `role_permissions` (
    `role_id` BIGINT NOT NULL COMMENT '关联到Role表的外键',
    `permission_id` BIGINT NOT NULL COMMENT '关联到Permission表的外键',
    `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`role_id`, `permission_id`),
    INDEX `idx_role_id` (`role_id`),
    INDEX `idx_permission_id` (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色权限关系表';

-- 添加外键约束（避免循环依赖）
ALTER TABLE `breath_sample` ADD CONSTRAINT `fk_sample_user` FOREIGN KEY (`user_id`) REFERENCES `user`(`user_id`) ON DELETE CASCADE;
ALTER TABLE `user_roles` ADD CONSTRAINT `fk_user_roles_user` FOREIGN KEY (`user_id`) REFERENCES `user`(`user_id`) ON DELETE CASCADE;
ALTER TABLE `user_roles` ADD CONSTRAINT `fk_user_roles_role` FOREIGN KEY (`role_id`) REFERENCES `role`(`role_id`) ON DELETE CASCADE;
ALTER TABLE `role_permissions` ADD CONSTRAINT `fk_role_permissions_role` FOREIGN KEY (`role_id`) REFERENCES `role`(`role_id`) ON DELETE CASCADE;
ALTER TABLE `role_permissions` ADD CONSTRAINT `fk_role_permissions_permission` FOREIGN KEY (`permission_id`) REFERENCES `permission`(`permission_id`) ON DELETE CASCADE;

-- 插入示例数据
-- 权限数据
INSERT INTO `permission` (`permission_id`, `code`, `create_by`) VALUES
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

-- 角色数据
INSERT INTO `role` (`role_id`, `role_name`, `create_by`) VALUES
(1, '超级管理员', 'system'),
(2, '普通用户', 'system');

-- 用户数据（密码明文为123456）
INSERT INTO `user` (`user_id`, `user_name`, `account`, `password`, `email`, `phone`, `status`, `create_by`) VALUES
(1, '系统管理员', 'admin', '123456', 'admin@example.com', '13888888888', '0', 'system'),
(2, '普通用户', 'user', '123456', 'user@example.com', '13999999999', '0', 'system');

-- 呼吸样本数据
INSERT INTO `breath_sample` (`sample_id`, `data_taken`, `user_id`, `create_by`) VALUES
(1, '2024-01-01 10:00:00', 1, 'system'),
(2, '2024-01-02 11:30:00', 2, 'system');

-- 用户角色关系数据
INSERT INTO `user_roles` (`user_id`, `role_id`, `create_by`) VALUES
(1, 1, 'system'),
(2, 2, 'system');

-- 角色权限关系数据
INSERT INTO `role_permissions` (`role_id`, `permission_id`, `create_by`) VALUES
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
SELECT 'Users:' as table_name;
SELECT * FROM `user`;

SELECT 'Roles:' as table_name;
SELECT * FROM `role`;

SELECT 'User Roles:' as table_name;
SELECT u.account, r.role_name 
FROM `user` u 
JOIN `user_roles` ur ON u.user_id = ur.user_id 
JOIN `role` r ON ur.role_id = r.role_id; 