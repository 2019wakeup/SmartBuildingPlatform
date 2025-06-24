package org.example.smartcloudplatform.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * 测试环境 MyBatis Plus 配置
 */
@TestConfiguration
public class TestMybatisPlusConfig {
    
    /**
     * 分页插件 - 测试环境使用H2数据库
     */
    @Bean
    @Primary
    public MybatisPlusInterceptor testMybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 测试环境使用H2数据库
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.H2));
        return interceptor;
    }
} 