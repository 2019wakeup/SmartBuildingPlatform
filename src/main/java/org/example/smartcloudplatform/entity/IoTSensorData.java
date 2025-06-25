package org.example.smartcloudplatform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.smartcloudplatform.common.BaseEntity;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 物联网传感器数据实体
 * 
 * @author SmartCloudPlatform
 */
@Schema(description = "物联网传感器数据实体")
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("iot_sensor_data")
public class IoTSensorData extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 数据ID */
    @Schema(description = "数据ID", example = "1")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 设备ID */
    @Schema(description = "设备ID", example = "device_001")
    @NotNull(message = "设备ID不能为空")
    @TableField("device_id")
    private String deviceId;

    /** CO2浓度 (ppm) */
    @Schema(description = "CO2浓度", example = "4469")
    @TableField("co2_ppm")
    private Integer co2Ppm;

    /** TVOC浓度 (ppb) */
    @Schema(description = "TVOC浓度", example = "331")
    @TableField("tvoc_ppb")
    private Integer tvocPpb;

    /** 芯片温度 (℃) */
    @Schema(description = "芯片温度", example = "51.9")
    @TableField("chip_temperature")
    private BigDecimal chipTemperature;

    /** 环境温度 (℃) */
    @Schema(description = "环境温度", example = "29.0")
    @TableField("env_temperature")
    private BigDecimal envTemperature;

    /** 环境湿度 (%) */
    @Schema(description = "环境湿度", example = "77.6")
    @TableField("env_humidity")
    private BigDecimal envHumidity;

    /** 数据接收时间 */
    @Schema(description = "数据接收时间")
    @NotNull(message = "数据接收时间不能为空")
    @TableField("received_time")
    private LocalDateTime receivedTime;

    /** 原始消息内容 */
    @Schema(description = "原始消息内容")
    @TableField("raw_message")
    private String rawMessage;

    public IoTSensorData() {
        this.receivedTime = LocalDateTime.now();
    }

    public IoTSensorData(String deviceId) {
        this();
        this.deviceId = deviceId;
    }
} 