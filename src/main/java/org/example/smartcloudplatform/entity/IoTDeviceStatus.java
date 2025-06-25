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
 * 物联网设备状态实体 - 记录设备最新状态
 * 
 * @author SmartCloudPlatform
 */
@Schema(description = "物联网设备状态实体")
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("iot_device_status")
public class IoTDeviceStatus extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 设备ID - 主键 */
    @Schema(description = "设备ID", example = "device_001")
    @TableId(value = "device_id", type = IdType.INPUT)
    @NotNull(message = "设备ID不能为空")
    private String deviceId;

    /** 设备名称 */
    @Schema(description = "设备名称", example = "空气质量监测器-001")
    @TableField("device_name")
    private String deviceName;

    /** 设备位置 */
    @Schema(description = "设备位置", example = "办公室A区")
    @TableField("device_location")
    private String deviceLocation;

    /** 当前CO2浓度 (ppm) */
    @Schema(description = "当前CO2浓度", example = "4469")
    @TableField("current_co2_ppm")
    private Integer currentCo2Ppm;

    /** 当前TVOC浓度 (ppb) */
    @Schema(description = "当前TVOC浓度", example = "331")
    @TableField("current_tvoc_ppb")
    private Integer currentTvocPpb;

    /** 当前芯片温度 (℃) */
    @Schema(description = "当前芯片温度", example = "51.9")
    @TableField("current_chip_temperature")
    private BigDecimal currentChipTemperature;

    /** 当前环境温度 (℃) */
    @Schema(description = "当前环境温度", example = "29.0")
    @TableField("current_env_temperature")
    private BigDecimal currentEnvTemperature;

    /** 当前环境湿度 (%) */
    @Schema(description = "当前环境湿度", example = "77.6")
    @TableField("current_env_humidity")
    private BigDecimal currentEnvHumidity;

    /** 设备状态 */
    @Schema(description = "设备状态", example = "online")
    @TableField("device_status")
    private String deviceStatus = "online";

    /** 最后更新时间 */
    @Schema(description = "最后更新时间")
    @TableField("last_update_time")
    private LocalDateTime lastUpdateTime;

    /** 最后离线时间 */
    @Schema(description = "最后离线时间")
    @TableField("last_offline_time")
    private LocalDateTime lastOfflineTime;

    public IoTDeviceStatus() {
        this.lastUpdateTime = LocalDateTime.now();
    }

    public IoTDeviceStatus(String deviceId) {
        this();
        this.deviceId = deviceId;
    }
} 