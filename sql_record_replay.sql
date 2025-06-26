-- 创建数据库
CREATE DATABASE IF NOT EXISTS `course_system`
DEFAULT CHARACTER SET utf8mb4
DEFAULT COLLATE utf8mb4_unicode_ci;

USE `course_system`;

-- 创建课程表
CREATE TABLE IF NOT EXISTS `courses` (
                                         `id` INT(11) NOT NULL AUTO_INCREMENT,
    `title` VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '课程名称',
    `description` TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '课程描述',
    `instructor` VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '讲师姓名',
    `level` VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '课程级别',
    `rating` FLOAT COMMENT '评分',
    `lessons` INT(11) COMMENT '课时数',
    `students` INT(11) COMMENT '学生人数',
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 创建视频表
CREATE TABLE IF NOT EXISTS `videos` (
                                        `id` INT(11) NOT NULL AUTO_INCREMENT,
    `course_id` INT(11) NOT NULL COMMENT '关联课程ID',
    `title` VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '视频标题',
    `url` VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '视频路径/链接',
    `duration` INT(11) COMMENT '时长(分钟)',
    `sequence` INT(11) COMMENT '播放顺序',
    PRIMARY KEY (`id`),
    FOREIGN KEY (`course_id`) REFERENCES `courses`(`id`) ON DELETE CASCADE ON UPDATE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 插入课程数据（完整描述）
INSERT INTO `courses` (`title`, `description`, `instructor`, `level`, `rating`, `lessons`, `students`) VALUES
                                                                                                           ('数据结构', '学习基本数据结构与算法，掌握程序设计基础', '胡书山', '必修', 5, 16, 90),
                                                                                                           ('计算机组成原理', '理解计算机硬件组成和工作原理，掌握硬件基础知识', '向尧', '必修', 5, 16, 90),
                                                                                                           ('计算机构架', '学习计算机系统架构设计与优化方法', '徐婕', '必修', 5, 16, 90),
                                                                                                           ('大数据分析与应用', '掌握大数据处理与分析的基本原理与实践', '李洁', '专选', 5, 16, 90),
                                                                                                           ('LINUX系统与分析', '深入学习Linux操作系统原理与系统编程', '徐婕', '专选', 5, 16, 90),
                                                                                                           ('合作项目', '培养团队协作能力和实际项目开发技能', 'Xia Cui,孙斌,许立君', '必修', 5, 16, 90);

-- 插入视频数据（修复路径问题并保持原始数据）
INSERT INTO `videos` (`course_id`, `title`, `url`, `duration`, `sequence`) VALUES
                                                                               (1, '数据结构绪论', 'https://example.com/video101', 24, 1),
                                                                               (1, '线性表与链表', '/static/videos/线性表.mp4', 32, 2),  -- 修正路径扩展名
                                                                               (2, '计算机系统概述', 'https://example.com/video201', 30, 1),
                                                                               (2, '数据表示与运算', 'https://example.com/video202', 35, 2);

-- 添加测试查询语句
SELECT
    c.title AS '课程名称',
        v.title AS '视频标题',
        v.duration AS '时长(分钟)',
        v.url AS '视频链接',
        v.sequence AS '播放顺序'
FROM `videos` v
         JOIN `courses` c ON v.course_id = c.id
ORDER BY c.id, v.sequence;