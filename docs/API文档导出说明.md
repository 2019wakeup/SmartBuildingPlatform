# 智能云平台API文档静态导出工具

本工具用于将您的Spring Boot项目的Swagger API文档导出为静态文件，无需修改任何Java或Spring配置。

## 前提条件

- Java 17+
- Maven 3.6+
- Python 3.6+（用于运行生成脚本）

## 使用方法

### 方法1：简化版本（推荐）

运行简化版本的Python脚本：

```bash
python3 simple_generate_docs.py
```

这个脚本会：
1. 编译您的项目
2. 启动Spring Boot应用
3. 获取OpenAPI JSON文档
4. 生成简单的HTML文档
5. 自动停止应用

生成的文件：
- `api-docs.json`: OpenAPI 3.0规范文件
- `api-docs.html`: 简单的HTML格式文档

### 方法2：完整版本（包含Swagger UI）

如果您想要完整的Swagger UI界面，运行：

```bash
python3 generate_docs.py
```

这个脚本会：
1. 编译项目
2. 启动应用
3. 获取OpenAPI JSON文档
4. 下载Swagger UI
5. 生成完整的HTML文档
6. 自动停止应用

生成的文件：
- `openapi.json`: OpenAPI 3.0规范文件
- `docs/index.html`: 完整的Swagger UI文档

### 方法3：Shell脚本（Mac/Linux）

```bash
chmod +x generate-docs.sh
./generate-docs.sh
```

## 查看文档

### 直接打开HTML文件

- 简化版本：双击打开 `api-docs.html`
- 完整版本：双击打开 `docs/index.html`

### 使用HTTP服务器

```bash
# 启动简单HTTP服务器
python3 -m http.server 3001

# 然后在浏览器中访问：
# 简化版本: http://localhost:3001/api-docs.html
# 完整版本: http://localhost:3001/docs/
```

## 文档格式说明

### OpenAPI JSON文件
- 符合OpenAPI 3.0规范
- 可以导入到其他API文档工具中
- 包含所有接口的详细信息

### HTML文档
- 可以离线查看
- 包含所有API接口信息
- 支持交互式测试（完整版本）

## 故障排除

### 常见问题

1. **应用启动失败**
   - 检查8080端口是否被占用
   - 确保数据库连接正常
   - 查看控制台错误信息

2. **文档生成失败**
   - 确保应用完全启动
   - 检查网络连接
   - 验证 `/v3/api-docs` 端点是否可访问

3. **Python依赖问题**
   ```bash
   pip3 install requests
   ```

### 手动生成（备用方法）

如果脚本运行失败，您也可以手动生成：

1. 启动应用：
   ```bash
   mvn spring-boot:run
   ```

2. 在另一个终端中获取JSON文档：
   ```bash
   curl http://localhost:8080/api/v3/api-docs > api-docs.json
   ```

3. 停止应用：`Ctrl+C`

## 自定义配置

您可以修改脚本中的以下配置：

- 应用端口（默认8080）
- 文档输出路径
- Swagger UI版本
- 等待超时时间

## 注意事项

- 生成过程中会临时启动您的应用
- 确保数据库等依赖服务正常运行
- 生成的静态文档不包含实时数据
- 如果API有变更，需要重新生成文档

## 支持的输出格式

- JSON (OpenAPI 3.0)
- HTML (带Swagger UI)
- 可以进一步转换为PDF、Word等格式

## 集成到CI/CD

您可以将文档生成集成到构建流程中：

```yaml
# GitHub Actions 示例
- name: Generate API Docs
  run: python3 simple_generate_docs.py
  
- name: Upload Docs
  uses: actions/upload-artifact@v2
  with:
    name: api-docs
    path: |
      api-docs.json
      api-docs.html
``` 