package org.example.smartcloudplatform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.smartcloudplatform.entity.IoTSensorData;
import org.example.smartcloudplatform.mapper.IoTSensorDataMapper;
import org.example.smartcloudplatform.service.IIoTSensorDataService;
import org.example.smartcloudplatform.service.IIoTDeviceStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 物联网传感器数据服务实现类
 * 
 * @author SmartCloudPlatform
 */
@Slf4j
@Service
public class IoTSensorDataServiceImpl extends ServiceImpl<IoTSensorDataMapper, IoTSensorData> implements IIoTSensorDataService {

    @Autowired
    private IoTSensorDataMapper iotSensorDataMapper;

    @Autowired
    private IIoTDeviceStatusService deviceStatusService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // 数据变化阈值配置
    private static final int CO2_THRESHOLD = 100;  // CO2变化阈值 (ppm)
    private static final int TVOC_THRESHOLD = 50;  // TVOC变化阈值 (ppb)
    private static final BigDecimal TEMPERATURE_THRESHOLD = new BigDecimal("2.0");  // 温度变化阈值 (℃)
    private static final BigDecimal HUMIDITY_THRESHOLD = new BigDecimal("5.0");     // 湿度变化阈值 (%)

    @Override
    @Transactional
    public boolean processDeviceMessage(String deviceId, String rawMessage) {
        try {
            log.info("处理设备消息: deviceId={}, message={}", deviceId, rawMessage);
            
            // 解析JSON消息
            IoTSensorData sensorData = parseMessage(deviceId, rawMessage);
            if (sensorData == null) {
                log.error("消息解析失败: deviceId={}, message={}", deviceId, rawMessage);
                return false;
            }

            // 检查是否需要存储数据（基于变化阈值）
            if (shouldStoreData(deviceId, sensorData)) {
                // 保存传感器数据
                save(sensorData);
                log.info("传感器数据已保存: deviceId={}", deviceId);
            } else {
                log.debug("数据变化未超过阈值，跳过存储: deviceId={}", deviceId);
            }

            // 更新设备状态
            deviceStatusService.updateDeviceStatus(sensorData);

            return true;
        } catch (Exception e) {
            log.error("处理设备消息失败: deviceId={}, error={}", deviceId, e.getMessage(), e);
            return false;
        }
    }

    /**
     * 解析设备消息
     */
    private IoTSensorData parseMessage(String deviceId, String rawMessage) {
        try {
            JsonNode rootNode = objectMapper.readTree(rawMessage);
            
            IoTSensorData sensorData = new IoTSensorData(deviceId);
            sensorData.setRawMessage(rawMessage);

            // 尝试新格式（直接在根节点）
            JsonNode dataNode = rootNode;
            
            // 如果有status字段，则使用旧格式
            if (rootNode.has("status")) {
                dataNode = rootNode.get("status");
                log.debug("使用旧格式解析: deviceId={}", deviceId);
            } else {
                log.debug("使用新格式解析: deviceId={}", deviceId);
            }

            // 解析CO2浓度 - 支持多种字段名
            String co2Str = getFieldValue(dataNode, "co2_ppm", "co2 ppm");
            if (co2Str != null && !co2Str.isEmpty()) {
                // 移除可能的逗号和其他非数字字符
                co2Str = co2Str.replaceAll("[^0-9]", "");
                if (!co2Str.isEmpty()) {
                    sensorData.setCo2Ppm(Integer.parseInt(co2Str));
                }
            }

            // 解析TVOC浓度 - 支持多种字段名
            String tvocStr = getFieldValue(dataNode, "tvoc_ppb", "tvoc ppb");
            if (tvocStr != null && !tvocStr.isEmpty()) {
                tvocStr = tvocStr.replaceAll("[^0-9]", "");
                if (!tvocStr.isEmpty()) {
                    sensorData.setTvocPpb(Integer.parseInt(tvocStr));
                }
            }

            // 解析芯片温度 - 支持多种字段名
            String chipTempStr = getFieldValue(dataNode, "chip_temperature", "chip temperature");
            if (chipTempStr != null && !chipTempStr.isEmpty()) {
                chipTempStr = chipTempStr.replaceAll("[^0-9.]", "");
                if (!chipTempStr.isEmpty()) {
                    sensorData.setChipTemperature(new BigDecimal(chipTempStr));
                }
            }

            // 解析环境温度 - 支持多种字段名
            String envTempStr = getFieldValue(dataNode, "env_temperature", "env temperature");
            if (envTempStr != null && !envTempStr.isEmpty()) {
                envTempStr = envTempStr.replaceAll("[^0-9.]", "");
                if (!envTempStr.isEmpty()) {
                    sensorData.setEnvTemperature(new BigDecimal(envTempStr));
                }
            }

            // 解析环境湿度 - 支持多种字段名
            String humidityStr = getFieldValue(dataNode, "env_humidity", "env humidity");
            if (humidityStr != null && !humidityStr.isEmpty()) {
                humidityStr = humidityStr.replaceAll("[^0-9.]", "");
                if (!humidityStr.isEmpty()) {
                    sensorData.setEnvHumidity(new BigDecimal(humidityStr));
                }
            }

            return sensorData;
        } catch (Exception e) {
            log.error("解析消息失败: deviceId={}, message={}, error={}", deviceId, rawMessage, e.getMessage(), e);
            return null;
        }
    }

