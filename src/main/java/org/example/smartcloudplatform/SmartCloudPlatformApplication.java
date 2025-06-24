package org.example.smartcloudplatform;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration.class
})
@MapperScan("org.example.smartcloudplatform.mapper")
public class SmartCloudPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartCloudPlatformApplication.class, args);
        System.out.println("智能云平台管理系统启动成功！");
    }

}
