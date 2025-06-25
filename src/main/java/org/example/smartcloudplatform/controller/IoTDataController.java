package org.example.smartcloudplatform.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.example.smartcloudplatform.common.AjaxResult;
import org.example.smartcloudplatform.common.TableDataInfo;
import org.example.smartcloudplatform.entity.IoTDeviceStatus;
import org.example.smartcloudplatform.entity.IoTSensorData;
import org.example.smartcloudplatform.service.IIoTDeviceStatusService;
import org.example.smartcloudplatform.service.IIoTSensorDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 物联网数据控制器
 * 
 * @author SmartCloudPlatform
 */
@Tag(name = "物联网数据管理", description = "物联网设备数据接收和查询接口")
@Slf4j
@RestController
@RequestMapping("/iot")
public class IoTDataController {

    @Autowired
    private IIoTSensorDataService sensorDataService;

    @Autowired
    private IIoTDeviceStatusService deviceStatusService;

    /**
     * 接收设备数据 - 用于ThingsCloud平台转发
     */
    @Operation(summary = "接收设备数据", description = "接收来自ThingsCloud平台转发的设备数据")
    @PostMapping("/data/receive")
    public AjaxResult receiveDeviceData(
            @Parameter(description = "设备ID", required = true) @RequestParam String deviceId,
            @RequestBody String messageBody,
            HttpServletRequest request) {
        
        try {
            log.info("接收到设备数据: deviceId={}, remoteAddr={}", deviceId, request.getRemoteAddr());
            log.debug("设备数据内容: {}", messageBody);

            // 处理设备消息
            boolean success = sensorDataService.processDeviceMessage(deviceId, messageBody);
            
            if (success) {
                return AjaxResult.success("设备数据处理成功");
            } else {
                return AjaxResult.error("设备数据处理失败");
            }
        } catch (Exception e) {
            log.error("接收设备数据失败: deviceId={}, error={}", deviceId, e.getMessage(), e);
            return AjaxResult.error("接收设备数据失败: " + e.getMessage());
        }
    }

    /**
     * 通用数据接收接口 - 支持JSON格式
     */
    @Operation(summary = "通用数据接收", description = "通用的设备数据接收接口，支持JSON格式")
    @PostMapping("/data/webhook/{deviceId}")
    public AjaxResult webhookReceiveData(
            @Parameter(description = "设备ID", required = true) @PathVariable String deviceId,
            @RequestBody Map<String, Object> data,
            HttpServletRequest request) {
        
        try {
            log.info("Webhook接收设备数据: deviceId={}, remoteAddr={}", deviceId, request.getRemoteAddr());
            
                         // 转换为JSON字符串
             ObjectMapper mapper = new ObjectMapper();
             String jsonMessage = mapper.writeValueAsString(data);
            
            // 处理设备消息
            boolean success = sensorDataService.processDeviceMessage(deviceId, jsonMessage);
            
            if (success) {
                return AjaxResult.success("数据接收成功");
            } else {
                return AjaxResult.error("数据处理失败");
            }
        } catch (Exception e) {
            log.error("Webhook接收数据失败: deviceId={}, error={}", deviceId, e.getMessage(), e);
            return AjaxResult.error("数据接收失败: " + e.getMessage());
        }
    }

    /**
     * 查询设备历史数据
     */
    @Operation(summary = "查询设备历史数据", description = "根据设备ID和时间范围查询历史数据")
    @GetMapping("/data/history/{deviceId}")
    public TableDataInfo getDeviceHistoryData(
            @Parameter(description = "设备ID", required = true) @PathVariable String deviceId,
            @Parameter(description = "开始时间") @RequestParam(required = false) 
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @Parameter(description = "结束时间") @RequestParam(required = false) 
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @Parameter(description = "数据条数限制") @RequestParam(defaultValue = "100") int limit) {
        
        try {
            List<IoTSensorData> dataList;
            
            if (startTime != null && endTime != null) {
                // 按时间范围查询
                dataList = sensorDataService.getDataByDeviceIdAndTimeRange(deviceId, startTime, endTime);
            } else {
                // 查询最近N条数据
                dataList = sensorDataService.getRecentDataByDeviceId(deviceId, limit);
            }
            
            return TableDataInfo.getDataTable(dataList);
        } catch (Exception e) {
            log.error("查询设备历史数据失败: deviceId={}, error={}", deviceId, e.getMessage(), e);
            TableDataInfo result = new TableDataInfo();
            result.setCode(500);
            result.setMsg("查询失败: " + e.getMessage());
            return result;
        }
    }

