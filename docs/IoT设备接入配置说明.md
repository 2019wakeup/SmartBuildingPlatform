# 物联网设备接入配置说明

## 1. 转发方式选择

### 推荐方案：HTTP URL转发
- **优势**：更容易集成、支持身份验证、便于调试
- **配置简单**：只需要配置一个HTTP接口地址

### ThingsCloud平台配置步骤

1. **登录ThingsCloud平台**
2. **选择设备管理**
3. **找到您的设备**
4. **配置数据转发**
   - 选择：**HTTP URL转发**
   - 转发地址：`http://您的服务器IP:8080/api/iot/data/webhook/{设备ID}`
   - 请求方法：`POST`
   - 内容类型：`application/json`

## 2. 具体配置示例

### 设备ID为 "sensor_001" 的配置
```
转发URL: http://your-server-ip:8080/api/iot/data/webhook/sensor_001
方法: POST
Content-Type: application/json
```

### 如果需要身份验证
```
转发URL: http://your-server-ip:8080/api/iot/data/receive?deviceId=sensor_001
方法: POST  
Content-Type: application/json
Headers: 
  Authorization: Bearer your-jwt-token
```

## 3. 数据格式

### 您的设备数据格式
```json
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

### 系统支持的数据格式
- 系统会自动解析上述格式
- 自动提取数值部分（去除单位）
- 支持容错处理

## 4. 数据聚合策略

### 变化阈值设置
- **CO2变化阈值**: 100 ppm
- **TVOC变化阈值**: 50 ppb  
- **温度变化阈值**: 2.0℃
- **湿度变化阈值**: 5.0%

### 存储规则
1. 首次数据：直接存储
2. 后续数据：只有超过阈值才存储
3. 强制存储：超过30分钟未存储则强制存储一次

## 5. 数据库表结构

### 传感器数据表 (iot_sensor_data)
- 存储历史传感器数据
- 支持按设备ID和时间范围查询
- 自动清理30天前的数据

### 设备状态表 (iot_device_status)  
- 存储设备当前状态
- 实时更新设备在线/离线状态
- 记录设备最新数据

## 6. API接口

### 数据接收接口
- `POST /api/iot/data/webhook/{deviceId}` - 通用数据接收
- `POST /api/iot/data/receive?deviceId=xxx` - 带参数接收

### 数据查询接口
- `GET /api/iot/data/latest/{deviceId}` - 获取最新数据
- `GET /api/iot/data/history/{deviceId}` - 获取历史数据
- `GET /api/iot/devices/status` - 获取所有设备状态

### 设备管理接口
- `POST /api/iot/devices/register` - 设备注册
- `GET /api/iot/devices/online` - 在线设备列表
- `GET /api/iot/devices/statistics` - 设备统计信息

## 7. 部署步骤

### 1. 数据库初始化
```bash
# 在MySQL中执行
mysql -u root -p cloudplatform < sql_iot_tables.sql
```

### 2. 启动应用
```bash
# 使用mvnw启动
./mvnw spring-boot:run
```

### 3. 验证接口
```bash
# 测试数据接收接口
curl -X POST http://localhost:8080/api/iot/data/webhook/test_device \
  -H "Content-Type: application/json" \
  -d '{
    "status": {
      "co2 ppm": "4469",
      "tvoc ppb": "331",
      "chip temperature": "51.9℃", 
      "env temperature": "29.0℃",
      "env humidity": "77.6%"
    }
  }'
```

## 8. 监控和维护

### 自动化任务
- **离线检查**: 每10分钟检查一次离线设备
- **数据清理**: 每天凌晨2点清理30天前的数据
- **统计记录**: 每小时记录设备统计信息

### 日志监控
- 查看应用日志: `tail -f logs/spring.log`
- 关注ERROR级别的日志
- 监控设备数据接收情况

## 9. 故障排除

### 常见问题
1. **数据未接收到**
   - 检查网络连接
   - 验证URL配置
   - 查看应用日志

2. **数据解析失败**
   - 检查JSON格式
   - 验证字段名称
   - 查看解析日志

3. **设备显示离线**
   - 检查数据发送频率
   - 调整离线阈值
   - 手动触发离线检查

### 调试命令
```bash
# 查看最新日志
tail -f logs/spring.log

# 检查数据库连接
./mvnw spring-boot:run --debug

# 测试接口连通性
curl -X GET http://localhost:8080/api/iot/devices/statistics
``` 