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
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * 个人健康报告实体
 * 
 * @author SmartCloudPlatform
 */
@Schema(description = "个人健康报告实体")
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("health_report")
public class HealthReport extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 报告ID */
    @Schema(description = "报告ID", example = "1")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 指纹数据 */
    @Schema(description = "指纹数据", example = "fingerprint_data_hash")
    @NotBlank(message = "指纹数据不能为空")
    @TableField("finger_print")
    private String fingerPrint;

    /** 数据采集时间 */
    @Schema(description = "数据采集时间", example = "2025-06-27 08:42:20")
    @NotNull(message = "数据采集时间不能为空")
    @TableField("data_taken")
    private LocalDateTime dataTaken;

    /** 报告数据（JSON格式存储传感器数据） */
    @Schema(description = "报告数据", example = "[{\"timestamp\": 1.1, \"temperature_c\": 34.26, \"pressure_hpa\": 1002.47, \"humidity_percent\": 55.0, \"gas_resistance_ohm\": null}]")
    @NotBlank(message = "报告数据不能为空")
    @TableField("report_data")
    private String reportData;

    /** 学生ID */
    @Schema(description = "学生ID", example = "0")
    @NotNull(message = "学生ID不能为空")
    @TableField("stu_id")
    private Long stuId;

    /** 用户对象 - 非数据库字段 */
    @TableField(exist = false)
    private User user;

    public HealthReport() {
    }

    public HealthReport(String fingerPrint, LocalDateTime dataTaken, String reportData, Long stuId) {
        this.fingerPrint = fingerPrint;
        this.dataTaken = dataTaken;
        this.reportData = reportData;
        this.stuId = stuId;
    }
} 