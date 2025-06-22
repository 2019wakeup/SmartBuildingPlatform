#!/bin/bash

echo "=== 智能云平台API文档生成脚本 ==="
echo ""

# 检查Java和Maven Wrapper是否可用
if ! command -v java &> /dev/null; then
    echo "错误: 未找到Java，请确保Java已安装并在PATH中"
    exit 1
fi

if [ ! -f "./mvnw" ]; then
    echo "错误: 未找到Maven Wrapper，请确保mvnw文件存在"
    exit 1
fi

echo "1. 清理并编译项目..."
./mvnw clean compile

echo ""
echo "2. 启动Spring Boot应用..."
# 在后台启动应用
./mvnw spring-boot:run &
APP_PID=$!

echo "应用启动中，PID: $APP_PID"
echo "等待应用完全启动..."

# 等待应用启动
sleep 30

# 检查应用是否启动成功
if curl -s http://localhost:8080/v3/api-docs > /dev/null; then
    echo "✓ 应用启动成功"
else
    echo "✗ 应用启动失败，请检查日志"
    kill $APP_PID 2>/dev/null
    exit 1
fi

echo ""
echo "3. 生成OpenAPI JSON文档..."
curl -s http://localhost:8080/v3/api-docs > openapi.json
if [ -s openapi.json ]; then
    echo "✓ OpenAPI JSON文档生成成功: openapi.json"
else
    echo "✗ OpenAPI JSON文档生成失败"
fi

echo ""
echo "4. 生成HTML文档..."
# 创建docs目录
mkdir -p docs

# 下载swagger-ui-dist
echo "下载Swagger UI..."
curl -s -L https://github.com/swagger-api/swagger-ui/archive/refs/tags/v5.9.0.tar.gz | tar -xz
cp -r swagger-ui-5.9.0/dist/* docs/
rm -rf swagger-ui-5.9.0

# 修改index.html以使用我们的openapi.json
sed -i '' 's|https://petstore.swagger.io/v2/swagger.json|../openapi.json|g' docs/index.html
sed -i '' 's|Swagger Petstore|智能云平台管理系统API文档|g' docs/index.html

cp openapi.json docs/

echo "✓ HTML文档生成成功: docs/index.html"

echo ""
echo "5. 停止应用..."
kill $APP_PID 2>/dev/null
sleep 5

echo ""
echo "=== 文档生成完成 ==="
echo "生成的文件:"
echo "  - openapi.json: OpenAPI规范文件"
echo "  - docs/index.html: HTML格式的API文档"
echo ""
echo "使用方法:"
echo "  1. 直接打开 docs/index.html 查看文档"
echo "  2. 或启动简单HTTP服务器: python3 -m http.server 3000"
echo "     然后访问: http://localhost:3000/docs/"
echo "" 