-- 创建数据库
CREATE DATABASE IF NOT EXISTS cloudplatform DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE cloudplatform;

-- 1. 视频表 Video
CREATE TABLE Video (
    videoName VARCHAR(100) PRIMARY KEY,
    uploadDate DATETIME NOT NULL,
    teacher VARCHAR(50) NOT NULL
);

-- 2. 权限表 Permission
CREATE TABLE Permission (
    permissionID BIGINT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(50) NOT NULL,
    videoName VARCHAR(100),
    CONSTRAINT fk_permission_video FOREIGN KEY (videoName) REFERENCES Video(videoName)
);

-- 3. 角色表 Role
CREATE TABLE Role (
    roleID BIGINT PRIMARY KEY AUTO_INCREMENT,
    roleName VARCHAR(50) NOT NULL,
    permissionID BIGINT,
    CONSTRAINT fk_role_permission FOREIGN KEY (permissionID) REFERENCES Permission(permissionID)
);

-- 4. 用户表 User
CREATE TABLE User (
    userID BIGINT PRIMARY KEY AUTO_INCREMENT,
    userName VARCHAR(50) NOT NULL,
    account VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20),
    roleID BIGINT,
    sampleID BIGINT,
    CONSTRAINT fk_user_role FOREIGN KEY (roleID) REFERENCES Role(roleID)
    -- 注意：sampleID 的外键将稍后添加（需等 BreathSample 表创建）
);

-- 5. 呼吸样本表 BreathSample
CREATE TABLE BreathSample (
    sampleID BIGINT PRIMARY KEY AUTO_INCREMENT,
    dataTaken DATETIME NOT NULL,
    userID BIGINT,
    CONSTRAINT fk_sample_user FOREIGN KEY (userID) REFERENCES User(userID)
);

-- 补充 user.sampleID 外键（现在 BreathSample 已存在）
ALTER TABLE User
    ADD CONSTRAINT fk_user_sample FOREIGN KEY (sampleID) REFERENCES BreathSample(sampleID);

-- 6. 用户角色关联表 UserRoles
CREATE TABLE UserRoles (
    userID BIGINT,
    roleID BIGINT,
    PRIMARY KEY (userID, roleID),
    CONSTRAINT fk_ur_user FOREIGN KEY (userID) REFERENCES User(userID),
    CONSTRAINT fk_ur_role FOREIGN KEY (roleID) REFERENCES Role(roleID)
);

-- 7. 角色权限关联表 RolePermissions
CREATE TABLE RolePermissions (
    roleID BIGINT,
    permissionID BIGINT,
    PRIMARY KEY (roleID, permissionID),
    CONSTRAINT fk_rp_role FOREIGN KEY (roleID) REFERENCES Role(roleID),
    CONSTRAINT fk_rp_permission FOREIGN KEY (permissionID) REFERENCES Permission(permissionID)
);
