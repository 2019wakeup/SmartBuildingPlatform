import request from './request'

// IoT设备状态接口
export interface IoTDeviceStatus {
  device_id: string
  device_name?: string
  device_location?: string
  current_co2_ppm?: number
  current_tvoc_ppb?: number
  current_chip_temperature?: number
  current_env_temperature?: number
  current_env_humidity?: number
  device_status: 'online' | 'offline'
  last_update_time?: string
  last_offline_time?: string
  create_time?: string
  update_time?: string
}

// IoT传感器数据接口
export interface IoTSensorData {
  id: number
  device_id: string
  co2_ppm?: number
  tvoc_ppb?: number
  chip_temperature?: number
  env_temperature?: number
  env_humidity?: number
  received_time: string
  raw_message?: string
  create_time?: string
  update_time?: string
}

// 设备统计信息接口
export interface DeviceStatistics {
  total_devices: number
  online_devices: number
  offline_devices: number
}

// 获取所有设备状态
export const getAllDeviceStatus = () => {
  return request.get<{rows: IoTDeviceStatus[], total: number}>('/iot/devices/status')
}

// 获取在线设备
export const getOnlineDevices = () => {
  return request.get<{rows: IoTDeviceStatus[], total: number}>('/iot/devices/online')
}

// 获取离线设备
export const getOfflineDevices = () => {
  return request.get<{rows: IoTDeviceStatus[], total: number}>('/iot/devices/offline')
}

// 获取设备统计信息
export const getDeviceStatistics = () => {
  return request.get<DeviceStatistics>('/iot/devices/statistics')
}

// 获取设备历史数据
export const getDeviceHistoryData = (deviceId: string, params?: {
  startTime?: string
  endTime?: string
  limit?: number
}) => {
  return request.get<{rows: IoTSensorData[], total: number}>(`/iot/data/history/${deviceId}`, { params })
}

// 获取设备最新数据
export const getLatestDeviceData = (deviceId: string) => {
  return request.get<IoTSensorData>(`/iot/data/latest/${deviceId}`)
}

// 设备注册
export const registerDevice = (data: {
  deviceId: string
  deviceName: string
  deviceLocation?: string
}) => {
  return request.post('/iot/devices/register', null, { params: data })
}

// 检查离线设备
export const checkOfflineDevices = (thresholdMinutes: number = 10) => {
  return request.post('/iot/devices/check-offline', null, { 
    params: { thresholdMinutes } 
  })
} 