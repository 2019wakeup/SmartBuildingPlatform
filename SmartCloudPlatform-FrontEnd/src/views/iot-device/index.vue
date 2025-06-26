<template>
  <div class="iot-device-management">
    <!-- Page Header -->
    <div class="page-header">
      <div class="header-title">
        <h2>Device Management</h2>
        <p>Manage IoT device registration, status and configuration</p>
      </div>
      <div class="header-actions">
        <el-button type="primary" @click="showRegisterDialog">
          <el-icon><Plus /></el-icon>
          Register Device
        </el-button>
        <el-button @click="handleCheckOfflineDevices" :loading="checkingOffline">
          <el-icon><Refresh /></el-icon>
          Check Offline Devices
        </el-button>
      </div>
    </div>

    <!-- Search and Filter -->
    <div class="search-section">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input
            v-model="searchForm.deviceId"
            placeholder="Device ID"
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
            placeholder="Device Name"
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
            placeholder="Device Status"
            clearable
            @change="handleSearch"
          >
            <el-option label="All" value="" />
            <el-option label="Online" value="online" />
            <el-option label="Offline" value="offline" />
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>
            Search
          </el-button>
          <el-button @click="resetSearch">
            <el-icon><Refresh /></el-icon>
            Reset
          </el-button>
        </el-col>
      </el-row>
    </div>

    <!-- Device List -->
    <div class="table-section">
      <el-table
        :data="deviceList"
        v-loading="loading"
        style="width: 100%"
        stripe
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="device_id" label="Device ID" width="180" />
        <el-table-column prop="device_name" label="Device Name" width="200" />
        <el-table-column prop="device_location" label="Device Location" width="200" />
        <el-table-column label="Device Status" width="100">
          <template #default="{ row }">
            <el-tag
              :type="row.device_status === 'online' ? 'success' : 'danger'"
              size="small"
            >
              {{ row.device_status === 'online' ? 'Online' : 'Offline' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="Current Data" width="300">
          <template #default="{ row }">
            <div class="device-data">
              <span v-if="row.current_co2_ppm">CO₂: {{ row.current_co2_ppm }}ppm</span>
              <span v-if="row.current_tvoc_ppb">TVOC: {{ row.current_tvoc_ppb }}ppb</span>
              <span v-if="row.current_env_temperature">Temp: {{ row.current_env_temperature }}℃</span>
              <span v-if="row.current_env_humidity">Humidity: {{ row.current_env_humidity }}%</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="last_update_time" label="Last Update Time" width="180">
          <template #default="{ row }">
            {{ row.last_update_time ? formatTime(row.last_update_time) : '--' }}
          </template>
        </el-table-column>
        <el-table-column label="Actions" width="200" fixed="right">
          <template #default="{ row }">
            <el-button
              type="primary"
              size="small"
              @click="viewDeviceDetail(row)"
            >
              Details
            </el-button>
            <el-button
              type="success"
              size="small"
              @click="viewDeviceData(row)"
            >
              Data
            </el-button>
            <el-button
              type="danger"
              size="small"
              @click="deleteDevice(row)"
            >
              Delete
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- Pagination -->
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

    <!-- Device Registration Dialog -->
    <el-dialog
      v-model="registerDialogVisible"
      title="Register New Device"
      width="500px"
      @close="resetRegisterForm"
    >
      <el-form
        ref="registerFormRef"
        :model="registerForm"
        :rules="registerRules"
        label-width="120px"
      >
        <el-form-item label="Device ID" prop="deviceId">
          <el-input
            v-model="registerForm.deviceId"
            placeholder="Enter device ID"
          />
        </el-form-item>
        <el-form-item label="Device Name" prop="deviceName">
          <el-input
            v-model="registerForm.deviceName"
            placeholder="Enter device name"
          />
        </el-form-item>
        <el-form-item label="Device Location" prop="deviceLocation">
          <el-input
            v-model="registerForm.deviceLocation"
            placeholder="Enter device location"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="registerDialogVisible = false">Cancel</el-button>
          <el-button type="primary" @click="handleRegisterDevice" :loading="registerLoading">
            Confirm
          </el-button>
        </span>
      </template>
    </el-dialog>

    <!-- Device Details Dialog -->
    <el-dialog
      v-model="detailDialogVisible"
      title="Device Details"
      width="800px"
    >
      <div v-if="selectedDevice" class="device-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="Device ID">
            {{ selectedDevice.device_id }}
          </el-descriptions-item>
          <el-descriptions-item label="Device Name">
            {{ selectedDevice.device_name }}
          </el-descriptions-item>
          <el-descriptions-item label="Device Location">
            {{ selectedDevice.device_location }}
          </el-descriptions-item>
          <el-descriptions-item label="Device Status">
            <el-tag
              :type="selectedDevice.device_status === 'online' ? 'success' : 'danger'"
            >
              {{ selectedDevice.device_status === 'online' ? 'Online' : 'Offline' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="CO₂ Concentration">
            {{ selectedDevice.current_co2_ppm || '--' }} ppm
          </el-descriptions-item>
          <el-descriptions-item label="TVOC Concentration">
            {{ selectedDevice.current_tvoc_ppb || '--' }} ppb
          </el-descriptions-item>
          <el-descriptions-item label="Chip Temperature">
            {{ selectedDevice.current_chip_temperature || '--' }} ℃
          </el-descriptions-item>
          <el-descriptions-item label="Environmental Temperature">
            {{ selectedDevice.current_env_temperature || '--' }} ℃
          </el-descriptions-item>
          <el-descriptions-item label="Environmental Humidity">
            {{ selectedDevice.current_env_humidity || '--' }} %
          </el-descriptions-item>
          <el-descriptions-item label="Last Update Time">
            {{ selectedDevice.last_update_time ? formatTime(selectedDevice.last_update_time) : '--' }}
          </el-descriptions-item>
          <el-descriptions-item label="Last Offline Time">
            {{ selectedDevice.last_offline_time ? formatTime(selectedDevice.last_offline_time) : '--' }}
          </el-descriptions-item>
          <el-descriptions-item label="Creation Time">
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

// Form validation rules
const registerRules = {
  deviceId: [
    { required: true, message: 'Please enter device ID', trigger: 'blur' },
    { min: 3, max: 50, message: 'Device ID length should be between 3-50 characters', trigger: 'blur' }
  ],
  deviceName: [
    { required: true, message: 'Please enter device name', trigger: 'blur' },
    { min: 2, max: 100, message: 'Device name length should be between 2-100 characters', trigger: 'blur' }
  ],
  deviceLocation: [
    { max: 200, message: 'Device location length cannot exceed 200 characters', trigger: 'blur' }
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
    console.error('Failed to load device list:', error)
    ElMessage.error('Failed to load device list')
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
          ElMessage.success('Device registered successfully')
          registerDialogVisible.value = false
          loadDeviceList()
        } else {
          ElMessage.error(response.msg || 'Device registration failed')
        }
      } catch (error: any) {
        ElMessage.error(error.message || 'Device registration failed')
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
      `Are you sure you want to delete device "${device.device_name}"?`,
      'Confirm Delete',
      {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }
    )
    
    // TODO: Call delete device API here
    ElMessage.success('Delete function to be implemented')
  } catch {
    ElMessage.info('Delete operation cancelled')
  }
}

const handleCheckOfflineDevices = async () => {
  try {
    checkingOffline.value = true
    const response = await checkOfflineDevices(10)
    if (response.success) {
      ElMessage.success(response.msg || 'Offline device check completed')
      loadDeviceList()
    } else {
      ElMessage.error(response.msg || 'Check failed')
    }
  } catch (error: any) {
    ElMessage.error(error.message || 'Failed to check offline devices')
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