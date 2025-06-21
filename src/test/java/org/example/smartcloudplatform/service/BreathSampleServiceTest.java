package org.example.smartcloudplatform.service;

import org.example.smartcloudplatform.entity.BreathSample;
import org.example.smartcloudplatform.mapper.BreathSampleMapper;
import org.example.smartcloudplatform.service.impl.BreathSampleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * 呼吸样本服务测试类
 */
@ExtendWith(MockitoExtension.class)
class BreathSampleServiceTest {

    @Mock
    private BreathSampleMapper breathSampleMapper;

    @InjectMocks
    private BreathSampleServiceImpl breathSampleService;

    private BreathSample testSample;

    @BeforeEach
    void setUp() {
        testSample = new BreathSample();
        testSample.setSampleId(1L);
        testSample.setUserId(1L);
        testSample.setDataTaken(LocalDateTime.now());
    }

    @Test
    void testSelectBreathSampleList() {
        // 准备测试数据
        List<BreathSample> expectedSamples = Arrays.asList(testSample);
        when(breathSampleMapper.selectBreathSampleList(any(BreathSample.class))).thenReturn(expectedSamples);

        // 执行测试
        List<BreathSample> result = breathSampleService.selectBreathSampleList(new BreathSample());

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getSampleId());
        assertEquals(1L, result.get(0).getUserId());
        verify(breathSampleMapper, times(1)).selectBreathSampleList(any(BreathSample.class));
    }

    @Test
    void testSelectBreathSampleById() {
        // 准备测试数据
        when(breathSampleMapper.selectBreathSampleById(1L)).thenReturn(testSample);

        // 执行测试
        BreathSample result = breathSampleService.selectBreathSampleById(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals(1L, result.getSampleId());
        assertEquals(1L, result.getUserId());
        assertNotNull(result.getDataTaken());
        verify(breathSampleMapper, times(1)).selectBreathSampleById(1L);
    }

    @Test
    void testSelectBreathSamplesByUserId() {
        // 准备测试数据
        BreathSample sample2 = new BreathSample();
        sample2.setSampleId(2L);
        sample2.setUserId(1L);
        sample2.setDataTaken(LocalDateTime.now().minusHours(1));
        
        List<BreathSample> expectedSamples = Arrays.asList(testSample, sample2);
        when(breathSampleMapper.selectBreathSamplesByUserId(1L)).thenReturn(expectedSamples);

        // 执行测试
        List<BreathSample> result = breathSampleService.selectBreathSamplesByUserId(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getUserId());
        assertEquals(1L, result.get(1).getUserId());
        verify(breathSampleMapper, times(1)).selectBreathSamplesByUserId(1L);
    }

    @Test
    void testInsertBreathSample() {
        // 准备测试数据
        when(breathSampleMapper.insertBreathSample(any(BreathSample.class))).thenReturn(1);

        // 执行测试
        int result = breathSampleService.insertBreathSample(testSample);

        // 验证结果
        assertEquals(1, result);
        verify(breathSampleMapper, times(1)).insertBreathSample(testSample);
    }

    @Test
    void testUpdateBreathSample() {
        // 准备测试数据
        testSample.setDataTaken(LocalDateTime.now().minusHours(2));
        when(breathSampleMapper.updateBreathSample(any(BreathSample.class))).thenReturn(1);

        // 执行测试
        int result = breathSampleService.updateBreathSample(testSample);

        // 验证结果
        assertEquals(1, result);
        verify(breathSampleMapper, times(1)).updateBreathSample(testSample);
    }

    @Test
    void testDeleteBreathSampleById() {
        // 准备测试数据
        when(breathSampleMapper.deleteBreathSampleById(1L)).thenReturn(1);

        // 执行测试
        int result = breathSampleService.deleteBreathSampleById(1L);

        // 验证结果
        assertEquals(1, result);
        verify(breathSampleMapper, times(1)).deleteBreathSampleById(1L);
    }

    @Test
    void testDeleteBreathSampleByIds() {
        // 准备测试数据
        Long[] sampleIds = {1L, 2L, 3L};
        when(breathSampleMapper.deleteBreathSampleByIds(sampleIds)).thenReturn(3);

        // 执行测试
        int result = breathSampleService.deleteBreathSampleByIds(sampleIds);

        // 验证结果
        assertEquals(3, result);
        verify(breathSampleMapper, times(1)).deleteBreathSampleByIds(sampleIds);
    }

    @Test
    void testDeleteBreathSampleByUserId() {
        // 准备测试数据
        when(breathSampleMapper.deleteBreathSampleByUserId(1L)).thenReturn(2);

        // 执行测试
        int result = breathSampleService.deleteBreathSampleByUserId(1L);

        // 验证结果
        assertEquals(2, result);
        verify(breathSampleMapper, times(1)).deleteBreathSampleByUserId(1L);
    }

    @Test
    void testSelectBreathSampleList_WithConditions() {
        // 准备测试数据 - 带条件查询
        BreathSample condition = new BreathSample();
        condition.setUserId(1L);
        condition.setDataTaken(LocalDateTime.now());
        
        List<BreathSample> expectedSamples = Arrays.asList(testSample);
        when(breathSampleMapper.selectBreathSampleList(condition)).thenReturn(expectedSamples);

        // 执行测试
        List<BreathSample> result = breathSampleService.selectBreathSampleList(condition);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getUserId());
        verify(breathSampleMapper, times(1)).selectBreathSampleList(condition);
    }

    @Test
    void testSelectBreathSampleById_NotFound() {
        // 准备测试数据 - 样本不存在
        when(breathSampleMapper.selectBreathSampleById(999L)).thenReturn(null);

        // 执行测试
        BreathSample result = breathSampleService.selectBreathSampleById(999L);

        // 验证结果
        assertNull(result);
        verify(breathSampleMapper, times(1)).selectBreathSampleById(999L);
    }

    @Test
    void testInsertBreathSample_Failed() {
        // 准备测试数据 - 插入失败
        when(breathSampleMapper.insertBreathSample(any(BreathSample.class))).thenReturn(0);

        // 执行测试
        int result = breathSampleService.insertBreathSample(testSample);

        // 验证结果
        assertEquals(0, result);
        verify(breathSampleMapper, times(1)).insertBreathSample(testSample);
    }
} 