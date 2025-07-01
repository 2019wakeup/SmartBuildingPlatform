<template>
  <div class="health-report-history">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span class="title">健康报告历史</span>
          <div class="header-actions">
            <el-date-picker
              v-model="dateRange"
              type="datetimerange"
              range-separator="至"
              start-placeholder="开始时间"
              end-placeholder="结束时间"
              format="YYYY-MM-DD HH:mm:ss"
              value-format="YYYY-MM-DD HH:mm:ss"
              @change="handleDateRangeChange"
              style="margin-right: 10px;"
            />
            <el-button type="primary" @click="fetchReports">
              <el-icon><Search /></el-icon>
              查询
            </el-button>
          </div>
        </div>
      </template>
      
      <div class="table-container">
        <el-table
          :data="reportList"
          v-loading="loading"
          style="width: 100%"
          height="500"
        >
          <el-table-column prop="id" label="报告ID" width="80" />
          <el-table-column prop="dataTaken" label="采集时间" width="180">
            <template #default="scope">
              {{ formatDateTime(scope.row.dataTaken) }}
            </template>
          </el-table-column>
          <el-table-column label="数据统计" width="300">
            <template #default="scope">
              <div class="data-stats">
                <el-tag size="small" type="info">
                  {{ getDataPointsCount(scope.row.reportData) }} 个数据点
                </el-tag>
                <el-tag size="small" type="success" style="margin-left: 5px;">
                  温度: {{ getAvgTemperature(scope.row.reportData) }}°C
                </el-tag>
                <el-tag size="small" type="warning" style="margin-left: 5px;">
                  湿度: {{ getAvgHumidity(scope.row.reportData) }}%
                </el-tag>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="健康状态" width="100">
            <template #default="scope">
              <el-tag
                :type="getHealthStatusType(scope.row.reportData)"
                size="small"
              >
                {{ getHealthStatusText(scope.row.reportData) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="180">
            <template #default="scope">
              {{ formatDateTime(scope.row.createTime) }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200" fixed="right">
            <template #default="scope">
              <el-button
                type="primary"
                size="small"
                @click="viewDetail(scope.row)"
              >
                查看详情
              </el-button>
              <el-button
                type="danger"
                size="small"
                @click="deleteReport(scope.row)"
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
    
    <!-- 详情对话框 -->
    <el-dialog
      v-model="showDetailDialog"
      title="健康报告详情"
      width="90%"
      :before-close="handleClose"
    >
      <div v-if="selectedReport">
        <div class="detail-header">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="报告ID">
              {{ selectedReport.id }}
            </el-descriptions-item>
            <el-descriptions-item label="学生ID">
              {{ selectedReport.stuId }}
            </el-descriptions-item>
            <el-descriptions-item label="采集时间">
              {{ formatDateTime(selectedReport.dataTaken) }}
            </el-descriptions-item>
            <el-descriptions-item label="创建时间">
              {{ formatDateTime(selectedReport.createTime) }}
            </el-descriptions-item>
          </el-descriptions>
        </div>
        
        <div class="detail-content">
          <h3>传感器数据</h3>
          <el-table :data="getReportDataPoints(selectedReport.reportData)" height="400">
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
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search } from '@element-plus/icons-vue'
import { 
  getHealthReportsByStuId, 
  getHealthReportsByStuIdAndTimeRange,
  deleteHealthReport 
} from '@/api/health-report'
import type { HealthReport, HealthReportDataPoint } from '@/api/types'

// Props
interface Props {
  stuId: number
}

const props = defineProps<Props>()

// 响应式数据
const loading = ref(false)
const reportList = ref<HealthReport[]>([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const dateRange = ref<[string, string] | null>(null)
const showDetailDialog = ref(false)
const selectedReport = ref<HealthReport | null>(null)

// 方法
const fetchReports = async () => {
  if (!props.stuId) return
  
  loading.value = true
  try {
    let response
    if (dateRange.value && dateRange.value.length === 2) {
      response = await getHealthReportsByStuIdAndTimeRange(
        props.stuId,
        dateRange.value[0],
        dateRange.value[1]
      )
    } else {
      response = await getHealthReportsByStuId(props.stuId)
    }
    
    if (response.code === 200) {
      reportList.value = response.rows || []
      total.value = response.total || 0
    } else {
      reportList.value = []
      total.value = 0
      ElMessage.warning(response.msg || '查询失败')
    }
  } catch (error) {
    console.error('获取健康报告列表失败:', error)
    ElMessage.error('获取健康报告列表失败')
  } finally {
    loading.value = false
  }
}

const handleDateRangeChange = () => {
  fetchReports()
}

const handleSizeChange = (val: number) => {
  pageSize.value = val
  fetchReports()
}

const handleCurrentChange = (val: number) => {
  currentPage.value = val
  fetchReports()
}

const viewDetail = (report: HealthReport) => {
  selectedReport.value = report
  showDetailDialog.value = true
}

const deleteReport = async (report: HealthReport) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除报告ID为 ${report.id} 的健康报告吗？`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
    )
    
    const response = await deleteHealthReport([report.id!])
    if (response.code === 200) {
      ElMessage.success('删除成功')
      fetchReports()
    } else {
      ElMessage.error(response.msg || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除健康报告失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

const formatDateTime = (dateTimeStr: string) => {
  return new Date(dateTimeStr).toLocaleString('zh-CN')
}

const getReportDataPoints = (reportData: string): HealthReportDataPoint[] => {
  try {
    return JSON.parse(reportData) as HealthReportDataPoint[]
  } catch (error) {
    console.error('解析报告数据失败:', error)
    return []
  }
}

const getDataPointsCount = (reportData: string): number => {
  const dataPoints = getReportDataPoints(reportData)
  return dataPoints.length
}

const getAvgTemperature = (reportData: string): string => {
  const dataPoints = getReportDataPoints(reportData)
  if (dataPoints.length === 0) return 'N/A'
  
  const total = dataPoints.reduce((sum, point) => sum + point.temperature_c, 0)
  return (total / dataPoints.length).toFixed(1)
}

const getAvgHumidity = (reportData: string): string => {
  const dataPoints = getReportDataPoints(reportData)
  if (dataPoints.length === 0) return 'N/A'
  
  const total = dataPoints.reduce((sum, point) => sum + point.humidity_percent, 0)
  return (total / dataPoints.length).toFixed(1)
}

const getHealthStatusText = (reportData: string): string => {
  const dataPoints = getReportDataPoints(reportData)
  if (dataPoints.length === 0) return '未知'
  
  const avgTemp = parseFloat(getAvgTemperature(reportData))
  const avgHumidity = parseFloat(getAvgHumidity(reportData))
  
  if (avgTemp >= 36 && avgTemp <= 37.5 && avgHumidity >= 40 && avgHumidity <= 70) {
    return '良好'
  } else if (avgTemp >= 35 && avgTemp <= 38 && avgHumidity >= 30 && avgHumidity <= 80) {
    return '正常'
  } else {
    return '异常'
  }
}

const getHealthStatusType = (reportData: string): string => {
  const status = getHealthStatusText(reportData)
  switch (status) {
    case '良好':
      return 'success'
    case '正常':
      return 'info'
    case '异常':
      return 'danger'
    default:
      return 'info'
  }
}

const handleClose = (done: () => void) => {
  showDetailDialog.value = false
  done()
}

// 生命周期
onMounted(() => {
  fetchReports()
})
</script>

<style scoped>
.health-report-history {
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

.header-actions {
  display: flex;
  align-items: center;
}

.table-container {
  margin-bottom: 20px;
}

.data-stats {
  display: flex;
  flex-wrap: wrap;
  gap: 5px;
}

.pagination-container {
  display: flex;
  justify-content: center;
  padding: 20px 0;
}

.detail-header {
  margin-bottom: 20px;
}

.detail-content {
  margin-top: 20px;
}

.detail-content h3 {
  margin-bottom: 15px;
  color: #303133;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
}
</style> 