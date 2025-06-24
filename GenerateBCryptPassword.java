import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GenerateBCryptPassword {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "123456";
        String encodedPassword = encoder.encode(password);
        
        System.out.println("原始密码: " + password);
        System.out.println("BCrypt哈希: " + encodedPassword);
        
        // 验证密码
        boolean matches = encoder.matches(password, encodedPassword);
        System.out.println("密码验证: " + matches);
        
        // 验证现有的哈希值
        String existingHash = "$2a$10$7JB720yubVSOfvVWHWK8VeKTfVCgR0AhMZUgLXjKQlRnxdJdYGOZK";
        boolean existingMatches = encoder.matches(password, existingHash);
        System.out.println("现有哈希验证: " + existingMatches);
    }
} 