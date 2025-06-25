# IoT数据可视化功能使用说明

## 功能概述

SmartCloudPlatform现已集成完整的IoT设备数据可视化功能，包括：

- 📊 **实时数据监控仪表板** - 图表展示设备传感器数据趋势
- 🖥️ **设备管理界面** - 设备注册、状态监控、配置管理
- 📡 **数据接收接口** - 支持多种数据格式的设备接入
- 🔄 **自动化任务** - 离线检测、数据清理、统计分析

## 快速开始

### 1. 启动系统

#### 后端服务
```bash
# 在项目根目录
./mvnw spring-boot:run
```

#### 前端服务
```bash
# 在SmartCloudPlatform-FrontEnd目录
npm run dev
```

### 2. 访问界面

- **前端地址**: http://localhost:5173
- **后端API**: http://localhost:8080
- **API文档**: http://localhost:8080/swagger-ui/index.html

### 3. 登录系统

- **用户名**: admin
- **密码**: admin123

## 主要功能

### IoT数据监控仪表板

访问路径：`IoT物联网 > 数据监控`

**功能特性：**
- 📈 实时数据图表展示（CO₂、TVOC、温度、湿度）
- 📊 设备状态统计（总数、在线、离线）
- 🎯 当前数值实时显示
- 🔄 自动刷新（30秒间隔）
- 📱 响应式设计，支持移动端

**支持的数据类型：**
- CO₂浓度 (ppm)
- TVOC浓度 (ppb) 
- 芯片温度 (℃)
- 环境温度 (℃)
- 环境湿度 (%)

### IoT设备管理

访问路径：`IoT物联网 > 设备管理`

**功能特性：**
- ➕ 设备注册和配置
- 📋 设备列表和状态监控
- 🔍 设备搜索和筛选
- 📊 设备详情查看
- 🔄 离线设备检查

## 设备接入配置

### 支持的接入方式

#### 1. HTTP Webhook接口
```
POST /iot/data/webhook/{deviceId}
Content-Type: application/json

{
  "status": {
    "co2 ppm": "4469",
    "tvoc ppb": "331",
    "chip temperature": "51.9℃",
    "env temperature": "29.0℃", 
    "env humidity": "77.6%"
  }
}
```

#### 2. 通用数据接收接口
```
POST /iot/data/receive?deviceId=your_device_id
Content-Type: application/json

{
  "co2": 4469,
  "tvoc": 331,
  "temperature": 29.0,
  "humidity": 77.6
}
```

### ThingsCloud平台配置

1. **登录ThingsCloud管理平台**
2. **选择设备 > 数据转发**
3. **配置HTTP转发**：
   - 转发地址：`http://your-server:8080/iot/data/webhook/{设备ID}`
   - 请求方法：`POST`
   - 内容类型：`application/json`

## 测试和调试

### 使用测试脚本

项目根目录提供了测试脚本 `test_iot_data.py`：

```bash
# 安装依赖
pip install requests

# 运行测试脚本
python test_iot_data.py
```

**测试脚本功能：**
- 自动注册测试设备
- 发送模拟传感器数据
- 模拟真实设备数据格式
- 支持多设备并发测试

### 手动测试接口

```bash
# 注册设备
curl -X POST "http://localhost:8080/iot/devices/register" \
  -d "deviceId=test_device&deviceName=测试设备&deviceLocation=实验室"

# 发送测试数据
curl -X POST "http://localhost:8080/iot/data/webhook/test_device" \
  -H "Content-Type: application/json" \
  -d '{
    "status": {
      "co2 ppm": "1200",
      "tvoc ppb": "150",
      "env temperature": "25.5℃",
      "env humidity": "65.0%"
    }
  }'

# 查看设备统计
curl "http://localhost:8080/iot/devices/statistics"
```

## 数据存储

### 数据库表结构

#### iot_sensor_data (传感器历史数据)
- 存储所有历史传感器数据
- 支持按设备ID和时间范围查询  
- 自动清理30天前的数据

#### iot_device_status (设备状态)
- 存储设备当前状态和最新数据
- 实时更新设备在线/离线状态
- 记录设备注册信息和位置

### 数据聚合策略

**变化阈值存储：**
- CO₂变化 > 100 ppm
- TVOC变化 > 50 ppb
- 温度变化 > 2.0℃  
- 湿度变化 > 5.0%

**强制存储规则：**
- 超过30分钟未存储则强制存储一次
- 确保数据连续性和完整性

## 自动化任务

### 定时任务配置

- **离线检查**: 每10分钟检查一次离线设备
- **数据清理**: 每天凌晨2点清理30天前的数据
- **统计更新**: 实时更新设备统计信息

### 监控和告警

可以通过日志监控系统状态：

```bash
# 查看应用日志
tail -f logs/spring.log | grep -E "(IoT|设备|传感器)"

# 监控设备数据接收
grep "接收到设备数据" logs/spring.log
```

## 扩展开发

### 添加新的传感器类型

1. **修改实体类** (`IoTSensorData.java`)
2. **更新数据库表结构**
3. **修改数据解析逻辑** (`IoTSensorDataServiceImpl.java`)
4. **更新前端图表配置**

### 自定义数据处理

可以在 `IoTSensorDataServiceImpl.processDeviceMessage()` 方法中添加自定义的数据处理逻辑：

- 数据验证和清洗
- 异常值检测和过滤  
- 数据格式转换
- 第三方系统集成

## 故障排除

### 常见问题

1. **数据未显示在图表中**
   - 检查设备是否已注册
   - 确认数据格式是否正确
   - 查看后端日志是否有错误

2. **设备显示离线**
   - 检查数据发送频率
   - 调整离线检测阈值
   - 手动触发离线检查

3. **前端页面加载失败**
   - 确认后端服务已启动
   - 检查API接口连通性
   - 查看浏览器控制台错误

### 调试技巧

```bash
# 检查数据库连接
./mvnw spring-boot:run --debug

# 测试API接口
curl -X GET "http://localhost:8080/iot/devices/statistics"

# 查看实时日志
tail -f logs/spring.log
```

## 性能优化

### 数据库优化

- 为设备ID和时间字段添加索引
- 定期清理历史数据
- 使用数据库连接池

### 前端优化

- 图表数据懒加载
- 实时数据防抖处理
- 组件缓存和复用

## 安全考虑

### API安全

- JWT令牌认证
- 请求频率限制
- 输入数据验证

### 数据安全

- 敏感数据加密存储
- 访问权限控制
- 操作日志记录

---

## 技术支持

如有问题或建议，请查看：

- 📖 [API文档完整地址列表](./API文档完整地址列表.md)
- 🔧 [IoT设备接入配置说明](./IoT设备接入配置说明.md)
- 📝 [项目README](../README.md)

**联系方式：** 请通过项目Issues提交问题和建议 