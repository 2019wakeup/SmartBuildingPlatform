-- MySQL 大小写敏感性配置
-- 设置表名大小写敏感性

-- 查看当前设置
SHOW VARIABLES LIKE 'lower_case_table_names';

-- 显示当前的系统变量
SELECT 
    @@lower_case_table_names as 'lower_case_table_names',
    @@sql_mode as 'sql_mode',
    @@version as 'mysql_version';

-- 注意：lower_case_table_names 只能在MySQL服务器启动时设置
-- 0 = 表名按照创建时的大小写存储，比较时大小写敏感
-- 1 = 表名转换为小写存储，比较时大小写不敏感
-- 2 = 表名按照创建时的大小写存储，比较时转换为小写（仅适用于不区分大小写的文件系统） 