    /**
     * 获取字段值，支持多种字段名
     */
    private String getFieldValue(JsonNode node, String... fieldNames) {
        for (String fieldName : fieldNames) {
            JsonNode fieldNode = node.get(fieldName);
            if (fieldNode != null && !fieldNode.isNull()) {
                return fieldNode.asText();
            }
        }
        return null;
    }

    @Override
    public boolean shouldStoreData(String deviceId, IoTSensorData newData) {
        try {
            // 获取最新数据
            IoTSensorData latestData = iotSensorDataMapper.selectLatestByDeviceId(deviceId);
            
            // 如果没有历史数据，直接存储
            if (latestData == null) {
                return true;
            }

            // 检查各项指标是否超过阈值
            boolean shouldStore = false;

            // CO2变化检查
            if (newData.getCo2Ppm() != null && latestData.getCo2Ppm() != null) {
                int co2Diff = Math.abs(newData.getCo2Ppm() - latestData.getCo2Ppm());
                if (co2Diff >= CO2_THRESHOLD) {
                    shouldStore = true;
                    log.debug("CO2变化超过阈值: deviceId={}, diff={}", deviceId, co2Diff);
                }
            }

            // TVOC变化检查
            if (newData.getTvocPpb() != null && latestData.getTvocPpb() != null) {
                int tvocDiff = Math.abs(newData.getTvocPpb() - latestData.getTvocPpb());
                if (tvocDiff >= TVOC_THRESHOLD) {
                    shouldStore = true;
                    log.debug("TVOC变化超过阈值: deviceId={}, diff={}", deviceId, tvocDiff);
                }
            }

            // 环境温度变化检查
            if (newData.getEnvTemperature() != null && latestData.getEnvTemperature() != null) {
                BigDecimal tempDiff = newData.getEnvTemperature().subtract(latestData.getEnvTemperature()).abs();
                if (tempDiff.compareTo(TEMPERATURE_THRESHOLD) >= 0) {
                    shouldStore = true;
                    log.debug("环境温度变化超过阈值: deviceId={}, diff={}", deviceId, tempDiff);
                }
            }

            // 环境湿度变化检查
            if (newData.getEnvHumidity() != null && latestData.getEnvHumidity() != null) {
                BigDecimal humidityDiff = newData.getEnvHumidity().subtract(latestData.getEnvHumidity()).abs();
                if (humidityDiff.compareTo(HUMIDITY_THRESHOLD) >= 0) {
                    shouldStore = true;
                    log.debug("环境湿度变化超过阈值: deviceId={}, diff={}", deviceId, humidityDiff);
                }
            }

            // 检查时间间隔（超过30分钟强制存储一次）
            LocalDateTime lastTime = latestData.getReceivedTime();
            LocalDateTime now = LocalDateTime.now();
            if (lastTime.plusMinutes(30).isBefore(now)) {
                shouldStore = true;
                log.debug("超过30分钟未存储，强制存储: deviceId={}", deviceId);
            }

            return shouldStore;
        } catch (Exception e) {
            log.error("检查存储条件失败: deviceId={}, error={}", deviceId, e.getMessage(), e);
            // 出错时默认存储
            return true;
        }
    }

    @Override
    public List<IoTSensorData> getDataByDeviceIdAndTimeRange(String deviceId, LocalDateTime startTime, LocalDateTime endTime) {
        return iotSensorDataMapper.selectByDeviceIdAndTimeRange(deviceId, startTime, endTime);
    }

    @Override
    public IoTSensorData getLatestDataByDeviceId(String deviceId) {
        return iotSensorDataMapper.selectLatestByDeviceId(deviceId);
    }

    @Override
    public List<IoTSensorData> getRecentDataByDeviceId(String deviceId, int limit) {
        return iotSensorDataMapper.selectRecentByDeviceId(deviceId, limit);
    }

    @Override
    @Transactional
    public int cleanHistoryData(LocalDateTime beforeTime) {
        log.info("开始清理历史数据: beforeTime={}", beforeTime);
        int deletedCount = iotSensorDataMapper.deleteHistoryDataBefore(beforeTime);
        log.info("历史数据清理完成: 删除记录数={}", deletedCount);
        return deletedCount;
    }
} 