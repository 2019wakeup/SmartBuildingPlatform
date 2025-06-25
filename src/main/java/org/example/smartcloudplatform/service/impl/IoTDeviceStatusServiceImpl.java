package org.example.smartcloudplatform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.example.smartcloudplatform.entity.IoTDeviceStatus;
import org.example.smartcloudplatform.entity.IoTSensorData;
import org.example.smartcloudplatform.mapper.IoTDeviceStatusMapper;
import org.example.smartcloudplatform.service.IIoTDeviceStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 物联网设备状态服务实现类
 * 
 * @author SmartCloudPlatform
 */
@Slf4j
@Service
public class IoTDeviceStatusServiceImpl extends ServiceImpl<IoTDeviceStatusMapper, IoTDeviceStatus> implements IIoTDeviceStatusService {

    @Autowired
    private IoTDeviceStatusMapper deviceStatusMapper;

    @Override
    @Transactional
    public boolean updateDeviceStatus(IoTSensorData sensorData) {
        try {
            String deviceId = sensorData.getDeviceId();
            
            // 查找现有设备状态
            IoTDeviceStatus deviceStatus = getById(deviceId);
            
            if (deviceStatus == null) {
                // 创建新设备状态记录
                deviceStatus = new IoTDeviceStatus(deviceId);
                deviceStatus.setDeviceName("空气质量监测器-" + deviceId);
                deviceStatus.setDeviceLocation("未知位置");
            }

            // 更新设备数据
            deviceStatus.setCurrentCo2Ppm(sensorData.getCo2Ppm());
            deviceStatus.setCurrentTvocPpb(sensorData.getTvocPpb());
            deviceStatus.setCurrentChipTemperature(sensorData.getChipTemperature());
            deviceStatus.setCurrentEnvTemperature(sensorData.getEnvTemperature());
            deviceStatus.setCurrentEnvHumidity(sensorData.getEnvHumidity());
            deviceStatus.setDeviceStatus("online");
            deviceStatus.setLastUpdateTime(LocalDateTime.now());

            // 保存或更新设备状态
            saveOrUpdate(deviceStatus);
            
            log.debug("设备状态已更新: deviceId={}", deviceId);
            return true;
        } catch (Exception e) {
            log.error("更新设备状态失败: deviceId={}, error={}", sensorData.getDeviceId(), e.getMessage(), e);
            return false;
        }
    }

    @Override
    public List<IoTDeviceStatus> getOnlineDevices() {
        return deviceStatusMapper.selectOnlineDevices();
    }

    @Override
    public List<IoTDeviceStatus> getOfflineDevices() {
        return deviceStatusMapper.selectOfflineDevices();
    }

    @Override
    public List<IoTDeviceStatus> getDevicesByLocation(String location) {
        return deviceStatusMapper.selectByLocation(location);
    }

    @Override
    @Transactional
    public int checkAndUpdateOfflineDevices(int offlineThresholdMinutes) {
        try {
            LocalDateTime thresholdTime = LocalDateTime.now().minusMinutes(offlineThresholdMinutes);
            
            // 查找超过阈值时间未更新的设备
            List<IoTDeviceStatus> devicesNotUpdated = deviceStatusMapper.selectDevicesNotUpdatedSince(thresholdTime);
            
            int updatedCount = 0;
            LocalDateTime now = LocalDateTime.now();
            
            for (IoTDeviceStatus device : devicesNotUpdated) {
                // 更新设备状态为离线
                int result = deviceStatusMapper.updateDeviceOffline(device.getDeviceId(), now);
                if (result > 0) {
                    updatedCount++;
                    log.info("设备已标记为离线: deviceId={}, lastUpdateTime={}", 
                            device.getDeviceId(), device.getLastUpdateTime());
                }
            }
            
            log.info("离线设备状态检查完成: 更新设备数={}, 阈值分钟数={}", updatedCount, offlineThresholdMinutes);
            return updatedCount;
        } catch (Exception e) {
            log.error("检查离线设备失败: error={}", e.getMessage(), e);
            return 0;
        }
    }

    @Override
    @Transactional
    public boolean registerDevice(String deviceId, String deviceName, String deviceLocation) {
        try {
            // 检查设备是否已存在
            IoTDeviceStatus existingDevice = getById(deviceId);
            if (existingDevice != null) {
                log.warn("设备已存在: deviceId={}", deviceId);
                return false;
            }

            // 创建新设备
            IoTDeviceStatus newDevice = new IoTDeviceStatus(deviceId);
            newDevice.setDeviceName(deviceName);
            newDevice.setDeviceLocation(deviceLocation);
            newDevice.setDeviceStatus("offline"); // 初始状态为离线
            newDevice.setLastUpdateTime(LocalDateTime.now());

            save(newDevice);
            log.info("设备注册成功: deviceId={}, deviceName={}, location={}", 
                    deviceId, deviceName, deviceLocation);
            return true;
        } catch (Exception e) {
            log.error("设备注册失败: deviceId={}, error={}", deviceId, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public DeviceStatistics getDeviceStatistics() {
        try {
            // 统计总设备数
            long totalDevices = count();
            
            // 统计在线设备数
            QueryWrapper<IoTDeviceStatus> onlineQuery = new QueryWrapper<>();
            onlineQuery.eq("device_status", "online");
            long onlineDevices = count(onlineQuery);
            
            // 统计离线设备数
            QueryWrapper<IoTDeviceStatus> offlineQuery = new QueryWrapper<>();
            offlineQuery.eq("device_status", "offline");
            long offlineDevices = count(offlineQuery);
            
            return new DeviceStatistics(totalDevices, onlineDevices, offlineDevices);
        } catch (Exception e) {
            log.error("获取设备统计信息失败: error={}", e.getMessage(), e);
            return new DeviceStatistics(0, 0, 0);
        }
    }
} 