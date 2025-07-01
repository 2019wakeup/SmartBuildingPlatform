package org.example.smartcloudplatform.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.example.smartcloudplatform.common.AjaxResult;
import org.example.smartcloudplatform.common.TableDataInfo;
import org.example.smartcloudplatform.entity.HealthReport;
import org.example.smartcloudplatform.service.IHealthReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 健康报告Controller
 * 
 * @author SmartCloudPlatform
 */
@Tag(name = "健康报告管理", description = "健康报告数据接收和管理")
@Slf4j
@RestController
@RequestMapping("/health-report")
public class HealthReportController {

    @Autowired
    private IHealthReportService healthReportService;

    /**
     * 接收健康报告数据
     */
    @Operation(summary = "接收健康报告数据", description = "接收传来的个人健康状况报告数据")
    @PostMapping("/submit")
    public AjaxResult submitHealthReport(@RequestBody Map<String, Object> requestData) {
        try {
            // 提取请求参数
            String fingerPrint = (String) requestData.get("fingerPrint");
            String dataTakenStr = (String) requestData.get("dataTaken");
            String reportData = (String) requestData.get("reportData");
            Object stuIdObj = requestData.get("stuID");

            // 参数验证
            if (fingerPrint == null || fingerPrint.trim().isEmpty()) {
                return AjaxResult.error("指纹数据不能为空");
            }
            if (dataTakenStr == null || dataTakenStr.trim().isEmpty()) {
                return AjaxResult.error("数据采集时间不能为空");
            }
            if (reportData == null || reportData.trim().isEmpty()) {
                return AjaxResult.error("报告数据不能为空");
            }
            if (stuIdObj == null) {
                return AjaxResult.error("学生ID不能为空");
            }

            // 数据类型转换
            LocalDateTime dataTaken;
            try {
                dataTaken = LocalDateTime.parse(dataTakenStr.replace(" ", "T"));
            } catch (Exception e) {
                log.error("时间格式解析失败: {}", dataTakenStr, e);
                return AjaxResult.error("时间格式不正确，请使用格式：yyyy-MM-dd HH:mm:ss");
            }

            Long stuId;
            try {
                stuId = Long.valueOf(stuIdObj.toString());
            } catch (NumberFormatException e) {
                log.error("学生ID格式不正确: {}", stuIdObj, e);
                return AjaxResult.error("学生ID格式不正确");
            }

            // 处理健康报告数据
            boolean success = healthReportService.processHealthReportData(fingerPrint, dataTaken, reportData, stuId);
            
            if (success) {
                log.info("健康报告数据接收成功: stuId={}, dataTaken={}", stuId, dataTaken);
                return AjaxResult.success("健康报告数据接收成功");
            } else {
                return AjaxResult.error("健康报告数据处理失败");
            }
        } catch (Exception e) {
            log.error("接收健康报告数据异常", e);
            return AjaxResult.error("系统异常：" + e.getMessage());
        }
    }

    /**
     * 查询健康报告列表
     */
    @Operation(summary = "查询健康报告列表", description = "分页查询健康报告列表")
    @GetMapping("/list")
    public TableDataInfo list(HealthReport healthReport) {
        try {
            List<HealthReport> list = healthReportService.selectHealthReportList(healthReport);
            return TableDataInfo.getDataTable(list);
        } catch (Exception e) {
            log.error("查询健康报告列表失败", e);
            TableDataInfo result = new TableDataInfo();
            result.setCode(500);
            result.setMsg("查询失败: " + e.getMessage());
            return result;
        }
    }

