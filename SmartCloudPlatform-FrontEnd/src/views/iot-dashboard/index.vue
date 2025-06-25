<template>
  <div class="iot-dashboard">
    <!-- 统计卡片 -->
    <div class="stats-cards">
      <el-row :gutter="20">
        <el-col :span="8">
          <el-card class="stats-card">
            <div class="stats-content">
              <div class="stats-icon total">
                <el-icon><Monitor /></el-icon>
              </div>
              <div class="stats-info">
                <div class="stats-value">{{ statistics.total_devices }}</div>
                <div class="stats-label">设备总数</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card class="stats-card">
            <div class="stats-content">
              <div class="stats-icon online">
                <el-icon><CircleCheck /></el-icon>
              </div>
              <div class="stats-info">
                <div class="stats-value">{{ statistics.online_devices }}</div>
                <div class="stats-label">在线设备</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card class="stats-card">
            <div class="stats-content">
              <div class="stats-icon offline">
                <el-icon><CircleClose /></el-icon>
              </div>
              <div class="stats-info">
                <div class="stats-value">{{ statistics.offline_devices }}</div>
                <div class="stats-label">离线设备</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 设备选择区域 -->
    <div class="device-selection">
      <el-card>
        <template #header>
          <div class="card-header">
            <span>设备选择</span>
            <el-button @click="refreshData" :loading="loading">
              <el-icon><Refresh /></el-icon>
              刷新数据
            </el-button>
          </div>
        </template>
        <div class="device-selector">
          <el-select v-model="selectedDevice" placeholder="选择要监控的设备" @change="onDeviceChange" style="width: 300px;">
            <el-option
              v-for="device in deviceList"
              :key="device.device_id"
              :label="`${device.device_name} (${device.device_id})`"
              :value="device.device_id"
            >
              <div style="display: flex; justify-content: space-between; align-items: center;">
                <span>{{ device.device_name }}</span>
                <el-tag 
                  :type="device.device_status === 'online' ? 'success' : 'danger'"
                  size="small"
                >
                  {{ device.device_status === 'online' ? '在线' : '离线' }}
                </el-tag>
              </div>
            </el-option>
          </el-select>
          <span v-if="selectedDevice" class="device-info-text">
            当前监控设备：{{ deviceList.find(d => d.device_id === selectedDevice)?.device_name }}
          </span>
        </div>
      </el-card>
    </div>

    <!-- 实时数据展示区域 -->
    <div class="dashboard-section" v-if="selectedDevice">
      <el-row :gutter="20">
        <!-- CO2浓度仪表盘 -->
        <el-col :span="12">
          <el-card class="metric-card co2-card">
            <template #header>
              <div class="metric-header">
                <div class="metric-icon co2">
                  <el-icon><Cloudy /></el-icon>
                </div>
                <div class="metric-info">
                  <h3>CO₂浓度</h3>
                  <p>二氧化碳浓度监测</p>
                </div>
              </div>
            </template>
            <div class="metric-content">
              <div class="gauge-container">
                <v-chart :option="co2GaugeOption" :loading="chartLoading" />
              </div>
              <div class="metric-details">
                <div class="current-reading">
                  <span class="value">{{ currentData?.co2_ppm || 0 }}</span>
                  <span class="unit">ppm</span>
                </div>
                <div class="status-indicator" :class="getCO2Status(currentData?.co2_ppm)">
                  {{ getCO2StatusText(currentData?.co2_ppm) }}
                </div>
              </div>
            </div>
          </el-card>
        </el-col>

        <!-- TVOC浓度仪表盘 -->
        <el-col :span="12">
          <el-card class="metric-card tvoc-card">
            <template #header>
              <div class="metric-header">
                <div class="metric-icon tvoc">
                  <el-icon><MagicStick /></el-icon>
                </div>
                <div class="metric-info">
                  <h3>TVOC浓度</h3>
                  <p>总挥发性有机化合物</p>
                </div>
              </div>
            </template>
            <div class="metric-content">
              <div class="gauge-container">
                <v-chart :option="tvocGaugeOption" :loading="chartLoading" />
              </div>
              <div class="metric-details">
                <div class="current-reading">
                  <span class="value">{{ currentData?.tvoc_ppb || 0 }}</span>
                  <span class="unit">ppb</span>
                </div>
                <div class="status-indicator" :class="getTVOCStatus(currentData?.tvoc_ppb)">
                  {{ getTVOCStatusText(currentData?.tvoc_ppb) }}
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <el-row :gutter="20" style="margin-top: 20px;">
        <!-- 环境温度显示 -->
        <el-col :span="12">
          <el-card class="metric-card temperature-card">
            <template #header>
              <div class="metric-header">
                <div class="metric-icon temperature">
                  <el-icon><Sunny /></el-icon>
                </div>
                <div class="metric-info">
                  <h3>环境温度</h3>
                  <p>当前环境温度监测</p>
                </div>
              </div>
            </template>
            <div class="metric-content">
              <div class="thermometer-container">
                <div class="thermometer">
                  <div class="thermometer-scale">
                    <div class="scale-mark" v-for="temp in [0, 10, 20, 30, 40]" :key="temp" :style="{bottom: temp * 2 + '%'}">
                      <span>{{ temp }}°</span>
                    </div>
                  </div>
                  <div class="thermometer-tube">
                    <div class="thermometer-mercury" :style="{height: getTemperatureHeight(currentData?.env_temperature)}"></div>
                  </div>
                </div>
              </div>
              <div class="metric-details">
                <div class="current-reading">
                  <span class="value">{{ currentData?.env_temperature || 0 }}</span>
                  <span class="unit">℃</span>
                </div>
                <div class="status-indicator" :class="getTemperatureStatus(currentData?.env_temperature)">
                  {{ getTemperatureStatusText(currentData?.env_temperature) }}
                </div>
              </div>
            </div>
          </el-card>
        </el-col>

        <!-- 环境湿度显示 -->
        <el-col :span="12">
          <el-card class="metric-card humidity-card">
            <template #header>
              <div class="metric-header">
                <div class="metric-icon humidity">
                  <el-icon><Drizzling /></el-icon>
                </div>
                <div class="metric-info">
                  <h3>环境湿度</h3>
                  <p>相对湿度监测</p>
                </div>
              </div>
            </template>
            <div class="metric-content">
              <div class="humidity-display">
                <div class="water-drop">
                  <div class="water-level" :style="{height: (currentData?.env_humidity || 0) + '%'}"></div>
                  <div class="drop-value">
                    <span class="value">{{ currentData?.env_humidity || 0 }}</span>
                    <span class="unit">%</span>
                  </div>
                </div>
                <div class="humidity-scale">
                  <div class="scale-item" v-for="level in [0, 25, 50, 75, 100]" :key="level">
                    <div class="scale-line"></div>
                    <span class="scale-text">{{ level }}%</span>
                  </div>
                </div>
              </div>
              <div class="metric-details">
                <div class="status-indicator" :class="getHumidityStatus(currentData?.env_humidity)">
                  {{ getHumidityStatusText(currentData?.env_humidity) }}
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 无数据提示 -->
    <div v-if="!selectedDevice" class="no-device-tip">
      <el-empty description="请选择要监控的设备" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { Refresh, Cloudy, MagicStick, Sunny, Drizzling } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { GaugeChart } from 'echarts/charts'
