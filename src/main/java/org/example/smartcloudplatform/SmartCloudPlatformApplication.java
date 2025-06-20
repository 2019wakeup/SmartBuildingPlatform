package org.example.smartcloudplatform;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class,
        org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration.class,
        com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration.class
})
// @MapperScan("org.example.smartcloudplatform.mapper") // 暂时注释，等配置数据源后再启用
public class SmartCloudPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartCloudPlatformApplication.class, args);
        System.out.println("智能云平台管理系统启动成功！");
    }

}
