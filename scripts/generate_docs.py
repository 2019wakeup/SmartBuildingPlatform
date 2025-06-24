#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import os
import sys
import time
import subprocess
import requests
import json
import shutil
import zipfile
from pathlib import Path

def print_step(msg):
    print(f"\n=== {msg} ===")

def print_success(msg):
    print(f"✓ {msg}")

def print_error(msg):
    print(f"✗ {msg}")

def check_requirements():
    """检查必要的工具是否可用"""
    print_step("检查环境")
    
    # 检查Java
    try:
        subprocess.run(['java', '-version'], capture_output=True, check=True)
        print_success("Java 可用")
    except (subprocess.CalledProcessError, FileNotFoundError):
        print_error("未找到Java，请确保Java已安装并在PATH中")
        return False
    
    # 检查Maven Wrapper
    try:
        subprocess.run(['./mvnw', '-version'], capture_output=True, check=True)
        print_success("Maven Wrapper 可用")
    except (subprocess.CalledProcessError, FileNotFoundError):
        print_error("未找到Maven Wrapper，请确保mvnw文件存在且有执行权限")
        return False
    
    return True

def compile_project():
    """编译项目"""
    print_step("编译项目")
    try:
        result = subprocess.run(['./mvnw', 'clean', 'compile'], 
                              capture_output=True, text=True, check=True)
        print_success("项目编译成功")
        return True
    except subprocess.CalledProcessError as e:
        print_error(f"项目编译失败: {e.stderr}")
        return False

def start_application():
    """启动Spring Boot应用"""
    print_step("启动应用")
    try:
        # 启动应用
        process = subprocess.Popen(['./mvnw', 'spring-boot:run'], 
                                 stdout=subprocess.PIPE, 
                                 stderr=subprocess.PIPE)
        
        print(f"应用启动中，PID: {process.pid}")
        print("等待应用完全启动...")
        
        # 等待应用启动
        max_attempts = 60  # 最多等待60秒
        for i in range(max_attempts):
            try:
                response = requests.get('http://localhost:8080/v3/api-docs', timeout=5)
                if response.status_code == 200:
                    print_success("应用启动成功")
                    return process
            except requests.exceptions.RequestException:
                pass
            
            time.sleep(1)
            print(f"等待中... ({i+1}/{max_attempts})")
        
        print_error("应用启动超时")
        process.terminate()
        return None
        
    except Exception as e:
        print_error(f"启动应用失败: {str(e)}")
        return None

def generate_openapi_json():
    """生成OpenAPI JSON文档"""
    print_step("生成OpenAPI JSON文档")
    try:
        response = requests.get('http://localhost:8080/v3/api-docs', timeout=10)
        if response.status_code == 200:
            with open('openapi.json', 'w', encoding='utf-8') as f:
                json.dump(response.json(), f, ensure_ascii=False, indent=2)
            print_success("OpenAPI JSON文档生成成功: openapi.json")
            return True
        else:
            print_error(f"获取API文档失败，状态码: {response.status_code}")
            return False
    except Exception as e:
        print_error(f"生成OpenAPI JSON文档失败: {str(e)}")
        return False

def download_swagger_ui():
    """下载Swagger UI"""
    print_step("下载Swagger UI")
    try:
        # 创建docs目录
        docs_dir = Path('docs')
        docs_dir.mkdir(exist_ok=True)
        
        # 下载Swagger UI
        url = 'https://github.com/swagger-api/swagger-ui/archive/refs/tags/v5.9.0.zip'
        print("正在下载Swagger UI...")
        
        response = requests.get(url, stream=True)
        response.raise_for_status()
        
        with open('swagger-ui.zip', 'wb') as f:
            for chunk in response.iter_content(chunk_size=8192):
                f.write(chunk)
        
        # 解压文件
        with zipfile.ZipFile('swagger-ui.zip', 'r') as zip_ref:
            zip_ref.extractall('.')
        
        # 复制dist目录内容到docs
        src_dir = Path('swagger-ui-5.9.0/dist')
        for item in src_dir.iterdir():
            if item.is_file():
                shutil.copy2(item, docs_dir)
            elif item.is_dir():
                shutil.copytree(item, docs_dir / item.name, dirs_exist_ok=True)
        
        # 清理临时文件
        os.remove('swagger-ui.zip')
        shutil.rmtree('swagger-ui-5.9.0')
        
        print_success("Swagger UI下载完成")
        return True
        
    except Exception as e:
        print_error(f"下载Swagger UI失败: {str(e)}")
        return False

def customize_swagger_ui():
    """自定义Swagger UI"""
    print_step("自定义Swagger UI")
    try:
        index_file = Path('docs/index.html')
        if not index_file.exists():
            print_error("index.html文件不存在")
            return False
        
        # 读取index.html
        content = index_file.read_text(encoding='utf-8')
        
        # 替换默认的API地址
        content = content.replace(
            'https://petstore.swagger.io/v2/swagger.json',
            './openapi.json'
        )
        
        # 替换标题
        content = content.replace(
            'Swagger Petstore',
            '智能云平台管理系统API文档'
        )
        
        # 写回文件
        index_file.write_text(content, encoding='utf-8')
        
        # 复制openapi.json到docs目录
        shutil.copy2('openapi.json', 'docs/')
        
        print_success("Swagger UI自定义完成")
        return True
        
    except Exception as e:
        print_error(f"自定义Swagger UI失败: {str(e)}")
        return False

def main():
    """主函数"""
    print("=== 智能云平台API文档生成工具 ===")
    
    # 检查环境
    if not check_requirements():
        sys.exit(1)
    
    # 编译项目
    if not compile_project():
        sys.exit(1)
    
    # 启动应用
    app_process = start_application()
    if not app_process:
        sys.exit(1)
    
    try:
        # 生成OpenAPI JSON
        if not generate_openapi_json():
            sys.exit(1)
        
        # 下载Swagger UI
        if not download_swagger_ui():
            sys.exit(1)
        
        # 自定义Swagger UI
        if not customize_swagger_ui():
            sys.exit(1)
        
        print_step("文档生成完成")
        print("生成的文件:")
        print("  - openapi.json: OpenAPI规范文件")
        print("  - docs/index.html: HTML格式的API文档")
        print("")
        print("使用方法:")
        print("  1. 直接打开 docs/index.html 查看文档")
        print("  2. 或启动HTTP服务器:")
        print("     python3 -m http.server 3000")
        print("     然后访问: http://localhost:3000/docs/")
        
    finally:
        # 停止应用
        print_step("停止应用")
        app_process.terminate()
        try:
            app_process.wait(timeout=10)
            print_success("应用已停止")
        except subprocess.TimeoutExpired:
            app_process.kill()
            print_success("应用已强制停止")

if __name__ == '__main__':
    main() 