import {
  TitleComponent,
  TooltipComponent,
  LegendComponent
} from 'echarts/components'
import VChart from 'vue-echarts'
import {
  getAllDeviceStatus,
  getDeviceStatistics,
  getDeviceHistoryData,
  getLatestDeviceData,
  type IoTDeviceStatus,
  type IoTSensorData,
  type DeviceStatistics
} from '@/api/iot'

// 注册ECharts组件
use([
  CanvasRenderer,
  GaugeChart,
  TitleComponent,
  TooltipComponent,
  LegendComponent
])

const router = useRouter()

// 响应式数据
const loading = ref(false)
const chartLoading = ref(false)
const deviceList = ref<IoTDeviceStatus[]>([])
const selectedDevice = ref<string>('')
const currentData = ref<IoTSensorData | null>(null)
const historyData = ref<IoTSensorData[]>([])
const statistics = ref<DeviceStatistics>({
  total_devices: 0,
  online_devices: 0,
  offline_devices: 0
})

// CO2仪表盘配置
const co2GaugeOption = computed(() => {
  const value = currentData.value?.co2_ppm || 0
  return {
    series: [
      {
        type: 'gauge',
        center: ['50%', '65%'],
        startAngle: 200,
        endAngle: -20,
        min: 0,
        max: 5000,
        splitNumber: 5,
        itemStyle: {
          color: '#ff6b6b'
        },
        progress: {
          show: true,
          width: 8
        },
        pointer: {
          show: false
        },
        axisLine: {
          lineStyle: {
            width: 8
          }
        },
        axisTick: {
          distance: -45,
          splitNumber: 5,
          lineStyle: {
            width: 2,
            color: '#999'
          }
        },
        splitLine: {
          distance: -52,
          length: 14,
          lineStyle: {
            width: 3,
            color: '#999'
          }
        },
        axisLabel: {
          distance: -20,
          color: '#999',
          fontSize: 10
        },
        anchor: {
          show: false
        },
        title: {
          show: false
        },
        detail: {
          valueAnimation: true,
          width: '60%',
          lineHeight: 40,
          borderRadius: 8,
          offsetCenter: [0, '-15%'],
          fontSize: 24,
          fontWeight: 'bolder',
          formatter: '{value} ppm',
          color: '#ff6b6b'
        },
        data: [
          {
            value: value
          }
        ]
      }
    ]
  }
})

