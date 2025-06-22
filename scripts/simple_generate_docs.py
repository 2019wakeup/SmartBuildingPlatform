#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import subprocess
import requests
import json
import time
import sys
import threading

def print_output(process):
    """打印进程输出"""
    while True:
        output = process.stdout.readline()
        if output == '' and process.poll() is not None:
            break
        if output:
            print(output.strip().decode('utf-8'))

def main():
    print("=== 智能云平台API文档生成工具（简化版）===")
    
    # 1. 编译项目
    print("\n1. 编译项目...")
    try:
        subprocess.run(['./mvnw', 'clean', 'compile'], check=True)
        print("✓ 项目编译成功")
    except subprocess.CalledProcessError:
        print("✗ 项目编译失败")
        sys.exit(1)
    
    # 2. 启动应用
    print("\n2. 启动应用...")
    print("启动日志:")
    print("-" * 50)
    
    process = subprocess.Popen(['./mvnw', 'spring-boot:run'], 
                             stdout=subprocess.PIPE, 
                             stderr=subprocess.STDOUT,
                             universal_newlines=True)
    
    print(f"应用启动中，PID: {process.pid}")
    
    # 启动线程来打印输出
    output_thread = threading.Thread(target=print_output, args=(process,))
    output_thread.daemon = True
    output_thread.start()
    
    # 等待应用启动
    print("\n等待应用启动...")
    for i in range(120):  # 增加到120秒
        try:
            response = requests.get('http://localhost:8080/api/v3/api-docs', timeout=5)
            if response.status_code == 200:
                print("\n✓ 应用启动成功")
                break
        except requests.exceptions.RequestException:
            pass
        
        time.sleep(1)
        if i % 15 == 0 and i > 0:
            print(f"\n等待中... ({i+1}/120)")
    else:
        print("\n✗ 应用启动超时")
        process.terminate()
        sys.exit(1)
    
    try:
        # 3. 生成OpenAPI JSON文档
        print("\n" + "=" * 50)
        print("3. 生成OpenAPI JSON文档...")
        
        # API文档端点
        api_urls = [
            'http://localhost:8080/api/v3/api-docs'
        ]
        
        api_data = None
        for url in api_urls:
            try:
                response = requests.get(url, timeout=10)
                if response.status_code == 200:
                    api_data = response.json()
                    print(f"✓ 从 {url} 获取API文档成功")
                    break
            except Exception as e:
                print(f"尝试 {url} 失败: {str(e)}")
        
        if api_data:
            # 保存JSON文档
            with open('api-docs.json', 'w', encoding='utf-8') as f:
                json.dump(api_data, f, ensure_ascii=False, indent=2)
            print("✓ OpenAPI JSON文档生成成功: api-docs.json")
            
            # 生成简单的HTML文档
            html_content = f"""
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>智能云平台管理系统API文档</title>
    <style>
        body {{ font-family: Arial, sans-serif; margin: 40px; background-color: #f5f5f5; }}
        .container {{ max-width: 1200px; margin: 0 auto; background: white; padding: 30px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }}
        .header {{ background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 30px; border-radius: 8px; margin-bottom: 30px; }}
        .header h1 {{ margin: 0; font-size: 2.5em; }}
        .header p {{ margin: 10px 0 0 0; opacity: 0.9; }}
        .api-info {{ margin: 30px 0; }}
        .endpoint {{ margin: 20px 0; padding: 20px; border: 1px solid #e1e5e9; border-radius: 8px; background: #fafbfc; }}
        .method {{ display: inline-block; padding: 6px 12px; color: white; border-radius: 4px; font-weight: bold; font-size: 0.85em; }}
        .get {{ background-color: #61affe; }}
        .post {{ background-color: #49cc90; }}
        .put {{ background-color: #fca130; }}
        .delete {{ background-color: #f93e3e; }}
        .patch {{ background-color: #50e3c2; }}
        .path {{ font-family: 'Courier New', monospace; font-size: 1.1em; margin-left: 10px; }}
        .description {{ margin: 10px 0; color: #666; }}
        .tags {{ margin: 10px 0; }}
        .tag {{ display: inline-block; background: #e1f5fe; color: #0277bd; padding: 4px 8px; border-radius: 4px; font-size: 0.8em; margin-right: 5px; }}
        pre {{ background: #f8f9fa; padding: 15px; border-radius: 4px; overflow-x: auto; border-left: 4px solid #007bff; }}
        .stats {{ display: flex; justify-content: space-around; margin: 20px 0; }}
        .stat {{ text-align: center; }}
        .stat-number {{ font-size: 2em; font-weight: bold; color: #007bff; }}
        .stat-label {{ color: #666; }}
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>智能云平台管理系统API文档</h1>
            <p>基于SpringBoot3 + MyBatis Plus的前后端分离管理系统</p>
            <p>版本: 1.0.0 | 生成时间: {time.strftime('%Y-%m-%d %H:%M:%S')}</p>
        </div>
        
        <div class="api-info">
            <h2>📋 文档信息</h2>
            <p><strong>完整的OpenAPI规范文档:</strong> <a href="api-docs.json" target="_blank">api-docs.json</a></p>
            <p><strong>在线Swagger UI:</strong> 启动应用后访问 <a href="http://localhost:8080/api/swagger-ui/index.html" target="_blank">http://localhost:8080/api/swagger-ui/index.html</a></p>
        </div>
        
        <div class="stats" id="stats">
            <div class="stat">
                <div class="stat-number" id="total-endpoints">0</div>
                <div class="stat-label">总接口数</div>
            </div>
            <div class="stat">
                <div class="stat-number" id="get-count">0</div>
                <div class="stat-label">GET</div>
            </div>
            <div class="stat">
                <div class="stat-number" id="post-count">0</div>
                <div class="stat-label">POST</div>
            </div>
            <div class="stat">
                <div class="stat-number" id="other-count">0</div>
                <div class="stat-label">其他</div>
            </div>
        </div>
        
        <h2>🚀 API接口列表</h2>
        <div id="endpoints"></div>
    </div>
    
    <script>
        // 加载API文档数据
        fetch('api-docs.json')
            .then(response => response.json())
            .then(data => {{
                const endpointsDiv = document.getElementById('endpoints');
                const paths = data.paths || {{}};
                
                let totalEndpoints = 0;
                let getCounts = 0;
                let postCounts = 0;
                let otherCounts = 0;
                
                Object.keys(paths).sort().forEach(path => {{
                    const methods = paths[path];
                    Object.keys(methods).forEach(method => {{
                        totalEndpoints++;
                        if (method.toLowerCase() === 'get') getCounts++;
                        else if (method.toLowerCase() === 'post') postCounts++;
                        else otherCounts++;
                        
                        const operation = methods[method];
                        const div = document.createElement('div');
                        div.className = 'endpoint';
                        div.innerHTML = `
                            <h3>
                                <span class="method ${{method.toLowerCase()}}">${{method.toUpperCase()}}</span>
                                <span class="path">${{path}}</span>
                            </h3>
                            <div class="description">${{operation.summary || operation.description || '无描述'}}</div>
                            ${{operation.tags ? `<div class="tags">${{operation.tags.map(tag => `<span class="tag">${{tag}}</span>`).join('')}}</div>` : ''}}
                        `;
                        endpointsDiv.appendChild(div);
                    }});
                }});
                
                // 更新统计信息
                document.getElementById('total-endpoints').textContent = totalEndpoints;
                document.getElementById('get-count').textContent = getCounts;
                document.getElementById('post-count').textContent = postCounts;
                document.getElementById('other-count').textContent = otherCounts;
            }})
            .catch(error => {{
                console.error('加载API文档失败:', error);
                document.getElementById('endpoints').innerHTML = '<p style="color: red;">加载API文档失败，请检查api-docs.json文件是否存在。</p>';
            }});
    </script>
</body>
</html>
"""
            
            with open('api-docs.html', 'w', encoding='utf-8') as f:
                f.write(html_content)
            print("✓ HTML文档生成成功: api-docs.html")
            
        else:
            print("✗ 获取API文档失败")
    
    finally:
        # 4. 停止应用
        print("\n" + "=" * 50)
        print("4. 停止应用...")
        process.terminate()
        try:
            process.wait(timeout=10)
            print("✓ 应用已停止")
        except subprocess.TimeoutExpired:
            process.kill()
            print("✓ 应用已强制停止")
    
    print("\n=== 文档生成完成 ===")
    print("生成的文件:")
    print("  - api-docs.json: OpenAPI规范文件")
    print("  - api-docs.html: 美化的HTML文档")
    print("\n使用方法:")
    print("  1. 直接打开 api-docs.html 查看文档")
    print("  2. 将 api-docs.json 导入到其他API文档工具中")
    print("  3. 启动HTTP服务器: python3 -m http.server 3000")
    print("     然后访问: http://localhost:3000/api-docs.html")

if __name__ == '__main__':
    main() 