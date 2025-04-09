#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
数电发票API
获取授权 客户端签名验证示例
请求接口: https://api.fa-piao.com/v5/enterprise/authorization
"""

import time
import random
import string
import hmac
import hashlib
import json
import requests

# 配置信息
config = {
    'baseUrl': 'https://api.fa-piao.com',
    'appKey': 'YOUR_appKey',  # 替换为您的AppKey
    'appSecret': 'YOUR_SECRET_KEY',  # 替换为您的密钥
}

def generate_random_string(length=16):
    """生成随机字符串"""
    characters = string.ascii_letters + string.digits
    return ''.join(random.choice(characters) for _ in range(length))

def calculate_signature(method, path, random_string, timestamp, app_key, app_secret):
    """计算HMAC-SHA256签名"""
    # 构建签名字符串，与服务器端保持一致
    sign_content = f"Method={method}&Path={path}&RandomString={random_string}&TimeStamp={timestamp}&AppKey={app_key}"
    
    # 使用HMAC-SHA256计算签名，以secret作为密钥
    signature = hmac.new(
        app_secret.encode('utf-8'),
        sign_content.encode('utf-8'),
        hashlib.sha256
    ).hexdigest()
    
    # 转为大写
    return signature.upper()

def send_request(url, form_data, headers):
    """发送HTTP请求"""
    try:
        # 禁用SSL警告（生产环境应移除）
        from urllib3 import disable_warnings
        disable_warnings()
        
        # 创建multipart/form-data格式数据
        multipart_data = {k: (None, v.encode('utf-8')) for k, v in form_data.items()}
        
        response = requests.post(
            url=url,
            files=multipart_data,
            headers={k: v for k, v in headers.items() if k != 'Content-Type'},
            verify=False  # 生产环境应设为True并配置证书
        )
        
        return {
            'http_code': response.status_code,
            'response': response.text
        }
    except Exception as e:
        print(f'请求错误: {str(e)}')
        return {
            'http_code': 500,
            'response': str(e)
        }

def main():
    """主流程"""
    try:
        # 准备请求头参数
        method = 'POST'
        path = '/v5/enterprise/authorization'
        
        # 请求参数
        form_data = {
            'nsrsbh': '915101820724315989'
        }

        random_string = generate_random_string(20)
        timestamp = str(int(time.time()))  # 当前时间戳
        
        # 计算签名
        signature = calculate_signature(
            method,
            path,
            random_string,
            timestamp,
            config['appKey'],
            config['appSecret']
        )
        
        # 设置请求头
        headers = {
            'AppKey': config['appKey'],
            'Sign': signature,
            'TimeStamp': timestamp,
            'RandomString': random_string,
            'Content-Type': 'multipart/form-data'
        }
        
        # 发送请求
        result = send_request(config['baseUrl'] + path, form_data, headers)
        
        # 输出结果
        print(f"{method}: {config['baseUrl']}{path}")
        print(f"headers: {json.dumps(headers, ensure_ascii=False)}")
        print(f"form-data: {json.dumps(form_data, ensure_ascii=False)}")
        print(f"HTTP状态码: {result['http_code']}")
        print(f"响应内容: {result['response']}")
            
    except Exception as e:
        print(f"发生错误: {str(e)}")

if __name__ == "__main__":
    main()
