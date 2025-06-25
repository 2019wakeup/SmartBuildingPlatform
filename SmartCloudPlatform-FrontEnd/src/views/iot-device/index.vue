<template>
  <div class="iot-device-management">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-title">
        <h2>设备管理</h2>
        <p>管理物联网设备的注册、状态和配置</p>
      </div>
      <div class="header-actions">
        <el-button type="primary" @click="showRegisterDialog">
          <el-icon><Plus /></el-icon>
          注册设备
        </el-button>
        <el-button @click="handleCheckOfflineDevices" :loading="checkingOffline">
          <el-icon><Refresh /></el-icon>
          检查离线设备
        </el-button>
      </div>
    </div>

    <!-- 搜索和筛选 -->
    <div class="search-section">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input
            v-model="searchForm.deviceId"
            placeholder="设备ID"
            clearable
            @input="handleSearch"
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>
        </el-col>
        <el-col :span="6">
          <el-input
            v-model="searchForm.deviceName"
            placeholder="设备名称"
            clearable
            @input="handleSearch"
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>
        </el-col>
        <el-col :span="6">
          <el-select
            v-model="searchForm.deviceStatus"
            placeholder="设备状态"
            clearable
            @change="handleSearch"
          >
            <el-option label="全部" value="" />
            <el-option label="在线" value="online" />
            <el-option label="离线" value="offline" />
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>
            搜索
          </el-button>
          <el-button @click="resetSearch">
            <el-icon><Refresh /></el-icon>
            重置
          </el-button>
        </el-col>
      </el-row>
    </div>

    <!-- 设备列表 -->
    <div class="table-section">
      <el-table
        :data="deviceList"
        v-loading="loading"
        style="width: 100%"
        stripe
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="device_id" label="设备ID" width="180" />
        <el-table-column prop="device_name" label="设备名称" width="200" />
        <el-table-column prop="device_location" label="设备位置" width="200" />
        <el-table-column label="设备状态" width="100">
          <template #default="{ row }">
            <el-tag
              :type="row.device_status === 'online' ? 'success' : 'danger'"
              size="small"
            >
              {{ row.device_status === 'online' ? '在线' : '离线' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="当前数据" width="300">
          <template #default="{ row }">
            <div class="device-data">
              <span v-if="row.current_co2_ppm">CO₂: {{ row.current_co2_ppm }}ppm</span>
              <span v-if="row.current_tvoc_ppb">TVOC: {{ row.current_tvoc_ppb }}ppb</span>
              <span v-if="row.current_env_temperature">温度: {{ row.current_env_temperature }}℃</span>
              <span v-if="row.current_env_humidity">湿度: {{ row.current_env_humidity }}%</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="last_update_time" label="最后更新时间" width="180">
          <template #default="{ row }">
            {{ row.last_update_time ? formatTime(row.last_update_time) : '--' }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button
              type="primary"
              size="small"
              @click="viewDeviceDetail(row)"
            >
              详情
            </el-button>
            <el-button
              type="success"
              size="small"
              @click="viewDeviceData(row)"
            >
              数据
            </el-button>
            <el-button
              type="danger"
              size="small"
              @click="deleteDevice(row)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.currentPage"
          v-model:page-size="pagination.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>

    <!-- 设备注册对话框 -->
    <el-dialog
      v-model="registerDialogVisible"
      title="注册新设备"
      width="500px"
      @close="resetRegisterForm"
    >
      <el-form
        ref="registerFormRef"
        :model="registerForm"
        :rules="registerRules"
        label-width="100px"
      >
        <el-form-item label="设备ID" prop="deviceId">
          <el-input
            v-model="registerForm.deviceId"
            placeholder="请输入设备ID"
          />
        </el-form-item>
        <el-form-item label="设备名称" prop="deviceName">
          <el-input
            v-model="registerForm.deviceName"
            placeholder="请输入设备名称"
          />
        </el-form-item>
        <el-form-item label="设备位置" prop="deviceLocation">
          <el-input
            v-model="registerForm.deviceLocation"
            placeholder="请输入设备位置"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="registerDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleRegisterDevice" :loading="registerLoading">
            确定
          </el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 设备详情对话框 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="设备详情"
      width="800px"
    >
      <div v-if="selectedDevice" class="device-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="设备ID">
            {{ selectedDevice.device_id }}
          </el-descriptions-item>
          <el-descriptions-item label="设备名称">
            {{ selectedDevice.device_name }}
          </el-descriptions-item>
          <el-descriptions-item label="设备位置">
            {{ selectedDevice.device_location }}
          </el-descriptions-item>
          <el-descriptions-item label="设备状态">
            <el-tag
              :type="selectedDevice.device_status === 'online' ? 'success' : 'danger'"
            >
              {{ selectedDevice.device_status === 'online' ? '在线' : '离线' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="CO₂浓度">
            {{ selectedDevice.current_co2_ppm || '--' }} ppm
          </el-descriptions-item>
          <el-descriptions-item label="TVOC浓度">
            {{ selectedDevice.current_tvoc_ppb || '--' }} ppb
          </el-descriptions-item>
          <el-descriptions-item label="芯片温度">
            {{ selectedDevice.current_chip_temperature || '--' }} ℃
          </el-descriptions-item>
          <el-descriptions-item label="环境温度">
            {{ selectedDevice.current_env_temperature || '--' }} ℃
          </el-descriptions-item>
          <el-descriptions-item label="环境湿度">
            {{ selectedDevice.current_env_humidity || '--' }} %
          </el-descriptions-item>
          <el-descriptions-item label="最后更新时间">
            {{ selectedDevice.last_update_time ? formatTime(selectedDevice.last_update_time) : '--' }}
          </el-descriptions-item>
          <el-descriptions-item label="最后离线时间">
            {{ selectedDevice.last_offline_time ? formatTime(selectedDevice.last_offline_time) : '--' }}
          </el-descriptions-item>
          <el-descriptions-item label="创建时间">
            {{ selectedDevice.create_time ? formatTime(selectedDevice.create_time) : '--' }}
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, reactive } from 'vue'
import { ElMessage, ElMessageBox, type FormInstance } from 'element-plus'
import { Plus, Search, Refresh } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import {
  getAllDeviceStatus,
  registerDevice,
  checkOfflineDevices,
  type IoTDeviceStatus
} from '@/api/iot'

const router = useRouter()

// 响应式数据
const loading = ref(false)
const checkingOffline = ref(false)
const registerLoading = ref(false)
const deviceList = ref<IoTDeviceStatus[]>([])
const selectedDevices = ref<IoTDeviceStatus[]>([])
const selectedDevice = ref<IoTDeviceStatus | null>(null)

// 对话框状态
const registerDialogVisible = ref(false)
const detailDialogVisible = ref(false)

// 表单引用
const registerFormRef = ref<FormInstance>()

// 搜索表单
const searchForm = reactive({
  deviceId: '',
  deviceName: '',
  deviceStatus: ''
})

// 分页
const pagination = reactive({
  currentPage: 1,
  pageSize: 20,
  total: 0
})

// 注册表单
const registerForm = reactive({
  deviceId: '',
  deviceName: '',
  deviceLocation: ''
})

// 表单验证规则
const registerRules = {
  deviceId: [
    { required: true, message: '请输入设备ID', trigger: 'blur' },
    { min: 3, max: 50, message: '设备ID长度在3-50个字符之间', trigger: 'blur' }
  ],
  deviceName: [
    { required: true, message: '请输入设备名称', trigger: 'blur' },
    { min: 2, max: 100, message: '设备名称长度在2-100个字符之间', trigger: 'blur' }
  ],
  deviceLocation: [
    { max: 200, message: '设备位置长度不能超过200个字符', trigger: 'blur' }
  ]
}

// 方法
const loadDeviceList = async () => {
  try {
    loading.value = true
    const response = await getAllDeviceStatus()
    deviceList.value = response.data.rows || []
    pagination.total = response.data.total || 0
  } catch (error) {
    console.error('加载设备列表失败:', error)
    ElMessage.error('加载设备列表失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pagination.currentPage = 1
  loadDeviceList()
}

const resetSearch = () => {
  searchForm.deviceId = ''
  searchForm.deviceName = ''
  searchForm.deviceStatus = ''
  handleSearch()
}

const handleSelectionChange = (selection: IoTDeviceStatus[]) => {
  selectedDevices.value = selection
}

const handleSizeChange = (size: number) => {
  pagination.pageSize = size
  loadDeviceList()
}

const handleCurrentChange = (page: number) => {
  pagination.currentPage = page
  loadDeviceList()
}

const showRegisterDialog = () => {
  registerDialogVisible.value = true
}

const resetRegisterForm = () => {
  registerForm.deviceId = ''
  registerForm.deviceName = ''
  registerForm.deviceLocation = ''
  registerFormRef.value?.resetFields()
}

const handleRegisterDevice = async () => {
  if (!registerFormRef.value) return

  await registerFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        registerLoading.value = true
        const response = await registerDevice(registerForm)
        if (response.success) {
          ElMessage.success('设备注册成功')
          registerDialogVisible.value = false
          loadDeviceList()
        } else {
          ElMessage.error(response.msg || '设备注册失败')
        }
      } catch (error: any) {
        ElMessage.error(error.message || '设备注册失败')
      } finally {
        registerLoading.value = false
      }
    }
  })
}

const viewDeviceDetail = (device: IoTDeviceStatus) => {
  selectedDevice.value = device
  detailDialogVisible.value = true
}

const viewDeviceData = (device: IoTDeviceStatus) => {
  router.push({
    path: '/iot-dashboard',
    query: { deviceId: device.device_id }
  })
}

const deleteDevice = async (device: IoTDeviceStatus) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除设备 "${device.device_name}" 吗？`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    // 这里应该调用删除设备的API
    ElMessage.success('删除功能待实现')
  } catch {
    ElMessage.info('已取消删除')
  }
}

const handleCheckOfflineDevices = async () => {
  try {
    checkingOffline.value = true
    const response = await checkOfflineDevices(10)
    if (response.success) {
      ElMessage.success(response.msg || '离线设备检查完成')
      loadDeviceList()
    } else {
      ElMessage.error(response.msg || '检查失败')
    }
  } catch (error: any) {
    ElMessage.error(error.message || '检查离线设备失败')
  } finally {
    checkingOffline.value = false
  }
}

const formatTime = (time: string) => {
  return new Date(time).toLocaleString()
}

// 生命周期
onMounted(() => {
  loadDeviceList()
})
</script>

<style scoped>
.iot-device-management {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.header-title h2 {
  margin: 0 0 5px 0;
  color: #303133;
  font-size: 24px;
}

.header-title p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.header-actions {
  display: flex;
  gap: 10px;
}

.search-section {
  margin-bottom: 20px;
  padding: 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.table-section {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.device-data {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.device-data span {
  font-size: 12px;
  color: #606266;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

.device-detail {
  padding: 20px;
}
</style> 