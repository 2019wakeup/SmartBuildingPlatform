package org.example.smartcloudplatform.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger配置类
 * 
 * @author SmartCloudPlatform
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI createRestApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("智能云平台管理系统API文档")
                        .description("基于SpringBoot3 + MyBatis Plus的前后端分离管理系统\n\n" +
                                "## 认证说明\n" +
                                "1. 先调用登录接口获取JWT令牌\n" +
                                "2. 点击右上角'Authorize'按钮\n" +
                                "3. 输入: Bearer {你的JWT令牌}\n" +
                                "4. 点击'Authorize'完成认证\n\n" +
                                "默认管理员账号: admin / admin123")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("SmartCloudPlatform")
                                .email("admin@smartcloud.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                // 添加JWT认证配置
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", 
                                new SecurityScheme()
                                        .name("Bearer Authentication")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("请输入JWT令牌，格式：Bearer {token}")));
    }
} 