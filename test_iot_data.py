#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
IoT设备数据模拟测试脚本
用于向SmartCloudPlatform发送模拟的IoT设备数据
"""

import requests
import json
import time
import random
from datetime import datetime

# 配置
BASE_URL = "http://localhost:8080/api"
DEVICE_LIST = [
    {"id": "device_001", "name": "空气质量监测器-001", "location": "办公室A区"},
    {"id": "device_002", "name": "空气质量监测器-002", "location": "办公室B区"},
    {"id": "device_003", "name": "空气质量监测器-003", "location": "会议室"},
    {"id": "sensor_001", "name": "环境传感器-001", "location": "实验室"},
]

def generate_sensor_data():
    """生成模拟传感器数据"""
    return {
        "status": {
            "co2 ppm": str(random.randint(400, 5000)),
            "tvoc ppb": str(random.randint(50, 1000)),
            "chip temperature": f"{random.uniform(20.0, 60.0):.1f}℃",
            "env temperature": f"{random.uniform(15.0, 35.0):.1f}℃",
            "env humidity": f"{random.uniform(30.0, 90.0):.1f}%"
        }
    }

def register_device(device_info):
    """注册设备"""
    url = f"{BASE_URL}/iot/devices/register"
    params = {
        "deviceId": device_info["id"],
        "deviceName": device_info["name"],
        "deviceLocation": device_info["location"]
    }
    
    try:
        response = requests.post(url, params=params)
        if response.status_code == 200:
            result = response.json()
            if result.get("success"):
                print(f"✅ 设备注册成功: {device_info['id']}")
            else:
                print(f"⚠️  设备注册响应: {device_info['id']} - {result.get('msg', '未知错误')}")
        else:
            print(f"❌ 设备注册失败: {device_info['id']} - HTTP {response.status_code}")
    except Exception as e:
        print(f"❌ 设备注册异常: {device_info['id']} - {str(e)}")

def send_device_data(device_id, data):
    """发送设备数据"""
    url = f"{BASE_URL}/iot/data/webhook/{device_id}"
    headers = {"Content-Type": "application/json"}
    
    try:
        response = requests.post(url, json=data, headers=headers)
        if response.status_code == 200:
            result = response.json()
            if result.get("success"):
                print(f"📡 数据发送成功: {device_id}")
            else:
                print(f"⚠️  数据发送响应: {device_id} - {result.get('msg', '未知错误')}")
        else:
            print(f"❌ 数据发送失败: {device_id} - HTTP {response.status_code}")
    except Exception as e:
        print(f"❌ 数据发送异常: {device_id} - {str(e)}")

def check_server_status():
    """检查服务器状态"""
    try:
        response = requests.get(f"{BASE_URL}/iot/devices/statistics", timeout=5)
        if response.status_code == 200:
            print("✅ 服务器连接正常")
            return True
        else:
            print(f"❌ 服务器响应异常: HTTP {response.status_code}")
            return False
    except Exception as e:
        print(f"❌ 服务器连接失败: {str(e)}")
        return False

def main():
    print("🚀 IoT设备数据模拟测试脚本启动")
    print(f"📡 目标服务器: {BASE_URL}")
    print("-" * 50)
    
    # 检查服务器状态
    if not check_server_status():
        print("请确保后端服务已启动并运行在 http://localhost:8080")
        return
    
    # 注册所有设备
    print("\n📋 开始注册设备...")
    for device in DEVICE_LIST:
        register_device(device)
    
    print(f"\n⏰ 开始发送模拟数据 (每10秒发送一次)")
    print("按 Ctrl+C 停止")
    
    try:
        while True:
            print(f"\n🕒 {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}")
            
            # 为每个设备发送数据
            for device in DEVICE_LIST:
                data = generate_sensor_data()
                send_device_data(device["id"], data)
                time.sleep(1)  # 设备间间隔1秒
            
            # 等待下一轮
            time.sleep(10)
            
    except KeyboardInterrupt:
        print("\n\n🛑 测试脚本已停止")
    except Exception as e:
        print(f"\n❌ 程序异常: {str(e)}")

if __name__ == "__main__":
    main() 