// TVOC仪表盘配置
const tvocGaugeOption = computed(() => {
  const value = currentData.value?.tvoc_ppb || 0
  return {
    series: [
      {
        type: 'gauge',
        center: ['50%', '65%'],
        startAngle: 200,
        endAngle: -20,
        min: 0,
        max: 1000,
        splitNumber: 5,
        itemStyle: {
          color: '#4ecdc4'
        },
        progress: {
          show: true,
          width: 8
        },
        pointer: {
          show: false
        },
        axisLine: {
          lineStyle: {
            width: 8
          }
        },
        axisTick: {
          distance: -45,
          splitNumber: 5,
          lineStyle: {
            width: 2,
            color: '#999'
          }
        },
        splitLine: {
          distance: -52,
          length: 14,
          lineStyle: {
            width: 3,
            color: '#999'
          }
        },
        axisLabel: {
          distance: -20,
          color: '#999',
          fontSize: 10
        },
        anchor: {
          show: false
        },
        title: {
          show: false
        },
        detail: {
          valueAnimation: true,
          width: '60%',
          lineHeight: 40,
          borderRadius: 8,
          offsetCenter: [0, '-15%'],
          fontSize: 24,
          fontWeight: 'bolder',
          formatter: '{value} ppb',
          color: '#4ecdc4'
        },
        data: [
          {
            value: value
          }
        ]
      }
    ]
  }
})

// 状态判断函数
const getCO2Status = (value: number | undefined) => {
  if (!value) return 'unknown'
  if (value < 1000) return 'good'
  if (value < 2000) return 'moderate'
  return 'poor'
}

const getCO2StatusText = (value: number | undefined) => {
  if (!value) return '未知'
  if (value < 1000) return '优秀'
  if (value < 2000) return '良好'
  return '较差'
}

const getTVOCStatus = (value: number | undefined) => {
  if (!value) return 'unknown'
  if (value < 150) return 'good'
  if (value < 500) return 'moderate'
  return 'poor'
}

const getTVOCStatusText = (value: number | undefined) => {
  if (!value) return '未知'
  if (value < 150) return '优秀'
  if (value < 500) return '良好'
  return '较差'
}

const getTemperatureStatus = (value: number | undefined) => {
  if (!value) return 'unknown'
  if (value >= 18 && value <= 26) return 'good'
  if (value >= 15 && value <= 30) return 'moderate'
  return 'poor'
}

