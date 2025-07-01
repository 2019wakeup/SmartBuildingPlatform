<template>
  <div class="health-report-card">
    <el-card class="box-card" shadow="hover">
      <template #header>
        <div class="card-header">
          <span class="title">个人健康报告</span>
          <el-button type="primary" size="small" @click="refreshData">
            <el-icon><Refresh /></el-icon>
            刷新
          </el-button>
        </div>
      </template>
      
      <div v-if="loading" class="loading-container">
        <el-skeleton :rows="3" animated />
      </div>
      
      <div v-else-if="healthReport" class="report-content">
        <div class="report-header">
          <div class="report-time">
            <el-icon><Clock /></el-icon>
            <span>采集时间：{{ formatDateTime(healthReport.dataTaken || healthReport.data_taken) }}</span>
          </div>
          <div class="report-id">
            <span>报告ID：{{ healthReport.id }}</span>
          </div>
        </div>
        
        <div class="report-stats" v-if="reportStats">
          <el-row :gutter="20">
            <el-col :span="8">
              <div class="stat-item">
                <div class="stat-label">数据点数量</div>
                <div class="stat-value">{{ reportStats.dataPoints }}</div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="stat-item">
                <div class="stat-label">平均温度</div>
                <div class="stat-value">{{ reportStats.avgTemperature }}°C</div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="stat-item">
                <div class="stat-label">平均湿度</div>
                <div class="stat-value">{{ reportStats.avgHumidity }}%</div>
              </div>
            </el-col>
          </el-row>
          <el-row :gutter="20" class="mt-4">
            <el-col :span="8">
              <div class="stat-item">
                <div class="stat-label">平均压力</div>
                <div class="stat-value">{{ reportStats.avgPressure }} hPa</div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="stat-item">
                <div class="stat-label">气体阻抗</div>
                <div class="stat-value">{{ reportStats.avgGasResistance || 'N/A' }}</div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="stat-item">
                <div class="stat-label">健康状态</div>
                <div class="stat-value" :class="getHealthStatusClass()">
                  {{ getHealthStatus() }}
                </div>
              </div>
            </el-col>
          </el-row>
        </div>
        
        <div class="report-actions">
          <el-button type="info" size="small" @click="showDetailDialog = true">
            查看详细数据
          </el-button>
          <el-button type="success" size="small" @click="viewHistory">
            历史报告
          </el-button>
        </div>
      </div>
      
      <div v-else class="no-data">
        <el-empty description="暂无健康报告数据" />
      </div>
    </el-card>
    
    <!-- 详细数据对话框 -->
    <el-dialog
      v-model="showDetailDialog"
      title="健康报告详细数据"
      width="80%"
      :before-close="handleClose"
    >
      <div v-if="healthReport">
        <el-table :data="reportDataPoints" height="400" style="width: 100%">
          <el-table-column prop="timestamp" label="时间戳" width="100" />
          <el-table-column prop="temperature_c" label="温度(°C)" width="120" />
          <el-table-column prop="pressure_hpa" label="压力(hPa)" width="120" />
          <el-table-column prop="humidity_percent" label="湿度(%)" width="120" />
          <el-table-column prop="gas_resistance_ohm" label="气体阻抗(Ω)" width="150">
            <template #default="scope">
              {{ scope.row.gas_resistance_ohm || 'N/A' }}
            </template>
          </el-table-column>
        </el-table>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showDetailDialog = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { Clock, Refresh } from '@element-plus/icons-vue'
import { getLatestHealthReportByStuId } from '@/api/health-report'
import type { HealthReport, HealthReportDataPoint } from '@/api/types'

// Props
interface Props {
  stuId: number
}

const props = defineProps<Props>()

// Emits
const emit = defineEmits<{
  viewHistory: []
}>()

// 响应式数据
const loading = ref(false)
const healthReport = ref<HealthReport | null>(null)
const showDetailDialog = ref(false)

// 计算属性
const reportDataPoints = computed(() => {
  // 兼容驼峰和下划线命名
  const rawData = healthReport.value?.reportData || healthReport.value?.report_data
  
  if (!rawData) {
    console.log('No reportData found in healthReport:', healthReport.value)
    return []
  }
  
  try {
    console.log('Raw reportData:', rawData)
    console.log('Type of reportData:', typeof rawData)
    
    let parsedData
    if (typeof rawData === 'string') {
      parsedData = JSON.parse(rawData)
    } else {
      parsedData = rawData
    }
    
    console.log('Parsed reportData:', parsedData)
    
    if (Array.isArray(parsedData)) {
      return parsedData as HealthReportDataPoint[]
    } else {
      console.error('Parsed data is not an array:', parsedData)
      return []
    }
  } catch (error) {
    console.error('解析报告数据失败:', error, 'Raw data:', rawData)
    return []
  }
})