    /**
     * 获取设备最新数据
     */
    @Operation(summary = "获取设备最新数据", description = "获取指定设备的最新传感器数据")
    @GetMapping("/data/latest/{deviceId}")
    public AjaxResult getLatestDeviceData(
            @Parameter(description = "设备ID", required = true) @PathVariable String deviceId) {
        
        try {
            IoTSensorData latestData = sensorDataService.getLatestDataByDeviceId(deviceId);
            
            if (latestData != null) {
                return AjaxResult.success("查询成功", latestData);
            } else {
                return AjaxResult.error("未找到设备数据");
            }
        } catch (Exception e) {
            log.error("获取设备最新数据失败: deviceId={}, error={}", deviceId, e.getMessage(), e);
            return AjaxResult.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 获取所有设备状态
     */
    @Operation(summary = "获取所有设备状态", description = "获取所有设备的当前状态信息")
    @GetMapping("/devices/status")
    public TableDataInfo getAllDeviceStatus() {
        try {
            List<IoTDeviceStatus> deviceList = deviceStatusService.list();
            return TableDataInfo.getDataTable(deviceList);
        } catch (Exception e) {
            log.error("获取设备状态失败: error={}", e.getMessage(), e);
            TableDataInfo result = new TableDataInfo();
            result.setCode(500);
            result.setMsg("查询失败: " + e.getMessage());
            return result;
        }
    }

    /**
     * 获取在线设备列表
     */
    @Operation(summary = "获取在线设备", description = "获取所有在线设备列表")
    @GetMapping("/devices/online")
    public TableDataInfo getOnlineDevices() {
        try {
            List<IoTDeviceStatus> onlineDevices = deviceStatusService.getOnlineDevices();
            return TableDataInfo.getDataTable(onlineDevices);
        } catch (Exception e) {
            log.error("获取在线设备失败: error={}", e.getMessage(), e);
            TableDataInfo result = new TableDataInfo();
            result.setCode(500);
            result.setMsg("查询失败: " + e.getMessage());
            return result;
        }
    }

    /**
     * 获取离线设备列表
     */
    @Operation(summary = "获取离线设备", description = "获取所有离线设备列表")
    @GetMapping("/devices/offline")
    public TableDataInfo getOfflineDevices() {
        try {
            List<IoTDeviceStatus> offlineDevices = deviceStatusService.getOfflineDevices();
            return TableDataInfo.getDataTable(offlineDevices);
        } catch (Exception e) {
            log.error("获取离线设备失败: error={}", e.getMessage(), e);
            TableDataInfo result = new TableDataInfo();
            result.setCode(500);
            result.setMsg("查询失败: " + e.getMessage());
            return result;
        }
    }

    /**
     * 设备注册
     */
    @Operation(summary = "设备注册", description = "注册新的物联网设备")
    @PostMapping("/devices/register")
    public AjaxResult registerDevice(
            @Parameter(description = "设备ID", required = true) @RequestParam String deviceId,
            @Parameter(description = "设备名称", required = true) @RequestParam String deviceName,
            @Parameter(description = "设备位置") @RequestParam(required = false) String deviceLocation) {
        
        try {
            boolean success = deviceStatusService.registerDevice(deviceId, deviceName, 
                    deviceLocation != null ? deviceLocation : "未知位置");
            
            if (success) {
                return AjaxResult.success("设备注册成功");
            } else {
                return AjaxResult.error("设备注册失败，设备可能已存在");
            }
        } catch (Exception e) {
            log.error("设备注册失败: deviceId={}, error={}", deviceId, e.getMessage(), e);
            return AjaxResult.error("设备注册失败: " + e.getMessage());
        }
    }

    /**
     * 获取设备统计信息
     */
    @Operation(summary = "获取设备统计", description = "获取设备总数、在线数、离线数等统计信息")
    @GetMapping("/devices/statistics")
    public AjaxResult getDeviceStatistics() {
        try {
            IIoTDeviceStatusService.DeviceStatistics statistics = deviceStatusService.getDeviceStatistics();
            return AjaxResult.success("查询成功", statistics);
        } catch (Exception e) {
            log.error("获取设备统计失败: error={}", e.getMessage(), e);
            return AjaxResult.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 手动检查离线设备
     */
    @Operation(summary = "检查离线设备", description = "手动检查并更新离线设备状态")
    @PostMapping("/devices/check-offline")
    public AjaxResult checkOfflineDevices(
            @Parameter(description = "离线阈值（分钟）") @RequestParam(defaultValue = "10") int thresholdMinutes) {
        
        try {
            int updatedCount = deviceStatusService.checkAndUpdateOfflineDevices(thresholdMinutes);
            return AjaxResult.success("检查完成，更新离线设备数: " + updatedCount);
        } catch (Exception e) {
            log.error("检查离线设备失败: error={}", e.getMessage(), e);
            return AjaxResult.error("检查失败: " + e.getMessage());
        }
    }
} 