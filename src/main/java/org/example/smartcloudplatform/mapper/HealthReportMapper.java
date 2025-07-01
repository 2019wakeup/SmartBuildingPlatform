package org.example.smartcloudplatform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.smartcloudplatform.entity.HealthReport;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 健康报告Mapper接口
 * 
 * @author SmartCloudPlatform
 */
@Mapper
public interface HealthReportMapper extends BaseMapper<HealthReport> {

    /**
     * 根据条件分页查询健康报告列表
     * 
     * @param healthReport 健康报告信息
     * @return 健康报告信息集合信息
     */
    List<HealthReport> selectHealthReportList(HealthReport healthReport);

    /**
     * 通过报告ID查询健康报告
     * 
     * @param id 报告ID
     * @return 健康报告对象信息
     */
    HealthReport selectHealthReportById(Long id);

    /**
     * 根据学生ID查询健康报告列表
     * 
     * @param stuId 学生ID
     * @return 健康报告列表
     */
    @Select("SELECT * FROM health_report WHERE stu_id = #{stuId} ORDER BY data_taken DESC")
    List<HealthReport> selectHealthReportsByStuId(@Param("stuId") Long stuId);

    /**
     * 根据学生ID和时间范围查询健康报告
     * 
     * @param stuId 学生ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 健康报告列表
     */
    @Select("SELECT * FROM health_report WHERE stu_id = #{stuId} " +
            "AND data_taken BETWEEN #{startTime} AND #{endTime} " +
            "ORDER BY data_taken DESC")
    List<HealthReport> selectHealthReportsByStuIdAndTimeRange(
            @Param("stuId") Long stuId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    /**
     * 获取学生最新的健康报告
     * 
     * @param stuId 学生ID
     * @return 最新健康报告
     */
    @Select("SELECT * FROM health_report WHERE stu_id = #{stuId} " +
            "ORDER BY data_taken DESC LIMIT 1")
    HealthReport selectLatestHealthReportByStuId(@Param("stuId") Long stuId);

    /**
     * 新增健康报告信息
     * 
     * @param healthReport 健康报告信息
     * @return 结果
     */
    int insertHealthReport(HealthReport healthReport);

    /**
     * 修改健康报告信息
     * 
     * @param healthReport 健康报告信息
     * @return 结果
     */
    int updateHealthReport(HealthReport healthReport);

    /**
     * 通过报告ID删除健康报告
     * 
     * @param id 报告ID
     * @return 结果
     */
    int deleteHealthReportById(Long id);

    /**
     * 批量删除健康报告信息
     * 
     * @param ids 需要删除的报告ID
     * @return 结果
     */
    int deleteHealthReportByIds(Long[] ids);

    /**
     * 根据学生ID删除健康报告信息
     * 
     * @param stuId 学生ID
     * @return 结果
     */
    int deleteHealthReportByStuId(Long stuId);
} 