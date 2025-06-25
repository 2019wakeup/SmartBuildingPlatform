package org.example.smartcloudplatform.config;

import lombok.extern.slf4j.Slf4j;
import org.example.smartcloudplatform.service.IIoTDeviceStatusService;
import org.example.smartcloudplatform.service.IIoTSensorDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

/**
 * 定时任务配置
 * 
 * @author SmartCloudPlatform
 */
@Slf4j
@Configuration
@EnableScheduling
public class ScheduleConfig {

    @Autowired
    private IIoTDeviceStatusService deviceStatusService;

    @Autowired
    private IIoTSensorDataService sensorDataService;

    /**
     * 每10分钟检查一次离线设备
     */
    @Scheduled(fixedRate = 600000) // 10分钟 = 600000毫秒
    public void checkOfflineDevices() {
        try {
            log.debug("开始检查离线设备...");
            int updatedCount = deviceStatusService.checkAndUpdateOfflineDevices(10); // 10分钟阈值
            if (updatedCount > 0) {
                log.info("定时检查发现离线设备: {} 个", updatedCount);
            }
        } catch (Exception e) {
            log.error("定时检查离线设备失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 每天凌晨2点清理30天前的历史数据
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void cleanHistoryData() {
        try {
            log.info("开始清理历史数据...");
            LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
            int deletedCount = sensorDataService.cleanHistoryData(thirtyDaysAgo);
            log.info("历史数据清理完成，删除记录数: {}", deletedCount);
        } catch (Exception e) {
            log.error("清理历史数据失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 每小时记录设备统计信息
     */
    @Scheduled(fixedRate = 3600000) // 1小时 = 3600000毫秒
    public void logDeviceStatistics() {
        try {
            IIoTDeviceStatusService.DeviceStatistics stats = deviceStatusService.getDeviceStatistics();
            log.info("设备统计 - 总数: {}, 在线: {}, 离线: {}", 
                    stats.getTotalDevices(), stats.getOnlineDevices(), stats.getOfflineDevices());
        } catch (Exception e) {
            log.error("记录设备统计失败: {}", e.getMessage(), e);
        }
    }
} 