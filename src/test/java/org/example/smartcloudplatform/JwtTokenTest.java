package org.example.smartcloudplatform;

import org.example.smartcloudplatform.common.JwtUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * JWT令牌测试类
 * 演示JWT令牌的生成、解析和验证过程
 * 
 * @author SmartCloudPlatform
 */
@SpringBootTest
public class JwtTokenTest {
    
    @Autowired
    private JwtUtils jwtUtils;
    
    @Test
    public void testJwtTokenGeneration() {
        System.out.println("=== JWT令牌生成和验证演示 ===");
        
        // 1. 生成JWT令牌
        Long userId = 1L;
        String account = "admin";
        String token = jwtUtils.generateToken(userId, account);
        
        System.out.println("1. 生成的JWT令牌：");
        System.out.println(token);
        System.out.println();
        
        // 2. 解析JWT令牌结构
        String[] parts = token.split("\\.");
        System.out.println("2. JWT令牌结构分析：");
        System.out.println("Header（头部）: " + parts[0]);
        System.out.println("Payload（载荷）: " + parts[1]);
        System.out.println("Signature（签名）: " + parts[2]);
        System.out.println();
        
        // 3. 从令牌中提取信息
        System.out.println("3. 从令牌中提取的信息：");
        Long extractedUserId = jwtUtils.getUserIdFromToken(token);
        String extractedAccount = jwtUtils.getAccountFromToken(token);
        System.out.println("用户ID: " + extractedUserId);
        System.out.println("用户账号: " + extractedAccount);
        System.out.println();
        
        // 4. 验证令牌有效性
        System.out.println("4. 令牌验证结果：");
        boolean isValid = jwtUtils.validateToken(token);
        boolean isExpired = jwtUtils.isTokenExpired(token);
        System.out.println("令牌是否有效: " + isValid);
        System.out.println("令牌是否过期: " + isExpired);
        System.out.println();
        
        // 5. 测试无效令牌
        System.out.println("5. 测试无效令牌：");
        String invalidToken = token + "invalid";
        boolean isInvalidTokenValid = jwtUtils.validateToken(invalidToken);
        System.out.println("无效令牌验证结果: " + isInvalidTokenValid);
        System.out.println();
        
        System.out.println("=== JWT令牌测试完成 ===");
        System.out.println("关键点总结：");
        System.out.println("1. JWT令牌包含所有必要信息，无需存储到数据库");
        System.out.println("2. 通过数字签名确保令牌未被篡改");
        System.out.println("3. 服务器可以直接验证令牌的有效性");
        System.out.println("4. 令牌一旦生成，在过期前都是有效的");
    }
} 