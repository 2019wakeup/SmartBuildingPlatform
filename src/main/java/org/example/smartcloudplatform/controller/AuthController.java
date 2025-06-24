package org.example.smartcloudplatform.controller;

import cn.hutool.crypto.digest.BCrypt;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.smartcloudplatform.common.AjaxResult;
import org.example.smartcloudplatform.common.JwtUtils;
import org.example.smartcloudplatform.entity.ChangePasswordRequest;
import org.example.smartcloudplatform.entity.LoginRequest;
import org.example.smartcloudplatform.entity.LoginResponse;
import org.example.smartcloudplatform.entity.User;
import org.example.smartcloudplatform.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 认证控制器
 * 
 * @author SmartCloudPlatform
 */
@Tag(name = "用户认证", description = "用户登录、登出等认证相关操作")
@RestController
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    private IUserService userService;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    /**
     * 用户登录
     */
    @Operation(summary = "用户登录", description = "用户通过账号密码登录系统")
    @PostMapping("/login")
    public AjaxResult login(@Parameter(description = "登录请求信息") 
                           @Validated @RequestBody LoginRequest loginRequest) {
        try {
            // 用户登录验证
            User user = userService.loginUser(loginRequest.getAccount(), loginRequest.getPassword());
            if (user == null) {
                return AjaxResult.error("用户名或密码错误");
            }
            
            // 生成JWT令牌
            String token = jwtUtils.generateToken(user.getUserId(), user.getAccount());
            
            // 清除密码信息
            user.setPassword(null);
            
            // 返回登录成功信息
            LoginResponse loginResponse = new LoginResponse(token, user);
            return AjaxResult.success("登录成功", loginResponse);
            
        } catch (Exception e) {
            return AjaxResult.error("登录失败：" + e.getMessage());
        }
    }
    
    /**
     * 用户登出
     */
    @Operation(summary = "用户登出", description = "用户登出系统")
    @PostMapping("/logout")
    public AjaxResult logout(HttpServletRequest request) {
        // JWT是无状态的，客户端删除token即可实现登出
        // 这里可以实现token黑名单机制（可选）
        return AjaxResult.success("登出成功");
    }
    
    /**
     * 获取当前用户信息
     */
    @Operation(summary = "获取当前用户信息", description = "根据JWT令牌获取当前登录用户的信息")
    @GetMapping("/userinfo")
    public AjaxResult getUserInfo(HttpServletRequest request) {
        try {
            String token = getTokenFromRequest(request);
            if (token == null) {
                return AjaxResult.error("未登录");
            }
            
            if (!jwtUtils.validateToken(token)) {
                return AjaxResult.error("令牌无效");
            }
            
            Long userId = jwtUtils.getUserIdFromToken(token);
            User user = userService.selectUserById(userId);
            if (user != null) {
                user.setPassword(null); // 清除密码信息
                return AjaxResult.success("获取用户信息成功", user);
            } else {
                return AjaxResult.error("用户不存在");
            }
        } catch (Exception e) {
            return AjaxResult.error("获取用户信息失败：" + e.getMessage());
        }
    }
    
    /**
     * 修改密码
     */
    @Operation(summary = "修改密码", description = "用户修改自己的登录密码")
    @PostMapping("/changePassword")
    public AjaxResult changePassword(@Parameter(description = "修改密码请求信息") 
                                   @Validated @RequestBody ChangePasswordRequest request,
                                   HttpServletRequest httpRequest) {
        try {
            String token = getTokenFromRequest(httpRequest);
            if (token == null) {
                return AjaxResult.error("未登录");
            }
            
            if (!jwtUtils.validateToken(token)) {
                return AjaxResult.error("令牌无效");
            }
            
            Long userId = jwtUtils.getUserIdFromToken(token);
            User user = userService.selectUserById(userId);
            if (user == null) {
                return AjaxResult.error("用户不存在");
            }
            
            // 验证旧密码
            if (!BCrypt.checkpw(request.getOldPassword(), user.getPassword())) {
                return AjaxResult.error("旧密码错误");
            }
            
            // 更新密码
            user.setPassword(request.getNewPassword());
            int result = userService.resetPwd(user);
            if (result > 0) {
                return AjaxResult.success("密码修改成功");
            } else {
                return AjaxResult.error("密码修改失败");
            }
        } catch (Exception e) {
            return AjaxResult.error("修改密码失败：" + e.getMessage());
        }
    }
    
    /**
     * 从请求中获取JWT令牌
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
} 