const getTemperatureStatusText = (value: number | undefined) => {
  if (!value) return '未知'
  if (value >= 18 && value <= 26) return '舒适'
  if (value >= 15 && value <= 30) return '适宜'
  return '异常'
}

const getHumidityStatus = (value: number | undefined) => {
  if (!value) return 'unknown'
  if (value >= 40 && value <= 60) return 'good'
  if (value >= 30 && value <= 70) return 'moderate'
  return 'poor'
}

const getHumidityStatusText = (value: number | undefined) => {
  if (!value) return '未知'
  if (value >= 40 && value <= 60) return '舒适'
  if (value >= 30 && value <= 70) return '适宜'
  return '异常'
}

const getTemperatureHeight = (value: number | undefined) => {
  if (!value) return '0%'
  const height = Math.min(Math.max((value / 40) * 100, 0), 100)
  return height + '%'
}

// 方法
const loadDeviceList = async () => {
  try {
    loading.value = true
    const response = await getAllDeviceStatus()
    deviceList.value = response.data.rows || []
    
    // 自动选择第一个设备
    if (deviceList.value.length > 0 && !selectedDevice.value) {
      selectedDevice.value = deviceList.value[0].device_id
      await loadDeviceData()
    }
  } catch (error) {
    console.error('加载设备列表失败:', error)
    ElMessage.error('加载设备列表失败')
  } finally {
    loading.value = false
  }
}

const loadStatistics = async () => {
  try {
    const response = await getDeviceStatistics()
    statistics.value = response.data || {
      total_devices: 0,
      online_devices: 0,
      offline_devices: 0
    }
  } catch (error) {
    console.error('加载统计信息失败:', error)
  }
}

const loadDeviceData = async () => {
  if (!selectedDevice.value) return
  
  try {
    chartLoading.value = true
    
    // 加载最新数据
    const latestResponse = await getLatestDeviceData(selectedDevice.value)
    currentData.value = latestResponse.data
    
    // 加载历史数据（最近50条）
    const historyResponse = await getDeviceHistoryData(selectedDevice.value, { limit: 50 })
    historyData.value = historyResponse.data.rows || []
    
  } catch (error) {
    console.error('加载设备数据失败:', error)
    ElMessage.error('加载设备数据失败')
  } finally {
    chartLoading.value = false
  }
}

const onDeviceChange = () => {
  loadDeviceData()
}

const selectDevice = (deviceId: string) => {
  selectedDevice.value = deviceId
  loadDeviceData()
}

const refreshData = async () => {
  await Promise.all([
    loadDeviceList(),
    loadStatistics(),
    loadDeviceData()
  ])
  ElMessage.success('数据刷新成功')
}

// 生命周期
onMounted(() => {
  Promise.all([
    loadDeviceList(),
    loadStatistics()
  ]).then(() => {
    // 检查URL参数中是否指定了设备ID
    const route = router.currentRoute.value
    const deviceIdFromQuery = route.query.deviceId as string
    if (deviceIdFromQuery && deviceList.value.some(d => d.device_id === deviceIdFromQuery)) {
      selectedDevice.value = deviceIdFromQuery
      loadDeviceData()
    }
  })
  
  // 设置定时刷新（每30秒）
  setInterval(() => {
    loadStatistics()
    if (selectedDevice.value) {
      loadDeviceData()
    }
  }, 30000)
})
</script>

<style scoped>
.iot-dashboard {
  padding: 20px;
}

.stats-cards {
  margin-bottom: 20px;
}

.stats-card {
  height: 120px;
}

.stats-content {
  display: flex;
  align-items: center;
  height: 100%;
}

.stats-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
  font-size: 24px;
  color: white;
}

