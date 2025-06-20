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
import java.time.LocalDateTime;

/**
 * 呼吸样本信息
 * 
 * @author SmartCloudPlatform
 */
@Schema(description = "呼吸样本信息实体")
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("BreathSample")
public class BreathSample extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 样本ID */
    @Schema(description = "样本ID", example = "1")
    @TableId(value = "sampleID", type = IdType.AUTO)
    private Long sampleId;

    /** 样本采集时间 */
    @Schema(description = "样本采集时间", example = "2024-01-01 10:00:00")
    @NotNull(message = "样本采集时间不能为空")
    private LocalDateTime dataTaken;

    /** 用户ID - 外键关联到User表 */
    @Schema(description = "用户ID", example = "1")
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    /** 用户对象 - 非数据库字段 */
    @TableField(exist = false)
    private User user;

    public BreathSample() {

    }

    public BreathSample(Long sampleId) {
        this.sampleId = sampleId;
    }
} 