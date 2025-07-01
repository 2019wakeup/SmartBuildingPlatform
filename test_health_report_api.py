#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
健康报告API测试脚本
"""

import requests
import json
from datetime import datetime

# API基础URL
BASE_URL = "http://localhost:8080/api"
HEALTH_REPORT_API = f"{BASE_URL}/health-report"

# 全局变量存储JWT令牌
JWT_TOKEN = None

def login():
    """登录获取JWT令牌"""
    global JWT_TOKEN
    print("=== 登录获取JWT令牌 ===")
    
    login_data = {
        "account": "admin",
        "password": "123456"
    }
    
    try:
        response = requests.post(
            f"{BASE_URL}/auth/login",
            json=login_data,
            headers={"Content-Type": "application/json"}
        )
        
        if response.status_code == 200:
            result = response.json()
            if result.get("code") == 200:
                JWT_TOKEN = result.get("data", {}).get("token")
                print(f"✅ 登录成功，获取到JWT令牌")
                return True
            else:
                print(f"❌ 登录失败: {result.get('msg')}")
                return False
        else:
            print(f"❌ 登录HTTP请求失败: {response.status_code}")
            return False
    except Exception as e:
        print(f"❌ 登录请求异常: {e}")
        return False

def get_auth_headers():
    """获取认证头"""
    if JWT_TOKEN:
        return {"Authorization": f"Bearer {JWT_TOKEN}", "Content-Type": "application/json"}
    else:
        return {"Content-Type": "application/json"}

def test_submit_health_report():
    """测试提交健康报告数据"""
    print("=== 测试提交健康报告数据 ===")
    
    # 构造测试数据
    test_data = {
        "fingerPrint": "test_fingerprint_hash_001",
        "dataTaken": "2025-06-27 08:42:20",
        "reportData": json.dumps([
            {
                "timestamp": 1.1,
                "temperature_c": 34.26,
                "pressure_hpa": 1002.47,
                "humidity_percent": 55.0,
                "gas_resistance_ohm": None
            },
            {
                "timestamp": 2.1,
                "temperature_c": 34.28,
                "pressure_hpa": 1002.45,
                "humidity_percent": 54.88,
                "gas_resistance_ohm": 5684.85
            },
            {
                "timestamp": 3.1,
                "temperature_c": 34.35,
                "pressure_hpa": 1002.48,
                "humidity_percent": 54.69,
                "gas_resistance_ohm": 5684.85
            },
            {
                "timestamp": 4.1,
                "temperature_c": 34.42,
                "pressure_hpa": 1002.44,
                "humidity_percent": 54.42,
                "gas_resistance_ohm": 5684.85
            },
            {
                "timestamp": 5.1,
                "temperature_c": 34.49,
                "pressure_hpa": 1002.47,
                "humidity_percent": 54.2,
                "gas_resistance_ohm": 5684.85
            }
        ]),
        "stuID": 0
    }
    
    try:
        response = requests.post(
            f"{HEALTH_REPORT_API}/submit",
            json=test_data,
            headers=get_auth_headers()
        )
        
        print(f"状态码: {response.status_code}")
        print(f"响应: {response.text}")
        
        if response.status_code == 200:
            result = response.json()
            if result.get("code") == 200:
                print("✅ 健康报告提交成功")
            else:
                print(f"❌ 健康报告提交失败: {result.get('msg')}")
        else:
            print(f"❌ HTTP请求失败: {response.status_code}")
            
    except Exception as e:
        print(f"❌ 请求异常: {e}")

def test_get_health_reports_by_student():
    """测试根据学生ID查询健康报告"""
    print("\n=== 测试根据学生ID查询健康报告 ===")
    
    stu_id = 0
    
    try:
        response = requests.get(f"{HEALTH_REPORT_API}/student/{stu_id}", headers=get_auth_headers())
        
        print(f"状态码: {response.status_code}")
        print(f"响应: {response.text}")
        
        if response.status_code == 200:
            result = response.json()
            if result.get("code") == 200:
                reports = result.get("rows", [])
                print(f"✅ 查询成功，共找到 {len(reports)} 条健康报告")
                for report in reports:
                    print(f"  - 报告ID: {report.get('id')}, 采集时间: {report.get('data_taken')}")
            else:
                print(f"❌ 查询失败: {result.get('msg')}")
        else:
            print(f"❌ HTTP请求失败: {response.status_code}")
            
    except Exception as e:
        print(f"❌ 请求异常: {e}")

def test_get_latest_health_report():
    """测试获取最新健康报告"""
    print("\n=== 测试获取最新健康报告 ===")
    
    stu_id = 0
    
    try:
        response = requests.get(f"{HEALTH_REPORT_API}/student/{stu_id}/latest", headers=get_auth_headers())
        
        print(f"状态码: {response.status_code}")
        print(f"响应: {response.text}")
        
        if response.status_code == 200:
            result = response.json()
            if result.get("code") == 200:
                report = result.get("data")
                if report:
                    print(f"✅ 获取最新报告成功")
                    print(f"  - 报告ID: {report.get('id')}")
                    print(f"  - 采集时间: {report.get('data_taken')}")
                    print(f"  - 数据点数量: {len(json.loads(report.get('report_data', '[]')))}")
                else:
                    print("❌ 未找到健康报告数据")
            else:
                print(f"❌ 获取失败: {result.get('msg')}")
        else:
            print(f"❌ HTTP请求失败: {response.status_code}")
            
    except Exception as e:
        print(f"❌ 请求异常: {e}")

def test_get_health_report_list():
    """测试查询健康报告列表"""
    print("\n=== 测试查询健康报告列表 ===")
    
    try:
        response = requests.get(f"{HEALTH_REPORT_API}/list", headers=get_auth_headers())
        
        print(f"状态码: {response.status_code}")
        print(f"响应: {response.text}")
        
        if response.status_code == 200:
            result = response.json()
            if result.get("code") == 200:
                reports = result.get("rows", [])
                print(f"✅ 查询成功，共找到 {len(reports)} 条健康报告")
                for report in reports:
                    print(f"  - 报告ID: {report.get('id')}, 学生ID: {report.get('stu_id')}, 采集时间: {report.get('data_taken')}")
            else:
                print(f"❌ 查询失败: {result.get('msg')}")
        else:
            print(f"❌ HTTP请求失败: {response.status_code}")
            
    except Exception as e:
        print(f"❌ 请求异常: {e}")

def main():
    """主函数"""
    print("健康报告API测试开始")
    print(f"API地址: {HEALTH_REPORT_API}")
    print(f"测试时间: {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}")
    print("=" * 50)
    
    # 先登录获取令牌
    if not login():
        print("无法获取JWT令牌，测试终止")
        return
    
    # 执行测试
    test_submit_health_report()
    test_get_health_reports_by_student()
    test_get_latest_health_report()
    test_get_health_report_list()
    
    print("\n" + "=" * 50)
    print("健康报告API测试完成")

if __name__ == "__main__":
    main() 