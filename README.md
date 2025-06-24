# 智能云平台管理系统 (SmartCloudPlatform)

## 📋 项目简介

基于SpringBoot3 + MyBatis Plus的前后端分离管理系统，提供用户管理、角色管理、权限控制等核心功能。

## 🚀 技术栈

### 后端技术
- **框架**: Spring Boot 3.1.12
- **数据库**: MySQL 8.0 + MyBatis Plus 3.5.8
- **安全**: Spring Security + JWT
- **文档**: SpringDoc OpenAPI 3 (Swagger)
- **工具**: Hutool + Lombok
- **测试**: JUnit 5 + Mockito

### 前端技术
- **框架**: Vue 3 + TypeScript
- **构建**: Vite
- **UI**: Element Plus
- **HTTP**: Axios

## 🎯 核心功能

### 🔐 用户认证系统
- **JWT令牌认证** - 无状态认证，支持分布式
- **用户登录/登出** - 安全的身份验证
- **密码加密** - BCrypt算法保护用户密码
- **权限控制** - 基于角色的访问控制

### 👥 用户管理
- 用户信息的增删改查
- 用户状态管理（启用/禁用）
- 用户角色分配
- 密码重置功能

### 🎭 角色管理  
- 角色信息的增删改查
- 角色权限分配
- 角色用户关联管理

## 📖 API文档

### 🌐 在线文档
启动应用后访问：**http://localhost:8080/api/swagger-ui/index.html**

> 💡 **重要提示**: API文档按功能分组显示，请在页面右上角的下拉框中切换查看不同分组：
> - **用户认证** - 登录相关接口
> - **系统管理** - 用户和角色管理接口  
> - **首页** - 基础接口

📋 **[查看完整API地址列表](./API文档完整地址列表.md)**

### 📚 接口分组

#### 🔑 用户认证 (`/auth`)
| 方法 | 路径 | 描述 | 认证 |
|------|------|------|------|
| POST | `/auth/login` | 用户登录 | ❌ |
| POST | `/auth/logout` | 用户登出 | ✅ |
| GET | `/auth/userinfo` | 获取当前用户信息 | ✅ |
| POST | `/auth/changePassword` | 修改密码 | ✅ |

#### 👥 用户管理 (`/system/user`)
| 方法 | 路径 | 描述 | 认证 |
|------|------|------|------|
| GET | `/system/user/list` | 获取用户列表 | ✅ |
| GET | `/system/user/{userId}` | 获取用户详情 | ✅ |
| POST | `/system/user` | 新增用户 | ✅ |
| PUT | `/system/user` | 修改用户 | ✅ |
| DELETE | `/system/user/{userIds}` | 删除用户 | ✅ |
| PUT | `/system/user/resetPwd` | 重置密码 | ✅ |
| PUT | `/system/user/changeStatus` | 修改用户状态 | ✅ |
| GET | `/system/user/authRole/{userId}` | 获取用户角色 | ✅ |
| PUT | `/system/user/authRole` | 分配用户角色 | ✅ |

#### 🎭 角色管理 (`/system/role`)
| 方法 | 路径 | 描述 | 认证 |
|------|------|------|------|
| GET | `/system/role/list` | 获取角色列表 | ✅ |
| GET | `/system/role/{roleId}` | 获取角色详情 | ✅ |
| POST | `/system/role` | 新增角色 | ✅ |
| PUT | `/system/role` | 修改角色 | ✅ |
| DELETE | `/system/role/{roleIds}` | 删除角色 | ✅ |
| GET | `/system/role/optionselect` | 获取角色选项 | ✅ |

## 🔧 快速开始

### 环境要求
- JDK 17+
- Maven 3.6+
- MySQL 8.0+
- Node.js 16+ (前端)

### 后端启动
```bash
# 1. 克隆项目
git clone <repository-url>
cd SmartCloudPlatform

# 2. 配置数据库
# 修改 src/main/resources/application.yml 中的数据库配置

# 3. 启动应用
./mvnw spring-boot:run

# 4. 访问文档
# http://localhost:8080/api/swagger-ui/index.html
```

### 前端启动
```bash
# 1. 进入前端目录
cd SmartCloudPlatform-FrontEnd

# 2. 安装依赖
npm install

# 3. 启动开发服务器
npm run dev
```

## 🔐 认证使用

### 1. 登录获取JWT令牌
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"account":"admin","password":"admin123"}'
```

### 2. 使用令牌访问API
```bash
curl -X GET http://localhost:8080/api/auth/userinfo \
  -H "Authorization: Bearer {your-jwt-token}"
