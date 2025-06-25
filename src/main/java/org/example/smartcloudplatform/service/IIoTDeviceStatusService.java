package org.example.smartcloudplatform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.smartcloudplatform.entity.IoTDeviceStatus;
import org.example.smartcloudplatform.entity.IoTSensorData;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 物联网设备状态服务接口
 * 
 * @author SmartCloudPlatform
 */
public interface IIoTDeviceStatusService extends IService<IoTDeviceStatus> {

    /**
     * 更新设备状态
     * @param sensorData 传感器数据
     * @return 是否更新成功
     */
    boolean updateDeviceStatus(IoTSensorData sensorData);

    /**
     * 获取所有在线设备
     * @return 在线设备列表
     */
    List<IoTDeviceStatus> getOnlineDevices();

    /**
     * 获取所有离线设备
     * @return 离线设备列表
     */
    List<IoTDeviceStatus> getOfflineDevices();

    /**
     * 根据位置查询设备
     * @param location 位置关键字
     * @return 设备列表
     */
    List<IoTDeviceStatus> getDevicesByLocation(String location);

    /**
     * 检查并更新离线设备状态
     * @param offlineThresholdMinutes 离线阈值（分钟）
     * @return 更新的设备数量
     */
    int checkAndUpdateOfflineDevices(int offlineThresholdMinutes);

    /**
     * 注册新设备
     * @param deviceId 设备ID
     * @param deviceName 设备名称
     * @param deviceLocation 设备位置
     * @return 是否注册成功
     */
    boolean registerDevice(String deviceId, String deviceName, String deviceLocation);

    /**
     * 获取设备统计信息
     * @return 设备统计信息
     */
    DeviceStatistics getDeviceStatistics();

    /**
     * 设备统计信息内部类
     */
    class DeviceStatistics {
        private long totalDevices;
        private long onlineDevices;
        private long offlineDevices;
        
        // 构造函数
        public DeviceStatistics(long totalDevices, long onlineDevices, long offlineDevices) {
            this.totalDevices = totalDevices;
            this.onlineDevices = onlineDevices;
            this.offlineDevices = offlineDevices;
        }
        
        // Getter方法
        public long getTotalDevices() { return totalDevices; }
        public long getOnlineDevices() { return onlineDevices; }
        public long getOfflineDevices() { return offlineDevices; }
        
        // Setter方法
        public void setTotalDevices(long totalDevices) { this.totalDevices = totalDevices; }
        public void setOnlineDevices(long onlineDevices) { this.onlineDevices = onlineDevices; }
        public void setOfflineDevices(long offlineDevices) { this.offlineDevices = offlineDevices; }
    }
} 