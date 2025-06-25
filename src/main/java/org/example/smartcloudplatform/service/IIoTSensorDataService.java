package org.example.smartcloudplatform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.smartcloudplatform.entity.IoTSensorData;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 物联网传感器数据服务接口
 * 
 * @author SmartCloudPlatform
 */
public interface IIoTSensorDataService extends IService<IoTSensorData> {

    /**
     * 处理设备上传的原始数据
     * @param deviceId 设备ID
     * @param rawMessage 原始消息
     * @return 是否处理成功
     */
    boolean processDeviceMessage(String deviceId, String rawMessage);

    /**
     * 根据设备ID和时间范围查询数据
     * @param deviceId 设备ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 传感器数据列表
     */
    List<IoTSensorData> getDataByDeviceIdAndTimeRange(String deviceId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取设备最新数据
     * @param deviceId 设备ID
     * @return 最新传感器数据
     */
    IoTSensorData getLatestDataByDeviceId(String deviceId);

    /**
     * 获取设备最近N条数据
     * @param deviceId 设备ID
     * @param limit 数量限制
     * @return 传感器数据列表
     */
    List<IoTSensorData> getRecentDataByDeviceId(String deviceId, int limit);

    /**
     * 检查数据是否需要存储（基于变化阈值）
     * @param deviceId 设备ID
     * @param newData 新数据
     * @return 是否需要存储
     */
    boolean shouldStoreData(String deviceId, IoTSensorData newData);

    /**
     * 清理历史数据
     * @param beforeTime 指定时间之前的数据将被删除
     * @return 删除的记录数
     */
    int cleanHistoryData(LocalDateTime beforeTime);
} 