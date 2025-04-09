<?php
/**
 * 数电发票API
 * 获取授权 客户端签名验证示例
 * 请求接口: https://api.fa-piao.com/v5/enterprise/authorization
 */

// 配置信息
$config = [
    'baseUrl' => 'https://api.fa-piao.com',
    'appKey' => 'YOUR_appKey', // 替换为您的AppKey
    'appSecret' => 'YOUR_SECRET_KEY', // 替换为您的密钥
];




// 生成随机字符串
function generateRandomString($length = 16) {
    $characters = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
    $randomString = '';
    for ($i = 0; $i < $length; $i++) {
        $randomString .= $characters[rand(0, strlen($characters) - 1)];
    }
    return $randomString;
}

// 计算签名
function calculateSignature($method, $path, $randomString, $timestamp, $appKey, $appSecret) {
    // 构建签名字符串，与服务器端保持一致
    $signContent = sprintf(
        "Method=%s&Path=%s&RandomString=%s&TimeStamp=%s&AppKey=%s",
        $method, $path, $randomString, $timestamp, $appKey
    );
    
    // 使用HMAC-SHA256计算签名，以secret作为密钥
    $signature = hash_hmac('sha256', $signContent, $appSecret);
    
    // 转为大写
    return strtoupper($signature);
}

// 发送请求
function sendRequest($url, $formData, $headers) {
    $ch = curl_init();
    
    curl_setopt($ch, CURLOPT_URL, $url);
    curl_setopt($ch, CURLOPT_POST, true);
    curl_setopt($ch, CURLOPT_POSTFIELDS, $formData);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
    curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false); // 开发环境可设置为false，生产环境建议设置为true
    
    $response = curl_exec($ch);
    $httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
    
    if (curl_errno($ch)) {
        echo '请求错误: ' . curl_error($ch);
    }
    
    curl_close($ch);
    
    return [
        'http_code' => $httpCode,
        'response' => $response
    ];
}

// 主流程
try {
    // 准备请求头参数
    $method = 'POST';
    $path = '/v5/enterprise/authorization';
    // 请求参数
    $formData = [
        'nsrsbh' => '915101820724315989'
    ];

    $randomString = generateRandomString(20);
    $timestamp = (string)time(); // 当前时间戳
    // 计算签名
    $signature = calculateSignature(
        $method, 
        $path, 
        $randomString, 
        $timestamp, 
        $config['appKey'], 
        $config['appSecret']
    );
    
    // 设置请求头
    $headers = [
        'AppKey: ' . $config['appKey'],
        'Sign: ' . $signature,
        'TimeStamp: ' . $timestamp,
        'RandomString: ' . $randomString,
        'Content-Type: multipart/form-data'
    ];
    
    // 发送请求
    $result = sendRequest($config['baseUrl'].$path, $formData, $headers);
    
    // 输出结果
    echo $method.": " . $config['baseUrl'].$path . "\n";
    echo "headers: " . json_encode($headers,JSON_UNESCAPED_UNICODE) . "\n";
    echo "form-data: " . json_encode($formData,JSON_UNESCAPED_UNICODE) . "\n";
    echo "HTTP状态码: " . $result['http_code'] . "\n";
    echo "响应内容: " . $result['response'] . "\n";

    
} catch (Exception $e) {
    echo "发生错误: " . $e->getMessage();
}
