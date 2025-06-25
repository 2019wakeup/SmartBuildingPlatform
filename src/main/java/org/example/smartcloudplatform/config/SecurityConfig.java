package org.example.smartcloudplatform.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security配置
 * 
 * @author SmartCloudPlatform
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // 禁用CSRF
                .csrf(AbstractHttpConfigurer::disable)
                // 设置会话管理为无状态
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 配置请求权限
                .authorizeHttpRequests(authz -> authz
                        // 允许登录和Swagger相关接口无需认证
                        .requestMatchers("/auth/login", "/auth/logout").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**").permitAll()
                        .requestMatchers("/webjars/**", "/doc.html", "/favicon.ico").permitAll()
                        .requestMatchers("/uploads/**").permitAll()
                        // 允许IoT相关接口无需认证（用于设备数据上报和监控）
                        .requestMatchers("/iot/data/webhook/**", "/iot/data/receive").permitAll()
                        .requestMatchers("/iot/devices/statistics", "/iot/devices/status").permitAll()
                        .requestMatchers("/iot/data/latest/**").permitAll()
                        // 其他请求需要认证
                        .anyRequest().authenticated()
                )
                // 添加JWT过滤器
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
} 