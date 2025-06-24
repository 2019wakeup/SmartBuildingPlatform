package org.example.smartcloudplatform.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.smartcloudplatform.common.BaseEntity;

import jakarta.validation.constraints.NotNull;

/**
 * 角色权限关系
 * 
 * @author SmartCloudPlatform
 */
@Schema(description = "角色权限关系实体")
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("RolePermissions")
public class RolePermissions extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 角色ID - 外键关联到Role表 */
    @Schema(description = "角色ID", example = "1")
    @NotNull(message = "角色ID不能为空")
    private Long roleId;

    /** 权限ID - 外键关联到Permission表 */
    @Schema(description = "权限ID", example = "1")
    @NotNull(message = "权限ID不能为空")
    private Long permissionId;

    /** 角色对象 - 非数据库字段 */
    @TableField(exist = false)
    private Role role;

    /** 权限对象 - 非数据库字段 */
    @TableField(exist = false)
    private Permission permission;

    public RolePermissions() {

    }

    public RolePermissions(Long roleId, Long permissionId) {
        this.roleId = roleId;
        this.permissionId = permissionId;
    }
} 