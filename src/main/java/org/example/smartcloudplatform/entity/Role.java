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

    /** 权限ID */
    @Schema(description = "权限ID", example = "1")
    private Long permissionId;

    /** 角色权限 */
    @Schema(description = "角色权限标识", example = "admin")
    private String roleKey;

    /** 显示顺序 */
    private Integer roleSort;

    /** 数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限） */
    private String dataScope;

    /** 角色状态（0正常 1停用） */
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 用户是否存在此角色标识 默认不存在 */
    @TableField(exist = false)
    private boolean flag = false;

    /** 权限组 */
    @TableField(exist = false)
    private Long[] permissionIds;

    /** 权限列表 */
    @TableField(exist = false)
    private List<Permission> permissions;

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