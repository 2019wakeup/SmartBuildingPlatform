package org.example.smartcloudplatform.service.impl;

import org.example.smartcloudplatform.entity.BreathSample;
import org.example.smartcloudplatform.mapper.BreathSampleMapper;
import org.example.smartcloudplatform.service.IBreathSampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 呼吸样本 业务层处理
 * 
 * @author SmartCloudPlatform
 */
@Service
public class BreathSampleServiceImpl implements IBreathSampleService {
    @Autowired
    private BreathSampleMapper breathSampleMapper;

    /**
     * 根据条件分页查询呼吸样本列表
     * 
     * @param breathSample 呼吸样本信息
     * @return 呼吸样本信息集合信息
     */
    @Override
    public List<BreathSample> selectBreathSampleList(BreathSample breathSample) {
        return breathSampleMapper.selectBreathSampleList(breathSample);
    }

    /**
     * 通过样本ID查询呼吸样本
     * 
     * @param sampleId 样本ID
     * @return 呼吸样本对象信息
     */
    @Override
    public BreathSample selectBreathSampleById(Long sampleId) {
        return breathSampleMapper.selectBreathSampleById(sampleId);
    }

    /**
     * 根据用户ID查询呼吸样本列表
     * 
     * @param userId 用户ID
     * @return 呼吸样本列表
     */
    @Override
    public List<BreathSample> selectBreathSamplesByUserId(Long userId) {
        return breathSampleMapper.selectBreathSamplesByUserId(userId);
    }

    /**
     * 新增呼吸样本信息
     * 
     * @param breathSample 呼吸样本信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertBreathSample(BreathSample breathSample) {
        return breathSampleMapper.insertBreathSample(breathSample);
    }

    /**
     * 修改呼吸样本信息
     * 
     * @param breathSample 呼吸样本信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateBreathSample(BreathSample breathSample) {
        return breathSampleMapper.updateBreathSample(breathSample);
    }

    /**
     * 通过样本ID删除呼吸样本
     * 
     * @param sampleId 样本ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteBreathSampleById(Long sampleId) {
        return breathSampleMapper.deleteBreathSampleById(sampleId);
    }

    /**
     * 批量删除呼吸样本信息
     * 
     * @param sampleIds 需要删除的样本ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteBreathSampleByIds(Long[] sampleIds) {
        return breathSampleMapper.deleteBreathSampleByIds(sampleIds);
    }

    /**
     * 根据用户ID删除呼吸样本信息
     * 
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteBreathSampleByUserId(Long userId) {
        return breathSampleMapper.deleteBreathSampleByUserId(userId);
    }
} 