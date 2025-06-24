package org.example.smartcloudplatform.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 登录响应实体
 * 
 * @author SmartCloudPlatform
 */
@Schema(description = "登录响应实体")
@Data
public class LoginResponse {
    
    /** JWT令牌 */
    @Schema(description = "JWT令牌")
    private String token;
    
    /** 用户信息 */
    @Schema(description = "用户信息")
    private User user;
    
    public LoginResponse(String token, User user) {
        this.token = token;
        this.user = user;
    }
} 