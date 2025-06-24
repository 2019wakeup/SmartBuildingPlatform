package org.example.smartcloudplatform.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * 登录请求实体
 * 
 * @author SmartCloudPlatform
 */
@Schema(description = "登录请求实体")
@Data
public class LoginRequest {
    
    /** 用户账号 */
    @Schema(description = "用户账号", example = "admin")
    @NotBlank(message = "用户账号不能为空")
    private String account;
    
    /** 用户密码 */
    @Schema(description = "用户密码", example = "123456")
    @NotBlank(message = "用户密码不能为空")
    private String password;
} 