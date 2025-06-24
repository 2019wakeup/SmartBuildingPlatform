-- 用户表
CREATE TABLE IF NOT EXISTS sys_user (
    user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_name VARCHAR(30) NOT NULL,
    account VARCHAR(30) NOT NULL UNIQUE,
    email VARCHAR(50),
    phone VARCHAR(11),
    sex CHAR(1) DEFAULT '0',
    avatar VARCHAR(100),
    password VARCHAR(100),
    status CHAR(1) DEFAULT '0',
    del_flag CHAR(1) DEFAULT '0',
    login_ip VARCHAR(128),
    login_date DATETIME,
    create_by VARCHAR(64),
    create_time DATETIME,
    update_by VARCHAR(64),
    update_time DATETIME,
    remark VARCHAR(500)
);

-- 角色表
CREATE TABLE IF NOT EXISTS sys_role (
    role_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    role_name VARCHAR(30) NOT NULL,
    role_key VARCHAR(100) NOT NULL,
    role_sort INT DEFAULT 0,
    data_scope CHAR(1) DEFAULT '1',
    menu_check_strictly BOOLEAN DEFAULT TRUE,
    dept_check_strictly BOOLEAN DEFAULT TRUE,
    status CHAR(1) DEFAULT '0',
    del_flag CHAR(1) DEFAULT '0',
    create_by VARCHAR(64),
    create_time DATETIME,
    update_by VARCHAR(64),
    update_time DATETIME,
    remark VARCHAR(500),
    user_id BIGINT,
    permission_id BIGINT
);

-- 权限表
CREATE TABLE IF NOT EXISTS sys_permission (
    permission_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    permission_name VARCHAR(50) NOT NULL,
    permission_key VARCHAR(100) NOT NULL,
    permission_type CHAR(1) DEFAULT 'M',
    parent_id BIGINT DEFAULT 0,
    order_num INT DEFAULT 0,
    path VARCHAR(200),
    component VARCHAR(255),
    query VARCHAR(255),
    is_frame INT DEFAULT 1,
    is_cache INT DEFAULT 0,
    menu_type CHAR(1) DEFAULT 'C',
    visible CHAR(1) DEFAULT '0',
    status CHAR(1) DEFAULT '0',
    perms VARCHAR(100),
    icon VARCHAR(100),
    create_by VARCHAR(64),
    create_time DATETIME,
    update_by VARCHAR(64),
    update_time DATETIME,
    remark VARCHAR(500)
);

-- 用户角色关联表
CREATE TABLE IF NOT EXISTS sys_user_role (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id)
);

-- 角色权限关联表
CREATE TABLE IF NOT EXISTS sys_role_permission (
    role_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    PRIMARY KEY (role_id, permission_id)
);

-- 呼吸样本表
CREATE TABLE IF NOT EXISTS breath_sample (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    sample_data TEXT,
    analysis_result TEXT,
    create_time DATETIME,
    update_time DATETIME
); 