# 智能云平台前端项目

基于 Vue3 + TypeScript + Element Plus 的前后端分离管理系统前端

## 🚀 项目特性

- ✅ **用户认证系统** - 完整的登录/登出功能，JWT token 认证
- ✅ **现代化UI设计** - 基于 Element Plus 的美观界面
- ✅ **响应式布局** - 支持移动端和桌面端
- ✅ **路由守卫** - 自动认证检查和页面保护
- ✅ **Service Hub** - 统一的功能服务入口
- ✅ **角色权限管理** - 用户、角色、权限的完整管理
- ✅ **TypeScript** - 完整的类型支持

## 📦 技术栈

- **Vue 3** - 渐进式 JavaScript 框架
- **TypeScript** - JavaScript 的超集
- **Element Plus** - Vue 3 组件库
- **Vue Router** - 官方路由管理器
- **Axios** - HTTP 客户端
- **Vite** - 现代化构建工具

## 🛠️ 开发环境要求

- Node.js >= 16.0.0
- npm >= 8.0.0

## 📖 快速开始

### 1. 安装依赖
```bash
npm install
```

### 2. 启动开发服务器
```bash
npm run dev
```

### 3. 构建生产版本
```bash
npm run build
```

## 🎯 功能模块

### 1. 用户认证
- **登录页面** (`/login`) - 用户名密码登录
- **JWT认证** - 自动token管理和刷新
- **路由守卫** - 未登录自动跳转到登录页

### 2. 主要页面
- **首页** (`/home`) - 系统概览和统计信息
- **Service Hub** (`/service-hub`) - 功能服务中心
- **用户管理** (`/user`) - 用户信息的增删改查
- **角色管理** (`/role`) - 角色配置和权限分配
- **权限管理** (`/permission`) - 系统权限设置

### 3. 布局组件
- **侧边栏导航** - 层级菜单结构
- **顶部导航栏** - 用户信息和操作菜单
- **面包屑导航** - 当前页面位置显示

## 🔧 API 接口

项目与后端 API 的交互遵循以下规范：

### 认证相关
- `POST /api/auth/login` - 用户登录
- `POST /api/auth/logout` - 用户登出
- `GET /api/auth/userinfo` - 获取当前用户信息
- `POST /api/auth/changePassword` - 修改密码

### 用户管理
- `GET /api/system/user/list` - 获取用户列表
- `GET /api/system/user/{id}` - 获取用户详情
- `POST /api/system/user` - 新增用户
- `PUT /api/system/user` - 修改用户
- `DELETE /api/system/user/{ids}` - 删除用户

### 角色管理
- `GET /api/system/role/list` - 获取角色列表
- `GET /api/system/role/{id}` - 获取角色详情
- `POST /api/system/role` - 新增角色
- `PUT /api/system/role` - 修改角色
- `DELETE /api/system/role/{ids}` - 删除角色

## 🎨 UI 设计

### 设计原则
- **简洁明了** - 清晰的视觉层次和信息架构
- **一致性** - 统一的设计语言和交互模式
- **响应式** - 适配不同屏幕尺寸
- **用户友好** - 直观的操作流程和反馈

### 色彩方案
- **主色调** - 渐变紫色 (#667eea - #764ba2)
- **辅助色** - 各功能模块的特色渐变
- **中性色** - 灰色系用于文本和背景

## 🔐 安全特性

- **JWT Token** - 安全的身份认证
- **路由守卫** - 防止未授权访问
- **请求拦截** - 自动添加认证头
- **错误处理** - 统一的错误提示和处理

## 📱 响应式设计

项目采用响应式设计，支持以下设备：
- **桌面端** - 1200px 以上
- **平板端** - 768px - 1199px
- **移动端** - 767px 以下

## 🚀 部署说明

### 开发环境
```bash
npm run dev
```
访问 http://localhost:3000

### 生产环境
```bash
npm run build
```
构建文件输出到 `dist` 目录

### 代理配置
开发环境下，API 请求会自动代理到后端服务器：
```javascript
proxy: {
  '/api': {
    target: 'http://localhost:8080',
    changeOrigin: true
  }
}
```

## 📝 开发指南

### 项目结构
```
src/
├── api/           # API 接口定义
├── components/    # 公共组件
├── layout/        # 布局组件
├── router/        # 路由配置
├── utils/         # 工具函数
├── views/         # 页面组件
├── App.vue        # 根组件
└── main.ts        # 入口文件
```

### 代码规范
- 使用 TypeScript 进行类型检查
- 遵循 Vue 3 Composition API 规范
- 使用 ESLint 进行代码检查
- 统一的命名规范和注释

## 🐛 常见问题

### 1. 登录后跳转到空白页
检查路由配置和认证状态

### 2. API 请求失败
确认后端服务已启动，检查代理配置

### 3. 样式显示异常
检查 Element Plus 组件库是否正确引入

## 📞 技术支持

如有问题请联系开发团队或查看项目文档。

---

**智能云平台管理系统** - 让管理更简单，让数据更安全
