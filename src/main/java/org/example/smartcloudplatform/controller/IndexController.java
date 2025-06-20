package org.example.smartcloudplatform.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.smartcloudplatform.common.AjaxResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页
 * 
 * @author SmartCloudPlatform
 */
@Tag(name = "首页管理", description = "系统首页相关接口")
@RestController
public class IndexController {
    
    /**
     * 系统首页
     *
     * @return 系统信息
     */
    @Operation(summary = "系统首页", description = "获取系统首页欢迎信息")
    @GetMapping("/")
    public AjaxResult index() {
        return AjaxResult.success("欢迎使用智能云平台管理系统");
    }

    /**
     * 健康检查
     */
    @Operation(summary = "健康检查", description = "检查系统运行状态")
    @GetMapping("/health")
    public AjaxResult health() {
        return AjaxResult.success("系统运行正常");
    }
} 