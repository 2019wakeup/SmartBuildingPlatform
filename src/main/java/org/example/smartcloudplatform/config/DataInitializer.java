package org.example.smartcloudplatform.config;

import cn.hutool.crypto.digest.BCrypt;
import org.example.smartcloudplatform.entity.User;
import org.example.smartcloudplatform.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 数据初始化器
 * 系统启动时创建默认管理员用户
 * 
 * @author SmartCloudPlatform
 */
@Component
public class DataInitializer implements ApplicationRunner {
    
    @Autowired
    private IUserService userService;
    
    @Override
    public void run(ApplicationArguments args) throws Exception {
        initAdminUser();
    }
    
    /**
     * 初始化管理员用户
     */
    private void initAdminUser() {
        try {
            // 检查是否已存在admin用户
            User existingUser = userService.selectUserByAccount("admin");
            if (existingUser == null) {
                // 创建默认管理员用户
                User adminUser = new User();
                adminUser.setUserName("系统管理员");
                adminUser.setAccount("admin");
                adminUser.setPassword("123456"); // 会在service层加密
                adminUser.setEmail("admin@smartcloud.com");
                adminUser.setPhone("13800138000");
                adminUser.setCreateBy("system");
                
                int result = userService.insertUser(adminUser);
                if (result > 0) {
                    System.out.println("默认管理员用户创建成功：账号=admin，密码=123456");
                }
            } else {
                // 用户已存在，重置密码为123456
                System.out.println("管理员用户已存在，重置密码为123456");
                // 注意：resetUserPwd方法使用的是userName，不是account
                int result = userService.resetUserPwd("系统管理员", "123456");
                if (result > 0) {
                    System.out.println("管理员密码重置成功：账号=admin，密码=123456");
                } else {
                    System.out.println("管理员密码重置失败");
                }
            }
        } catch (Exception e) {
            System.err.println("初始化管理员用户失败：" + e.getMessage());
        }
    }
} 