.stats-icon.total {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.stats-icon.online {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.stats-icon.offline {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.stats-info {
  flex: 1;
}

.stats-value {
  font-size: 32px;
  font-weight: bold;
  color: #303133;
  line-height: 1;
}

.stats-label {
  font-size: 14px;
  color: #909399;
  margin-top: 5px;
}

.device-selection {
  margin-bottom: 20px;
}

.device-selector {
  display: flex;
  align-items: center;
  gap: 20px;
}

.device-info-text {
  color: #909399;
  font-size: 14px;
}

.dashboard-section {
  margin-bottom: 20px;
}

.metric-card {
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.metric-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
}

.metric-header {
  display: flex;
  align-items: center;
  gap: 12px;
}

.metric-icon {
  width: 40px;
  height: 40px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  color: white;
}

.metric-icon.co2 {
  background: linear-gradient(135deg, #ff6b6b 0%, #ee5a52 100%);
}

.metric-icon.tvoc {
  background: linear-gradient(135deg, #4ecdc4 0%, #44a08d 100%);
}

.metric-icon.temperature {
  background: linear-gradient(135deg, #45b7d1 0%, #96c93d 100%);
}

.metric-icon.humidity {
  background: linear-gradient(135deg, #96ceb4 0%, #74a9cf 100%);
}

.metric-info h3 {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.metric-info p {
  margin: 2px 0 0 0;
  font-size: 12px;
  color: #909399;
}

.metric-content {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 20px 0;
}

.gauge-container {
  flex: 1;
  height: 200px;
}

.metric-details {
  flex: 1;
  text-align: center;
}

.current-reading {
  margin-bottom: 12px;
}

.current-reading .value {
  font-size: 32px;
  font-weight: bold;
  color: #303133;
}

.current-reading .unit {
  font-size: 16px;
  color: #909399;
  margin-left: 4px;
}

.status-indicator {
  padding: 4px 12px;
  border-radius: 16px;
  font-size: 12px;
  font-weight: 500;
}

.status-indicator.good {
  background: #f0f9ff;
  color: #1890ff;
  border: 1px solid #91d5ff;
}

.status-indicator.moderate {
  background: #fff7e6;
  color: #fa8c16;
  border: 1px solid #ffd591;
}

.status-indicator.poor {
  background: #fff1f0;
  color: #f5222d;
  border: 1px solid #ffa39e;
}

.status-indicator.unknown {
  background: #f5f5f5;
  color: #8c8c8c;
  border: 1px solid #d9d9d9;
}

/* 温度计样式 */
.thermometer-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 200px;
  position: relative;
}

.thermometer {
  position: relative;
  display: flex;
  align-items: flex-end;
}

.thermometer-scale {
  position: absolute;
  left: -40px;
  bottom: 0;
  height: 160px;
}

.scale-mark {
  position: absolute;
  left: 0;
  display: flex;
  align-items: center;
}

.scale-mark span {
  font-size: 10px;
  color: #666;
  margin-right: 5px;
}

.thermometer-tube {
  width: 20px;
  height: 160px;
  background: #f0f0f0;
  border-radius: 10px;
  position: relative;
  overflow: hidden;
}

.thermometer-mercury {
  position: absolute;
  bottom: 0;
  width: 100%;
  background: linear-gradient(180deg, #45b7d1 0%, #ff6b6b 100%);
  border-radius: 0 0 10px 10px;
  transition: height 0.8s ease;
}

/* 湿度水滴样式 */
.humidity-display {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 200px;
  gap: 30px;
}

.water-drop {
  width: 80px;
  height: 100px;
  background: #f0f8ff;
  border: 3px solid #96ceb4;
  border-radius: 50px 50px 50px 0;
  transform: rotate(-45deg);
  position: relative;
  overflow: hidden;
}

.water-level {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  background: linear-gradient(180deg, #96ceb4 0%, #4ecdc4 100%);
  transition: height 0.8s ease;
  border-radius: 50px 50px 50px 0;
}

.drop-value {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%) rotate(45deg);
  text-align: center;
  color: #303133;
}

.drop-value .value {
  font-size: 18px;
  font-weight: bold;
  display: block;
}

.drop-value .unit {
  font-size: 12px;
  color: #666;
}

.humidity-scale {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  height: 100px;
}

.scale-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.scale-line {
  width: 12px;
  height: 2px;
  background: #d9d9d9;
}

.scale-text {
  font-size: 10px;
  color: #666;
}

.no-device-tip {
  margin-top: 40px;
  text-align: center;
}


</style> 