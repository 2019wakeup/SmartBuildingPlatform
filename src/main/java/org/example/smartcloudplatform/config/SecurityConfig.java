package org.example.smartcloudplatform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security配置
 * 
 * @author SmartCloudPlatform
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // 禁用CSRF
                .csrf(AbstractHttpConfigurer::disable)
                // 禁用认证，允许所有请求
                .authorizeHttpRequests(authz -> authz
                        .anyRequest().permitAll()
                )
                .build();
    }
} 