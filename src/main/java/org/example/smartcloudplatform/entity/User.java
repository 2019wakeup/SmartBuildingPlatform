package org.example.smartcloudplatform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.smartcloudplatform.common.BaseEntity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * 用户信息
 * 
 * @author SmartCloudPlatform
 */
@Schema(description = "用户信息实体")
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("User")
public class User extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 用户ID */
    @Schema(description = "用户ID", example = "1")
    @TableId(value = "userID", type = IdType.AUTO)
    private Long userId;

    /** 用户名称 */
    @Schema(description = "用户名称", example = "张三")
    @NotBlank(message = "用户名称不能为空")
    @Size(min = 0, max = 50, message = "用户名称长度不能超过50个字符")
    private String userName;

    /** 用户账号 */
    @Schema(description = "用户账号", example = "admin")
    @NotBlank(message = "用户账号不能为空")
    @Size(min = 0, max = 50, message = "用户账号长度不能超过50个字符")
    private String account;

    /** 用户密码 */
    @Schema(description = "用户密码", example = "123456")
    @JsonIgnore
    private String password;

    /** 用户邮箱 */
    @Schema(description = "用户邮箱", example = "admin@example.com")
    @Email(message = "邮箱格式不正确")
    @Size(min = 0, max = 100, message = "邮箱长度不能超过100个字符")
    private String email;

    /** 手机号码 */
    @Schema(description = "手机号码", example = "13888888888")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号码格式不正确")
    private String phone;

    /** 角色ID */
    private Long roleId;

    /** 样本ID */
    private Long sampleId;

    /** 用户状态（0正常 1停用） */
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 最后登录IP */
    @TableField(exist = false)
    private String loginIp;

    /** 最后登录时间 */
    @TableField(exist = false)
    private String loginDate;

    /** 角色对象 */
    @TableField(exist = false)
    private Role role;

    /** 角色组 */
    @TableField(exist = false)
    private Long[] roleIds;

    /** 角色列表 */
    @TableField(exist = false)
    private List<Role> roles;

    public User() {

    }

    public User(Long userId) {
        this.userId = userId;
    }

    public boolean isAdmin() {
        return isAdmin(this.userId);
    }

    public static boolean isAdmin(Long userId) {
        return userId != null && 1L == userId;
    }
} 