-- 健康报告表
CREATE TABLE `health_report` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '报告ID',
  `finger_print` varchar(255) NOT NULL COMMENT '指纹数据',
  `data_taken` datetime NOT NULL COMMENT '数据采集时间',
  `report_data` longtext NOT NULL COMMENT '报告数据（JSON格式存储传感器数据）',
  `stu_id` bigint(20) NOT NULL COMMENT '学生ID',
  `create_by` varchar(64) DEFAULT 'system' COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT 'system' COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` text COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_stu_id` (`stu_id`),
  KEY `idx_data_taken` (`data_taken`),
  KEY `idx_stu_data_taken` (`stu_id`, `data_taken`),
  KEY `idx_finger_print` (`finger_print`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='个人健康报告表';

-- 插入示例数据
INSERT INTO `health_report` (`finger_print`, `data_taken`, `report_data`, `stu_id`, `create_by`, `remark`) VALUES
('sample_fingerprint_hash_001', '2025-06-27 08:42:20', '[{"timestamp": 1.1, "temperature_c": 34.26, "pressure_hpa": 1002.47, "humidity_percent": 55.0, "gas_resistance_ohm": null}, {"timestamp": 2.1, "temperature_c": 34.28, "pressure_hpa": 1002.45, "humidity_percent": 54.88, "gas_resistance_ohm": 5684.85}]', 0, 'system', '示例健康报告数据'); 