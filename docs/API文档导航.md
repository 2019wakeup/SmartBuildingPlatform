# 🚀 智能云平台API文档导航

## 📖 在线文档（推荐）
**立即访问**: http://localhost:8080/api/swagger-ui/index.html

> 💡 **使用提示**: 
> 1. 先用 `admin/admin123` 登录获取JWT令牌
> 2. 点击右上角"Authorize"按钮输入：`Bearer {token}`
> 3. 即可测试所有需要认证的接口

## 📚 API接口概览

### 🔐 认证接口
- **POST** `/auth/login` - 用户登录 
- **GET** `/auth/userinfo` - 获取用户信息
- **POST** `/auth/logout` - 用户登出
- **POST** `/auth/changePassword` - 修改密码

### 👥 用户管理
- **GET** `/system/user/list` - 用户列表
- **POST** `/system/user` - 新增用户
- **PUT** `/system/user` - 修改用户
- **DELETE** `/system/user/{ids}` - 删除用户

### 🎭 角色管理
- **GET** `/system/role/list` - 角色列表
- **POST** `/system/role` - 新增角色
- **PUT** `/system/role` - 修改角色
- **DELETE** `/system/role/{ids}` - 删除角色

## 🛠️ 静态文档生成

### 快速生成
```bash
python3 scripts/simple_generate_docs.py
```

### 查看生成的文档
```bash
# 启动HTTP服务器
python3 -m http.server 3000

# 访问：http://localhost:3000/generated-docs/api-docs.html
```

## 📋 完整文档目录

### 📚 详细文档
- [完整项目文档](./README.md)
- [文档中心](./docs/README.md)

### 🔐 认证相关
- [JWT原理说明](./docs/JWT令牌原理说明.md)
- [登录功能说明](./docs/登录功能使用说明.md)

### 🧪 测试相关
- [登录功能测试](./docs/测试登录功能.md)
- [完整测试说明](./docs/测试说明.md)

---

**快速开始**: 启动应用 → 访问Swagger → 登录测试 → 开始使用！ 