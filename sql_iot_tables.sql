-- 物联网传感器数据表
CREATE TABLE `iot_sensor_data` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '数据ID',
  `device_id` varchar(50) NOT NULL COMMENT '设备ID',
  `co2_ppm` int(11) DEFAULT NULL COMMENT 'CO2浓度(ppm)',
  `tvoc_ppb` int(11) DEFAULT NULL COMMENT 'TVOC浓度(ppb)',
  `chip_temperature` decimal(5,2) DEFAULT NULL COMMENT '芯片温度(℃)',
  `env_temperature` decimal(5,2) DEFAULT NULL COMMENT '环境温度(℃)',
  `env_humidity` decimal(5,2) DEFAULT NULL COMMENT '环境湿度(%)',
  `received_time` datetime NOT NULL COMMENT '数据接收时间',
  `raw_message` text COMMENT '原始消息内容',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_device_id` (`device_id`),
  KEY `idx_received_time` (`received_time`),
  KEY `idx_device_time` (`device_id`, `received_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='物联网传感器数据表';

-- 物联网设备状态表
CREATE TABLE `iot_device_status` (
  `device_id` varchar(50) NOT NULL COMMENT '设备ID',
  `device_name` varchar(100) DEFAULT NULL COMMENT '设备名称',
  `device_location` varchar(200) DEFAULT NULL COMMENT '设备位置',
  `current_co2_ppm` int(11) DEFAULT NULL COMMENT '当前CO2浓度(ppm)',
  `current_tvoc_ppb` int(11) DEFAULT NULL COMMENT '当前TVOC浓度(ppb)',
  `current_chip_temperature` decimal(5,2) DEFAULT NULL COMMENT '当前芯片温度(℃)',
  `current_env_temperature` decimal(5,2) DEFAULT NULL COMMENT '当前环境温度(℃)',
  `current_env_humidity` decimal(5,2) DEFAULT NULL COMMENT '当前环境湿度(%)',
  `device_status` varchar(20) DEFAULT 'offline' COMMENT '设备状态(online/offline)',
  `last_update_time` datetime DEFAULT NULL COMMENT '最后更新时间',
  `last_offline_time` datetime DEFAULT NULL COMMENT '最后离线时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`device_id`),
  KEY `idx_device_status` (`device_status`),
  KEY `idx_last_update_time` (`last_update_time`),
  KEY `idx_location` (`device_location`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='物联网设备状态表';

-- 插入示例数据
INSERT INTO `iot_device_status` (`device_id`, `device_name`, `device_location`, `device_status`, `last_update_time`) VALUES
('device_001', '空气质量监测器-001', '办公室A区', 'offline', NOW()),
('device_002', '空气质量监测器-002', '办公室B区', 'offline', NOW()),
('device_003', '空气质量监测器-003', '会议室', 'offline', NOW()); 