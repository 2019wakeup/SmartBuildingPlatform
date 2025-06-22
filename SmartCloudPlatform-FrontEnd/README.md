# 智能云平台管理系统 - 前端项目

基于Vue3 + Vite + Element Plus的前后端分离管理系统前端项目。

## 技术栈

- **Vue 3.3.4** - 渐进式JavaScript框架
- **Vite 4.4.5** - 现代化前端构建工具
- **Element Plus 2.3.8** - 基于Vue 3的桌面端组件库
- **TypeScript 5.0.2** - JavaScript的超集
- **Vue Router 4.2.4** - Vue.js官方路由管理器
- **Pinia 2.1.6** - Vue的状态管理库
- **Axios 1.4.0** - HTTP客户端库

## 项目结构

```
src/
├── api/                 # API接口
│   ├── request.ts      # axios封装
│   ├── types.ts        # 类型定义
│   ├── user.ts         # 用户相关接口
│   └── role.ts         # 角色相关接口
├── layout/             # 布局组件
│   └── index.vue       # 主布局
├── router/             # 路由配置
│   └── index.ts        # 路由定义
├── views/              # 页面组件
│   ├── user/           # 用户管理
│   ├── role/           # 角色管理
│   └── permission/     # 权限管理
├── App.vue             # 根组件
├── main.ts             # 入口文件
└── style.css           # 全局样式
```

## 功能特性

### 用户管理
- ✅ 用户列表查询
- ✅ 用户信息的增删改查
- ✅ 用户状态管理
- ✅ 密码重置
- ✅ 用户角色授权
- ✅ 分页查询

### 角色管理
- ✅ 角色列表查询
- ✅ 角色信息的增删改查
- ✅ 分页查询

### 权限管理
- ✅ 权限列表查询
- ✅ 权限信息的增删改查
- ✅ 分页查询

### UI特性
- ✅ 响应式设计
- ✅ 现代化界面
- ✅ 统一的视觉风格
- ✅ 良好的用户体验

## 安装和运行

### 环境要求
- Node.js >= 16.0.0
- npm >= 7.0.0

### 安装依赖
```bash
cd SmartCloudPlatform-FrontEnd
npm install
```

### 开发环境运行
```bash
npm run dev
```
访问 http://localhost:3000

### 生产环境构建
```bash
npm run build
```

### 预览构建结果
```bash
npm run preview
```

## API接口

项目严格按照后端API文档进行接口调用，主要包括：

### 用户管理接口
- `GET /api/system/user/list` - 获取用户列表
- `POST /api/system/user` - 新增用户
- `PUT /api/system/user` - 修改用户
- `DELETE /api/system/user/{userIds}` - 删除用户
- `PUT /api/system/user/changeStatus` - 修改用户状态
- `PUT /api/system/user/resetPwd` - 重置密码
- `GET /api/system/user/authRole/{userId}` - 获取用户角色
- `PUT /api/system/user/authRole` - 用户授权角色

### 角色管理接口
- `GET /api/system/role/list` - 获取角色列表
- `POST /api/system/role` - 新增角色
- `PUT /api/system/role` - 修改角色
- `DELETE /api/system/role/{roleIds}` - 删除角色
- `GET /api/system/role/optionselect` - 获取角色选项

## 配置说明

### Vite配置
- 自动导入Element Plus组件
- 配置代理转发后端接口
- TypeScript支持
- 路径别名配置

### 代理配置
开发环境下，所有`/api`请求会被代理到`http://localhost:8080`

## 开发规范

### 代码风格
- 使用TypeScript进行类型检查
- 遵循Vue 3 Composition API规范
- 使用ESLint进行代码检查

### 组件规范
- 使用`<script setup>`语法
- 合理使用响应式API
- 组件命名采用PascalCase

### API调用规范
- 统一使用封装的request方法
- 错误处理统一在拦截器中处理
- 接口类型定义在types.ts中

## 注意事项

1. 确保后端服务已启动并运行在8080端口
2. 所有API接口严格按照后端文档规范调用
3. 开发时注意浏览器控制台的错误信息
4. 建议使用现代浏览器进行开发和测试

## 浏览器支持

- Chrome >= 87
- Firefox >= 78
- Safari >= 14
- Edge >= 88

## 许可证

本项目采用Apache 2.0许可证。