    /**
     * 获取健康报告详细信息
     */
    @Operation(summary = "获取健康报告详细信息", description = "根据ID获取健康报告详细信息")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@Parameter(description = "报告ID", required = true) @PathVariable Long id) {
        try {
            HealthReport healthReport = healthReportService.selectHealthReportById(id);
            if (healthReport != null) {
                return AjaxResult.success(healthReport);
            } else {
                return AjaxResult.error("健康报告不存在");
            }
        } catch (Exception e) {
            log.error("获取健康报告详细信息失败: id={}", id, e);
            return AjaxResult.error("获取失败: " + e.getMessage());
        }
    }

    /**
     * 根据学生ID查询健康报告列表
     */
    @Operation(summary = "根据学生ID查询健康报告", description = "根据学生ID查询该学生的所有健康报告")
    @GetMapping("/student/{stuId}")
    public TableDataInfo getReportsByStuId(@Parameter(description = "学生ID", required = true) @PathVariable Long stuId) {
        try {
            List<HealthReport> list = healthReportService.selectHealthReportsByStuId(stuId);
            return TableDataInfo.getDataTable(list);
        } catch (Exception e) {
            log.error("根据学生ID查询健康报告失败: stuId={}", stuId, e);
            TableDataInfo result = new TableDataInfo();
            result.setCode(500);
            result.setMsg("查询失败: " + e.getMessage());
            return result;
        }
    }

    /**
     * 根据学生ID和时间范围查询健康报告
     */
    @Operation(summary = "根据学生ID和时间范围查询健康报告", description = "根据学生ID和时间范围查询健康报告")
    @GetMapping("/student/{stuId}/range")
    public TableDataInfo getReportsByStuIdAndTimeRange(
            @Parameter(description = "学生ID", required = true) @PathVariable Long stuId,
            @Parameter(description = "开始时间") @RequestParam(required = false) 
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @Parameter(description = "结束时间") @RequestParam(required = false) 
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        try {
            List<HealthReport> list = healthReportService.selectHealthReportsByStuIdAndTimeRange(stuId, startTime, endTime);
            return TableDataInfo.getDataTable(list);
        } catch (Exception e) {
            log.error("根据学生ID和时间范围查询健康报告失败: stuId={}, startTime={}, endTime={}", stuId, startTime, endTime, e);
            TableDataInfo result = new TableDataInfo();
            result.setCode(500);
            result.setMsg("查询失败: " + e.getMessage());
            return result;
        }
    }

    /**
     * 获取学生最新健康报告
     */
    @Operation(summary = "获取学生最新健康报告", description = "获取指定学生的最新健康报告")
    @GetMapping("/student/{stuId}/latest")
    public AjaxResult getLatestReportByStuId(@Parameter(description = "学生ID", required = true) @PathVariable Long stuId) {
        try {
            HealthReport healthReport = healthReportService.selectLatestHealthReportByStuId(stuId);
            if (healthReport != null) {
                return AjaxResult.success(healthReport);
            } else {
                return AjaxResult.error("该学生暂无健康报告数据");
            }
        } catch (Exception e) {
            log.error("获取学生最新健康报告失败: stuId={}", stuId, e);
            return AjaxResult.error("获取失败: " + e.getMessage());
        }
    }

    /**
     * 新增健康报告
     */
    @Operation(summary = "新增健康报告", description = "新增健康报告")
    @PostMapping
    public AjaxResult add(@Valid @RequestBody HealthReport healthReport) {
        try {
            int result = healthReportService.insertHealthReport(healthReport);
            if (result > 0) {
                return AjaxResult.success("新增成功");
            } else {
                return AjaxResult.error("新增失败");
            }
        } catch (Exception e) {
            log.error("新增健康报告失败", e);
            return AjaxResult.error("新增失败: " + e.getMessage());
        }
    }

    /**
     * 修改健康报告
     */
    @Operation(summary = "修改健康报告", description = "修改健康报告")
    @PutMapping
    public AjaxResult edit(@Valid @RequestBody HealthReport healthReport) {
        try {
            int result = healthReportService.updateHealthReport(healthReport);
            if (result > 0) {
                return AjaxResult.success("修改成功");
            } else {
                return AjaxResult.error("修改失败");
            }
        } catch (Exception e) {
            log.error("修改健康报告失败", e);
            return AjaxResult.error("修改失败: " + e.getMessage());
        }
    }

    /**
     * 删除健康报告
     */
    @Operation(summary = "删除健康报告", description = "根据ID删除健康报告")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@Parameter(description = "报告ID，多个用逗号分隔", required = true) @PathVariable Long[] ids) {
        try {
            int result = healthReportService.deleteHealthReportByIds(ids);
            if (result > 0) {
                return AjaxResult.success("删除成功");
            } else {
                return AjaxResult.error("删除失败");
            }
        } catch (Exception e) {
            log.error("删除健康报告失败", e);
            return AjaxResult.error("删除失败: " + e.getMessage());
        }
    }
} 