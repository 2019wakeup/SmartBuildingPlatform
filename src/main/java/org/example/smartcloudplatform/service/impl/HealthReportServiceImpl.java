package org.example.smartcloudplatform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.smartcloudplatform.entity.HealthReport;
import org.example.smartcloudplatform.mapper.HealthReportMapper;
import org.example.smartcloudplatform.service.IHealthReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 健康报告服务实现类
 * 
 * @author SmartCloudPlatform
 */
@Slf4j
@Service
public class HealthReportServiceImpl extends ServiceImpl<HealthReportMapper, HealthReport> implements IHealthReportService {

    @Autowired
    private HealthReportMapper healthReportMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public List<HealthReport> selectHealthReportList(HealthReport healthReport) {
        return healthReportMapper.selectHealthReportList(healthReport);
    }

    @Override
    public HealthReport selectHealthReportById(Long id) {
        return healthReportMapper.selectHealthReportById(id);
    }

    @Override
    public List<HealthReport> selectHealthReportsByStuId(Long stuId) {
        return healthReportMapper.selectHealthReportsByStuId(stuId);
    }

    @Override
    public List<HealthReport> selectHealthReportsByStuIdAndTimeRange(Long stuId, LocalDateTime startTime, LocalDateTime endTime) {
        return healthReportMapper.selectHealthReportsByStuIdAndTimeRange(stuId, startTime, endTime);
    }

    @Override
    public HealthReport selectLatestHealthReportByStuId(Long stuId) {
        return healthReportMapper.selectLatestHealthReportByStuId(stuId);
    }

    @Override
    @Transactional
    public int insertHealthReport(HealthReport healthReport) {
        healthReport.setCreateTime(LocalDateTime.now());
        return healthReportMapper.insertHealthReport(healthReport);
    }

    @Override
    @Transactional
    public int updateHealthReport(HealthReport healthReport) {
        healthReport.setUpdateTime(LocalDateTime.now());
        return healthReportMapper.updateHealthReport(healthReport);
    }

    @Override
    @Transactional
    public int deleteHealthReportByIds(Long[] ids) {
        return healthReportMapper.deleteHealthReportByIds(ids);
    }

    @Override
    @Transactional
    public int deleteHealthReportById(Long id) {
        return healthReportMapper.deleteHealthReportById(id);
    }

    @Override
    @Transactional
    public boolean processHealthReportData(String fingerPrint, LocalDateTime dataTaken, String reportData, Long stuId) {
        try {
            // 验证JSON格式
            if (reportData != null && !reportData.trim().isEmpty()) {
                try {
                    objectMapper.readTree(reportData);
                } catch (Exception e) {
                    log.error("报告数据JSON格式不正确: stuId={}, error={}", stuId, e.getMessage());
                    return false;
                }
            }

            // 创建健康报告对象
            HealthReport healthReport = new HealthReport();
            healthReport.setFingerPrint(fingerPrint);
            healthReport.setDataTaken(dataTaken);
            healthReport.setReportData(reportData);
            healthReport.setStuId(stuId);
            healthReport.setCreateBy("system");
            healthReport.setCreateTime(LocalDateTime.now());

            // 保存到数据库
            int result = healthReportMapper.insertHealthReport(healthReport);
            
            if (result > 0) {
                log.info("健康报告数据保存成功: stuId={}, reportId={}", stuId, healthReport.getId());
                return true;
            } else {
                log.error("健康报告数据保存失败: stuId={}", stuId);
                return false;
            }
        } catch (Exception e) {
            log.error("处理健康报告数据失败: stuId={}, error={}", stuId, e.getMessage(), e);
            return false;
        }
    }

    /**
     * 解析报告数据中的传感器数据
     * 
     * @param reportData JSON格式的报告数据
     * @return 解析后的数据统计信息
     */
    public String parseReportDataStats(String reportData) {
        try {
            JsonNode dataArray = objectMapper.readTree(reportData);
            if (dataArray.isArray() && dataArray.size() > 0) {
                double avgTemp = 0.0;
                double avgPressure = 0.0;
                double avgHumidity = 0.0;
                int count = 0;

                for (JsonNode dataPoint : dataArray) {
                    if (dataPoint.has("temperature_c") && !dataPoint.get("temperature_c").isNull()) {
                        avgTemp += dataPoint.get("temperature_c").asDouble();
                    }
                    if (dataPoint.has("pressure_hpa") && !dataPoint.get("pressure_hpa").isNull()) {
                        avgPressure += dataPoint.get("pressure_hpa").asDouble();
                    }
                    if (dataPoint.has("humidity_percent") && !dataPoint.get("humidity_percent").isNull()) {
                        avgHumidity += dataPoint.get("humidity_percent").asDouble();
                    }
                    count++;
                }

                if (count > 0) {
                    avgTemp /= count;
                    avgPressure /= count;
                    avgHumidity /= count;

                    return String.format("数据点数量: %d, 平均温度: %.2f°C, 平均压力: %.2f hPa, 平均湿度: %.2f%%", 
                            count, avgTemp, avgPressure, avgHumidity);
                }
            }
        } catch (Exception e) {
            log.error("解析报告数据失败: {}", e.getMessage());
        }
        return "数据解析失败";
    }
} 