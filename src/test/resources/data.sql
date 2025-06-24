-- 插入测试用户数据
INSERT INTO sys_user (user_id, user_name, account, email, phone, password, status, del_flag, create_by, create_time) VALUES
(1, '管理员', 'admin', 'admin@example.com', '13800138000', '$2a$10$7JB720yubVSoOvdq0bTyPOyWpLxGQPOWPOWPOWPOWPOWPOWPOWPOW', '0', '0', 'system', NOW()),
(2, '测试用户', 'testuser', 'test@example.com', '13800138001', '$2a$10$7JB720yubVSoOvdq0bTyPOyWpLxGQPOWPOWPOWPOWPOWPOWPOWPOW', '0', '0', 'system', NOW());

-- 插入测试角色数据
INSERT INTO sys_role (role_id, role_name, role_key, role_sort, status, del_flag, create_by, create_time, user_id, permission_id) VALUES
(1, '管理员', 'admin', 1, '0', '0', 'system', NOW(), 1, 1),
(2, '普通用户', 'common', 2, '0', '0', 'system', NOW(), 2, 2);

-- 插入测试权限数据
INSERT INTO sys_permission (permission_id, permission_name, permission_key, permission_type, parent_id, order_num, status, create_by, create_time) VALUES
(1, '系统管理', 'system', 'M', 0, 1, '0', 'system', NOW()),
(2, '用户管理', 'system:user', 'C', 1, 1, '0', 'system', NOW()),
(3, '角色管理', 'system:role', 'C', 1, 2, '0', 'system', NOW());

-- 插入用户角色关联数据
INSERT INTO sys_user_role (user_id, role_id) VALUES
(1, 1),
(2, 2);

-- 插入角色权限关联数据
INSERT INTO sys_role_permission (role_id, permission_id) VALUES
(1, 1),
(1, 2),
(1, 3),
(2, 2); 