package org.example.smartcloudplatform.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Swagger文档访问控制器
 * 
 * @author SmartCloudPlatform
 */
@Hidden
@Controller
public class SwaggerController {
    
    /**
     * 重定向到Swagger UI
     */
    @GetMapping("/doc")
    public String swagger() {
        return "redirect:/swagger-ui.html";
    }
    
    /**
     * 重定向到Swagger UI
     */
    @GetMapping("/swagger")
    public String doc() {
        return "redirect:/swagger-ui.html";
    }
} 