```

### 3. Swagger中使用JWT
1. 在Swagger页面点击右上角"Authorize"按钮
2. 输入：`Bearer {your-jwt-token}`
3. 点击"Authorize"完成认证

## 🔑 默认账号

| 账号 | 密码 | 角色 | 说明 |
|------|------|------|------|
| admin | admin123 | 管理员 | 系统默认管理员账号 |

## 🧪 测试

### 运行测试
```bash
# 运行所有测试
./mvnw test

# 运行特定测试
./mvnw test -Dtest=UserServiceTest
./mvnw test -Dtest=AuthControllerTest
```

### 测试覆盖
- ✅ 单元测试 - 服务层业务逻辑
- ✅ 集成测试 - 控制器层API
- ✅ 实体测试 - 数据模型验证
- ✅ JWT测试 - 认证功能验证

## 📄 API文档导出

### 静态文档生成
```bash
# 方法1：简化版本（推荐）
python3 scripts/simple_generate_docs.py

# 方法2：完整版本
python3 scripts/generate_docs.py

# 方法3：Shell脚本
./scripts/generate-docs.sh
```

### 生成文件
生成的文档将保存在 `generated-docs/` 目录中：
- `generated-docs/api-docs.json` - OpenAPI 3.0规范文件
- `generated-docs/api-docs.html` - 静态HTML文档

## 🏗️ 项目结构

```
SmartCloudPlatform/
├── src/main/java/org/example/smartcloudplatform/
│   ├── common/          # 公共工具类
│   │   ├── AjaxResult.java      # 统一响应格式
│   │   ├── JwtUtils.java        # JWT工具类
│   │   └── TableDataInfo.java   # 分页响应格式
│   ├── config/          # 配置类
│   │   ├── SecurityConfig.java  # 安全配置
│   │   ├── SwaggerConfig.java   # API文档配置
│   │   └── JwtAuthenticationFilter.java # JWT过滤器
│   ├── controller/      # 控制器层
│   │   ├── AuthController.java  # 认证控制器
│   │   └── system/             # 系统管理控制器
│   ├── entity/          # 实体类
│   │   ├── User.java           # 用户实体
│   │   ├── Role.java           # 角色实体
│   │   └── LoginRequest.java   # 登录请求DTO
│   ├── mapper/          # 数据访问层
│   ├── service/         # 业务逻辑层
│   └── SmartCloudPlatformApplication.java
├── src/main/resources/
│   ├── application.yml  # 应用配置
│   └── mapper/          # MyBatis映射文件
├── src/test/           # 测试代码
├── SmartCloudPlatform-FrontEnd/  # 前端项目
├── docs/               # 项目文档
│   ├── README.md       # 文档中心索引
│   ├── JWT令牌原理说明.md
│   ├── 登录功能使用说明.md
│   ├── 测试登录功能.md
│   ├── API文档导出说明.md
│   └── 测试说明.md
├── scripts/            # 工具脚本
│   ├── generate_docs.py
│   ├── simple_generate_docs.py
│   └── generate-docs.sh
├── generated-docs/     # 生成的API文档
│   ├── api-docs.json
│   └── api-docs.html
└── README.md           # 项目主文档
```

## 🔧 配置说明

### 数据库配置
```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/cloudplatform
    username: root
    password: your-password
```

### JWT配置
```yaml
# JWT密钥和过期时间在代码中配置
# 密钥: SmartCloudPlatformJWTSecretKeyForUserAuthentication2024
# 过期时间: 7天
```

## 🚨 注意事项

1. **安全性**
   - 生产环境请修改JWT密钥
   - 使用HTTPS传输
   - 定期更换管理员密码

2. **数据库**
   - 确保MySQL服务正常运行
   - 检查数据库连接配置
   - 初次启动会自动创建管理员用户

3. **端口配置**
   - 后端默认端口：8080
   - 前端默认端口：3000
   - 确保端口未被占用

## 📞 技术支持

- **项目地址**: [GitHub Repository]
- **问题反馈**: [Issues]
- **邮箱**: admin@smartcloud.com

## 📄 许可证

本项目采用 Apache 2.0 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

---

## 🔗 相关文档链接

- [📚 文档中心](./docs/README.md) - 所有文档的统一入口
- [🚀 API文档完整地址列表](./API文档完整地址列表.md) - 所有API接口地址
- [JWT令牌原理说明](./docs/JWT令牌原理说明.md)
- [登录功能使用说明](./docs/登录功能使用说明.md)
- [测试功能说明](./docs/测试登录功能.md)
- [API文档导出说明](./docs/API文档导出说明.md)

**最后更新**: 2025-06-22 