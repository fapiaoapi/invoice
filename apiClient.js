/**
 * 数电发票API
 * 获取授权 客户端签名验证示例
 * 请求接口: https://api.fa-piao.com/v5/enterprise/authorization
 */

const crypto = require('crypto');
const https = require('https');
const querystring = require('querystring');
const FormData = require('form-data');

// 配置信息
const config = {
    baseUrl: 'https://api.fa-piao.com',
    appKey: 'YOUR_APP_KEY', // 替换为您的AppKey
    appSecret: 'YOUR_SECRET_KEY', // 替换为您的密钥
};

/**
 * 生成随机字符串
 * @param {number} length 字符串长度
 * @returns {string} 随机字符串
 */
function generateRandomString(length = 16) {
    const characters = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
    let randomString = '';
    for (let i = 0; i < length; i++) {
        randomString += characters.charAt(Math.floor(Math.random() * characters.length));
    }
    return randomString;
}

/**
 * 计算签名
 * @param {string} method HTTP方法
 * @param {string} path 请求路径
 * @param {string} randomString 随机字符串
 * @param {string} timestamp 时间戳
 * @param {string} appKey 应用密钥
 * @param {string} appSecret 应用密钥
 * @returns {string} 签名
 */
function calculateSignature(method, path, randomString, timestamp, appKey, appSecret) {
    // 构建签名字符串，与服务器端保持一致
    const signContent = `Method=${method}&Path=${path}&RandomString=${randomString}&TimeStamp=${timestamp}&AppKey=${appKey}`;
    
    // 使用HMAC-SHA256计算签名，以secret作为密钥
    const hmac = crypto.createHmac('sha256', appSecret);
    hmac.update(signContent);
    const signature = hmac.digest('hex');
    
    // 转为大写
    return signature.toUpperCase();
}

/**
 * 发送请求
 * @param {string} url 请求URL
 * @param {object} formData 表单数据
 * @param {object} headers 请求头
 * @returns {Promise<object>} 响应结果
 */
function sendRequest(url, formData, headers) {
    return new Promise((resolve, reject) => {
        // 创建form-data
        const form = new FormData();
        for (const key in formData) {
            form.append(key, formData[key]);
        }
        
        // 获取form-data的headers
        const formHeaders = form.getHeaders();
        
        // 解析URL
        const urlObj = new URL(url);
        
        // 请求选项
        const options = {
            hostname: urlObj.hostname,
            port: urlObj.port || 443,
            path: urlObj.pathname,
            method: 'POST',
            headers: {
                ...formHeaders,
                ...headers
            },
            rejectUnauthorized: false // 开发环境可设置为false，生产环境建议设置为true
        };
        
        // 删除Content-Type，使用form-data自动生成的
        delete options.headers['Content-Type'];
        
        const req = https.request(options, (res) => {
            let data = '';
            
            res.on('data', (chunk) => {
                data += chunk;
            });
            
            res.on('end', () => {
                resolve({
                    http_code: res.statusCode,
                    response: data
                });
            });
        });
        
        req.on('error', (error) => {
            console.error('请求错误:', error);
            reject(error);
        });
        
        // 发送form-data
        form.pipe(req);
    });
}

/**
 * 主函数
 */
async function main() {
    try {
        // 准备请求头参数
        const method = 'POST';
        const path = '/v5/enterprise/authorization';
        
        // 请求参数
        const formData = {
            nsrsbh: '915101820724315989'
        };

        const randomString = generateRandomString(20);
        const timestamp = Math.floor(Date.now() / 1000).toString(); // 当前时间戳
        
        // 计算签名
        const signature = calculateSignature(
            method, 
            path, 
            randomString, 
            timestamp, 
            config.appKey, 
            config.appSecret
        );
        
        // 设置请求头
        const headers = {
            'AppKey': config.appKey,
            'Sign': signature,
            'TimeStamp': timestamp,
            'RandomString': randomString
        };
        
        // 发送请求
        const result = await sendRequest(config.baseUrl + path, formData, headers);
        
        // 输出结果
        console.log(`${method}: ${config.baseUrl}${path}`);
        console.log(`headers: ${JSON.stringify(headers)}`);
        console.log(`form-data: ${JSON.stringify(formData)}`);
        console.log(`HTTP状态码: ${result.http_code}`);
        console.log(`响应内容: ${result.response}`);
        
    } catch (error) {
        console.error(`发生错误: ${error.message}`);
    }
}

// 执行主函数
main();
