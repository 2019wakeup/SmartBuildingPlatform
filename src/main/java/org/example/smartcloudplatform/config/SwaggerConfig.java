package org.example.smartcloudplatform.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
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
                        .description("基于SpringBoot3 + MyBatis Plus的前后端分离管理系统")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("SmartCloudPlatform")
                                .email("admin@smartcloud.com")
                                .url("https://www.smartcloud.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")));
    }
} 