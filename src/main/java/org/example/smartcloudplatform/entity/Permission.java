package org.example.smartcloudplatform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.smartcloudplatform.common.BaseEntity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * 权限信息
 * 
 * @author SmartCloudPlatform
 */
@Schema(description = "权限信息实体")
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("Permission")
public class Permission extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 权限ID */
    @Schema(description = "权限ID", example = "1")
    @TableId(value = "permissionID", type = IdType.AUTO)
    private Long permissionId;

    /** 权限代码 */
    @Schema(description = "权限代码", example = "user:list")
    @NotBlank(message = "权限代码不能为空")
    @Size(min = 0, max = 50, message = "权限代码长度不能超过50个字符")
    private String code;

    /** 角色权限关系列表 - 非数据库字段 */
    @TableField(exist = false)
    private List<RolePermissions> rolePermissions;

    public Permission() {

    }

    public Permission(Long permissionId) {
        this.permissionId = permissionId;
    }
} 