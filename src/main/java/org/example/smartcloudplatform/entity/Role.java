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
 * 角色信息
 * 
 * @author SmartCloudPlatform
 */
@Schema(description = "角色信息实体")
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("Role")
public class Role extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 角色ID */
    @Schema(description = "角色ID", example = "1")
    @TableId(value = "roleID", type = IdType.AUTO)
    private Long roleId;

    /** 角色名称 */
    @Schema(description = "角色名称", example = "管理员")
    @NotBlank(message = "角色名称不能为空")
    @Size(min = 0, max = 50, message = "角色名称长度不能超过50个字符")
    private String roleName;

    /** 用户ID - 外键关联到User表 */
    @Schema(description = "用户ID", example = "1")
    private Long userId;

    /** 权限ID - 外键关联到Permission表 */
    @Schema(description = "权限ID", example = "1")
    private Long permissionId;

    /** 用户对象 - 非数据库字段 */
    @TableField(exist = false)
    private User user;

    /** 权限对象 - 非数据库字段 */
    @TableField(exist = false)
    private Permission permission;

    /** 角色权限关系列表 - 非数据库字段 */
    @TableField(exist = false)
    private List<RolePermissions> rolePermissions;

    public Role() {

    }

    public Role(Long roleId) {
        this.roleId = roleId;
    }

    public boolean isAdmin() {
        return isAdmin(this.roleId);
    }

    public static boolean isAdmin(Long roleId) {
        return roleId != null && 1L == roleId;
    }
} 