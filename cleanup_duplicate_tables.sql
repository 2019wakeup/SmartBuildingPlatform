-- 清理重复表脚本
-- 删除小写表名的重复表，保留首字母大写的表

USE cloudplatform;

-- 显示当前所有表
SELECT 'Before cleanup:' as status;
SHOW TABLES;

-- 删除小写的重复表
-- 注意：只删除小写版本，保留首字母大写版本

-- 1. 删除小写的用户相关表
DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS role;
DROP TABLE IF EXISTS permission;
DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS role_permissions;
DROP TABLE IF EXISTS breath_sample;

-- 显示清理后的表
SELECT 'After cleanup:' as status;
SHOW TABLES;

-- 验证保留的表数据完整性
SELECT 'User table count:' as info, COUNT(*) as count FROM User;
SELECT 'Role table count:' as info, COUNT(*) as count FROM Role;
SELECT 'Permission table count:' as info, COUNT(*) as count FROM Permission;
SELECT 'UserRoles table count:' as info, COUNT(*) as count FROM UserRoles;
SELECT 'RolePermissions table count:' as info, COUNT(*) as count FROM RolePermissions;
SELECT 'BreathSample table count:' as info, COUNT(*) as count FROM BreathSample;

-- 显示最终的表结构
SELECT 'Final table list:' as status;
SHOW TABLES; 