const reportStats = computed(() => {
  const dataPoints = reportDataPoints.value
  if (!dataPoints || dataPoints.length === 0) return null
  
  let totalTemp = 0
  let totalPressure = 0
  let totalHumidity = 0
  let totalGasResistance = 0
  let gasResistanceCount = 0
  
  dataPoints.forEach(point => {
    totalTemp += point.temperature_c
    totalPressure += point.pressure_hpa
    totalHumidity += point.humidity_percent
    if (point.gas_resistance_ohm) {
      totalGasResistance += point.gas_resistance_ohm
      gasResistanceCount++
    }
  })
  
  const count = dataPoints.length
  return {
    dataPoints: count,
    avgTemperature: (totalTemp / count).toFixed(2),
    avgPressure: (totalPressure / count).toFixed(2),
    avgHumidity: (totalHumidity / count).toFixed(2),
    avgGasResistance: gasResistanceCount > 0 ? (totalGasResistance / gasResistanceCount).toFixed(2) : null
  }
})

// 方法
const fetchLatestReport = async () => {
  console.log('HealthReportCard: fetchLatestReport called with stuId:', props.stuId)
  if (!props.stuId) {
    console.log('HealthReportCard: stuId is falsy, returning early')
    return
  }
  
  loading.value = true
  try {
    console.log('HealthReportCard: calling API with stuId:', props.stuId)
    const response = await getLatestHealthReportByStuId(props.stuId)
    console.log('HealthReportCard: API response:', response)
    if (response.success === true || response.code === 200) {
      healthReport.value = response.data
      console.log('HealthReportCard: healthReport set to:', healthReport.value)
    } else {
      healthReport.value = null
      console.log('HealthReportCard: API returned unsuccessful response:', response)
    }
  } catch (error) {
    console.error('获取健康报告失败:', error)
    ElMessage.error('获取健康报告失败')
  } finally {
    loading.value = false
  }
}

const refreshData = () => {
  fetchLatestReport()
}

const viewHistory = () => {
  emit('viewHistory')
}

const formatDateTime = (dateTimeStr: string) => {
  if (!dateTimeStr) return '未知时间'
  
  try {
    // 处理后端返回的时间格式，可能是 ISO 格式或其他格式
    let date: Date
    
    if (dateTimeStr.includes('T')) {
      // ISO 格式: 2025-06-27T10:50:00
      date = new Date(dateTimeStr)
    } else if (dateTimeStr.includes(' ')) {
      // 标准格式: 2025-06-27 10:50:00
      date = new Date(dateTimeStr.replace(' ', 'T'))
    } else {
      date = new Date(dateTimeStr)
    }
    
    if (isNaN(date.getTime())) {
      console.error('Invalid date format:', dateTimeStr)
      return '时间格式错误'
    }
    
    return date.toLocaleString('zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit',
      second: '2-digit'
    })
  } catch (error) {
    console.error('Date parsing error:', error, 'Input:', dateTimeStr)
    return '时间解析错误'
  }
}

const getHealthStatus = () => {
  if (!reportStats.value) return '未知'
  
  const avgTemp = parseFloat(reportStats.value.avgTemperature)
  const avgHumidity = parseFloat(reportStats.value.avgHumidity)
  
  if (avgTemp >= 36 && avgTemp <= 37.5 && avgHumidity >= 40 && avgHumidity <= 70) {
    return '良好'
  } else if (avgTemp >= 35 && avgTemp <= 38 && avgHumidity >= 30 && avgHumidity <= 80) {
    return '正常'
  } else {
    return '异常'
  }
}

const getHealthStatusClass = () => {
  const status = getHealthStatus()
  switch (status) {
    case '良好':
      return 'status-good'
    case '正常':
      return 'status-normal'
    case '异常':
      return 'status-abnormal'
    default:
      return 'status-unknown'
  }
}

const handleClose = (done: () => void) => {
  showDetailDialog.value = false
  done()
}

// 生命周期
onMounted(() => {
  fetchLatestReport()
})
</script>

<style scoped>
.health-report-card {
  width: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.title {
  font-size: 18px;
  font-weight: bold;
  color: #303133;
}

.loading-container {
  padding: 20px;
}

.report-content {
  padding: 10px 0;
}

.report-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid #ebeef5;
}

.report-time {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #606266;
}

.report-id {
  color: #909399;
  font-size: 14px;
}

.report-stats {
  margin-bottom: 20px;
}

.stat-item {
  text-align: center;
  padding: 15px;
  background: #f8f9fa;
  border-radius: 8px;
  border: 1px solid #e9ecef;
}

.stat-label {
  font-size: 14px;
  color: #666;
  margin-bottom: 8px;
}

.stat-value {
  font-size: 20px;
  font-weight: bold;
  color: #333;
}

.status-good {
  color: #67c23a;
}

.status-normal {
  color: #409eff;
}

.status-abnormal {
  color: #f56c6c;
}

.status-unknown {
  color: #909399;
}

.report-actions {
  text-align: center;
  padding-top: 10px;
  border-top: 1px solid #ebeef5;
}

.no-data {
  padding: 40px 0;
}

.mt-4 {
  margin-top: 16px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
}
</style> 