import request from './request'
import type { HealthReport, AjaxResult, TableDataInfo, ApiResponse } from './types'

// API路径常量
const API_BASE = '/health-report'

/**
 * 提交健康报告数据
 */
export function submitHealthReport(data: {
  fingerPrint: string
  dataTaken: string
  reportData: string
  stuID: number
}): Promise<ApiResponse> {
  return request.post(`${API_BASE}/submit`, data)
}

/**
 * 查询健康报告列表
 */
export function getHealthReportList(params?: any): Promise<ApiResponse> {
  return request.get(`${API_BASE}/list`, { params })
}

/**
 * 获取健康报告详细信息
 */
export function getHealthReport(id: number): Promise<ApiResponse> {
  return request.get(`${API_BASE}/${id}`)
}

/**
 * 根据学生ID查询健康报告列表
 */
export function getHealthReportsByStuId(stuId: number): Promise<ApiResponse> {
  return request.get(`${API_BASE}/student/${stuId}`)
}

/**
 * 根据学生ID和时间范围查询健康报告
 */
export function getHealthReportsByStuIdAndTimeRange(
  stuId: number,
  startTime?: string,
  endTime?: string
): Promise<ApiResponse> {
  return request.get(`${API_BASE}/student/${stuId}/range`, {
    params: {
      startTime,
      endTime
    }
  })
}

/**
 * 获取学生最新健康报告
 */
export function getLatestHealthReportByStuId(stuId: number): Promise<ApiResponse> {
  return request.get(`${API_BASE}/student/${stuId}/latest`)
}

/**
 * 新增健康报告
 */
export function addHealthReport(data: HealthReport): Promise<ApiResponse> {
  return request.post(API_BASE, data)
}

/**
 * 修改健康报告
 */
export function updateHealthReport(data: HealthReport): Promise<ApiResponse> {
  return request.put(API_BASE, data)
}

/**
 * 删除健康报告
 */
export function deleteHealthReport(ids: number[]): Promise<ApiResponse> {
  return request.delete(`${API_BASE}/${ids.join(',')}`)
} 