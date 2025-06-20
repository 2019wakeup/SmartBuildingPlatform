package org.example.smartcloudplatform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.smartcloudplatform.entity.BreathSample;

import java.util.List;

/**
 * 呼吸样本表 数据层
 * 
 * @author SmartCloudPlatform
 */
@Mapper
public interface BreathSampleMapper extends BaseMapper<BreathSample> {
    /**
     * 根据条件分页查询呼吸样本列表
     * 
     * @param breathSample 呼吸样本信息
     * @return 呼吸样本信息集合信息
     */
    List<BreathSample> selectBreathSampleList(BreathSample breathSample);

    /**
     * 通过样本ID查询呼吸样本
     * 
     * @param sampleId 样本ID
     * @return 呼吸样本对象信息
     */
    BreathSample selectBreathSampleById(Long sampleId);

    /**
     * 根据用户ID查询呼吸样本列表
     * 
     * @param userId 用户ID
     * @return 呼吸样本列表
     */
    List<BreathSample> selectBreathSamplesByUserId(Long userId);

    /**
     * 新增呼吸样本信息
     * 
     * @param breathSample 呼吸样本信息
     * @return 结果
     */
    int insertBreathSample(BreathSample breathSample);

    /**
     * 修改呼吸样本信息
     * 
     * @param breathSample 呼吸样本信息
     * @return 结果
     */
    int updateBreathSample(BreathSample breathSample);

    /**
     * 通过样本ID删除呼吸样本
     * 
     * @param sampleId 样本ID
     * @return 结果
     */
    int deleteBreathSampleById(Long sampleId);

    /**
     * 批量删除呼吸样本信息
     * 
     * @param sampleIds 需要删除的样本ID
     * @return 结果
     */
    int deleteBreathSampleByIds(Long[] sampleIds);

    /**
     * 根据用户ID删除呼吸样本信息
     * 
     * @param userId 用户ID
     * @return 结果
     */
    int deleteBreathSampleByUserId(Long userId);
} 