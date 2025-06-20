package org.example.smartcloudplatform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
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
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("Permission")
public class Permission extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 权限ID */
    @TableId(value = "permissionID", type = IdType.AUTO)
    private Long permissionId;

    /** 权限编码 */
    @NotBlank(message = "权限编码不能为空")
    @Size(min = 0, max = 50, message = "权限编码长度不能超过50个字符")
    private String code;

    /** 权限名称 */
    @NotBlank(message = "权限名称不能为空")
    @Size(min = 0, max = 50, message = "权限名称长度不能超过50个字符")
    private String permissionName;

    /** 权限类型（M目录 C菜单 F按钮） */
    private String permissionType;

    /** 父权限ID */
    private Long parentId;

    /** 显示顺序 */
    private Integer orderNum;

    /** 路由地址 */
    private String path;

    /** 组件路径 */
    private String component;

    /** 路由参数 */
    private String query;

    /** 是否为外链（0是 1否） */
    private String isFrame;

    /** 是否缓存（0缓存 1不缓存） */
    private String isCache;

    /** 权限状态（0正常 1停用） */
    private String status;

    /** 权限图标 */
    private String icon;

    /** 视频名称 */
    private String videoName;

    /** 子权限 */
    @TableField(exist = false)
    private List<Permission> children;

    public Permission() {

    }

    public Permission(Long permissionId) {
        this.permissionId = permissionId;
    }
} 