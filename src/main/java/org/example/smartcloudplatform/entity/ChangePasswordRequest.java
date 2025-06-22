package org.example.smartcloudplatform.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 修改密码请求实体
 * 
 * @author SmartCloudPlatform
 */
@Schema(description = "修改密码请求实体")
@Data
public class ChangePasswordRequest {
    
    /** 旧密码 */
    @Schema(description = "旧密码", example = "123456")
    @NotBlank(message = "旧密码不能为空")
    private String oldPassword;
    
    /** 新密码 */
    @Schema(description = "新密码", example = "newPassword123")
    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, max = 20, message = "新密码长度必须在6到20个字符之间")
    private String newPassword;
} 