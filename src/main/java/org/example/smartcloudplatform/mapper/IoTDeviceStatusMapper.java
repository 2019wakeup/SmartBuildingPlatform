package org.example.smartcloudplatform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.example.smartcloudplatform.entity.IoTDeviceStatus;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 物联网设备状态Mapper接口
 * 
 * @author SmartCloudPlatform
 */
@Mapper
public interface IoTDeviceStatusMapper extends BaseMapper<IoTDeviceStatus> {

    /**
     * 获取所有在线设备
     */
    @Select("SELECT * FROM iot_device_status WHERE device_status = 'online' " +
            "ORDER BY last_update_time DESC")
    List<IoTDeviceStatus> selectOnlineDevices();

    /**
     * 获取所有离线设备
     */
    @Select("SELECT * FROM iot_device_status WHERE device_status = 'offline' " +
            "ORDER BY last_offline_time DESC")
    List<IoTDeviceStatus> selectOfflineDevices();

    /**
     * 更新设备状态为离线
     */
    @Update("UPDATE iot_device_status SET device_status = 'offline', " +
            "last_offline_time = #{offlineTime} WHERE device_id = #{deviceId}")
    int updateDeviceOffline(@Param("deviceId") String deviceId, 
                           @Param("offlineTime") LocalDateTime offlineTime);

    /**
     * 根据位置查询设备
     */
    @Select("SELECT * FROM iot_device_status WHERE device_location LIKE CONCAT('%', #{location}, '%') " +
            "ORDER BY last_update_time DESC")
    List<IoTDeviceStatus> selectByLocation(@Param("location") String location);

    /**
     * 查询超过指定时间未更新的设备（可能离线）
     */
    @Select("SELECT * FROM iot_device_status WHERE last_update_time < #{beforeTime} " +
            "AND device_status = 'online'")
    List<IoTDeviceStatus> selectDevicesNotUpdatedSince(@Param("beforeTime") LocalDateTime beforeTime);
} 