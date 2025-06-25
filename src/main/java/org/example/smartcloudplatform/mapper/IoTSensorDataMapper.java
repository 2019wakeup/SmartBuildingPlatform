package org.example.smartcloudplatform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.smartcloudplatform.entity.IoTSensorData;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 物联网传感器数据Mapper接口
 * 
 * @author SmartCloudPlatform
 */
@Mapper
public interface IoTSensorDataMapper extends BaseMapper<IoTSensorData> {

    /**
     * 根据设备ID和时间范围查询数据
     */
    @Select("SELECT * FROM iot_sensor_data WHERE device_id = #{deviceId} " +
            "AND received_time BETWEEN #{startTime} AND #{endTime} " +
            "ORDER BY received_time DESC")
    List<IoTSensorData> selectByDeviceIdAndTimeRange(
            @Param("deviceId") String deviceId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    /**
     * 获取设备最新的数据记录
     */
    @Select("SELECT * FROM iot_sensor_data WHERE device_id = #{deviceId} " +
            "ORDER BY received_time DESC LIMIT 1")
    IoTSensorData selectLatestByDeviceId(@Param("deviceId") String deviceId);

    /**
     * 根据设备ID获取最近N条记录
     */
    @Select("SELECT * FROM iot_sensor_data WHERE device_id = #{deviceId} " +
            "ORDER BY received_time DESC LIMIT #{limit}")
    List<IoTSensorData> selectRecentByDeviceId(
            @Param("deviceId") String deviceId,
            @Param("limit") int limit);

    /**
     * 删除指定时间之前的历史数据
     */
    @Select("DELETE FROM iot_sensor_data WHERE received_time < #{beforeTime}")
    int deleteHistoryDataBefore(@Param("beforeTime") LocalDateTime beforeTime);
} 