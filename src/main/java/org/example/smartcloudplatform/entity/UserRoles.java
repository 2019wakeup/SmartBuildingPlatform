package org.example.smartcloudplatform.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.smartcloudplatform.common.BaseEntity;

import jakarta.validation.constraints.NotNull;

/**
 * 用户角色关系
 * 
 * @author SmartCloudPlatform
 */
@Schema(description = "用户角色关系实体")
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("UserRoles")
public class UserRoles extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 用户ID - 外键关联到User表 */
    @Schema(description = "用户ID", example = "1")
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    /** 角色ID - 外键关联到Role表 */
    @Schema(description = "角色ID", example = "1")
    @NotNull(message = "角色ID不能为空")
    private Long roleId;

    /** 用户对象 - 非数据库字段 */
    @TableField(exist = false)
    private User user;

    /** 角色对象 - 非数据库字段 */
    @TableField(exist = false)
    private Role role;

    public UserRoles() {

    }

    public UserRoles(Long userId, Long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }
} 