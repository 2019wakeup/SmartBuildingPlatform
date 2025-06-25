-- 更新IoT表结构，添加缺失的审计字段
USE cloudplatform;

-- 添加 iot_sensor_data 表的审计字段
ALTER TABLE iot_sensor_data 
ADD COLUMN create_by varchar(50) DEFAULT 'system' COMMENT '创建者',
ADD COLUMN update_by varchar(50) DEFAULT 'system' COMMENT '更新者', 
ADD COLUMN remark text COMMENT '备注';

-- 添加 iot_device_status 表的审计字段
ALTER TABLE iot_device_status
ADD COLUMN create_by varchar(50) DEFAULT 'system' COMMENT '创建者',
ADD COLUMN update_by varchar(50) DEFAULT 'system' COMMENT '更新者',
ADD COLUMN remark text COMMENT '备注';

-- 显示更新后的表结构
DESCRIBE iot_sensor_data;
DESCRIBE iot_device_status; 