package org.example.smartcloudplatform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.smartcloudplatform.entity.HealthReport;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 健康报告服务接口
 * 
 * @author SmartCloudPlatform
 */
public interface IHealthReportService extends IService<HealthReport> {

    /**
     * 查询健康报告列表
     * 
     * @param healthReport 健康报告
     * @return 健康报告集合
     */
    List<HealthReport> selectHealthReportList(HealthReport healthReport);

    /**
     * 查询健康报告
     * 
     * @param id 健康报告主键
     * @return 健康报告
     */
    HealthReport selectHealthReportById(Long id);

    /**
     * 根据学生ID查询健康报告列表
     * 
     * @param stuId 学生ID
     * @return 健康报告列表
     */
    List<HealthReport> selectHealthReportsByStuId(Long stuId);

    /**
     * 根据学生ID和时间范围查询健康报告
     * 
     * @param stuId 学生ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 健康报告列表
     */
    List<HealthReport> selectHealthReportsByStuIdAndTimeRange(Long stuId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取学生最新的健康报告
     * 
     * @param stuId 学生ID
     * @return 最新健康报告
     */
    HealthReport selectLatestHealthReportByStuId(Long stuId);

    /**
     * 新增健康报告
     * 
     * @param healthReport 健康报告
     * @return 结果
     */
    int insertHealthReport(HealthReport healthReport);

    /**
     * 修改健康报告
     * 
     * @param healthReport 健康报告
     * @return 结果
     */
    int updateHealthReport(HealthReport healthReport);

    /**
     * 批量删除健康报告
     * 
     * @param ids 需要删除的健康报告主键集合
     * @return 结果
     */
    int deleteHealthReportByIds(Long[] ids);

    /**
     * 删除健康报告信息
     * 
     * @param id 健康报告主键
     * @return 结果
     */
    int deleteHealthReportById(Long id);

    /**
     * 处理接收到的健康报告数据
     * 
     * @param fingerPrint 指纹数据
     * @param dataTaken 数据采集时间
     * @param reportData 报告数据
     * @param stuId 学生ID
     * @return 处理结果
     */
    boolean processHealthReportData(String fingerPrint, LocalDateTime dataTaken, String reportData, Long stuId);
} 