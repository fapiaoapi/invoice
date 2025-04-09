using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Security.Cryptography;
using System.Text;
using System.Threading.Tasks;
using System.Text.Json;

namespace FaPiaoApiClient
{
    class Program
    {
        // 配置信息
        private static readonly string BaseUrl = "https://api.fa-piao.com";
        private static readonly string AppKey = "YOUR_APP_KEY"; // 替换为您的AppKey
        private static readonly string AppSecret = "YOUR_SECRET_KEY"; // 替换为您的密钥

        static async Task Main(string[] args)
        {
            try
            {
                // 准备请求头参数
                string method = "POST";
                string path = "/v5/enterprise/authorization";
                
                // 请求参数
                var formData = new Dictionary<string, string>
                {
                    { "nsrsbh", "915101820724315989" }
                };

                string randomString = GenerateRandomString(20);
                string timestamp = DateTimeOffset.UtcNow.ToUnixTimeSeconds().ToString();
                
                // 计算签名
                string signature = CalculateSignature(method, path, randomString, timestamp);
                
                // 发送请求
                var result = await SendRequest(BaseUrl + path, formData, method, randomString, timestamp, signature);
                
                // 输出结果
                Console.WriteLine($"{method}: {BaseUrl}{path}");
                Console.WriteLine($"headers: {GetHeadersJson(randomString, timestamp, signature)}");
                Console.WriteLine($"form-data: {JsonSerializer.Serialize(formData)}");
                Console.WriteLine($"HTTP状态码: {result.StatusCode}");
                Console.WriteLine($"响应内容: {result.Content}");
                
                // 尝试解析JSON响应
                try
                {
                    var responseObj = JsonSerializer.Deserialize<object>(result.Content);
                    Console.WriteLine("解析后的响应:");
                    Console.WriteLine(JsonSerializer.Serialize(responseObj, new JsonSerializerOptions { WriteIndented = true }));
                }
                catch (JsonException)
                {
                    // 响应不是有效的JSON格式
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine($"发生错误: {ex.Message}");
            }
        }

        /// <summary>
        /// 生成随机字符串
        /// </summary>
        private static string GenerateRandomString(int length)
        {
            const string chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
            var random = new Random();
            var result = new StringBuilder(length);
            
            for (int i = 0; i < length; i++)
            {
                result.Append(chars[random.Next(chars.Length)]);
            }
            
            return result.ToString();
        }

        /// <summary>
        /// 计算HMAC-SHA256签名
        /// </summary>
        private static string CalculateSignature(string method, string path, string randomString, string timestamp)
        {
            // 构建签名字符串，与服务器端保持一致
            string signContent = $"Method={method}&Path={path}&RandomString={randomString}&TimeStamp={timestamp}&AppKey={AppKey}";
            
            // 使用HMAC-SHA256计算签名，以secret作为密钥
            using (var hmac = new HMACSHA256(Encoding.UTF8.GetBytes(AppSecret)))
            {
                byte[] hashBytes = hmac.ComputeHash(Encoding.UTF8.GetBytes(signContent));
                return BitConverter.ToString(hashBytes).Replace("-", "").ToUpper();
            }
        }

        /// <summary>
        /// 发送HTTP请求
        /// </summary>
        private static async Task<(int StatusCode, string Content)> SendRequest(
            string url, Dictionary<string, string> formData, string method, 
            string randomString, string timestamp, string signature)
        {
            using (var httpClient = new HttpClient())
            {
                // 设置请求头
                httpClient.DefaultRequestHeaders.Add("AppKey", AppKey);
                httpClient.DefaultRequestHeaders.Add("Sign", signature);
                httpClient.DefaultRequestHeaders.Add("TimeStamp", timestamp);
                httpClient.DefaultRequestHeaders.Add("RandomString", randomString);
                
                // 创建multipart/form-data内容
                using (var content = new MultipartFormDataContent())
                {
                    foreach (var item in formData)
                    {
                        content.Add(new StringContent(item.Value), item.Key);
                    }
                    
                    // 发送请求
                    var response = await httpClient.PostAsync(url, content);
                    
                    // 读取响应
                    string responseContent = await response.Content.ReadAsStringAsync();
                    
                    return ((int)response.StatusCode, responseContent);
                }
            }
        }

        /// <summary>
        /// 获取请求头JSON字符串
        /// </summary>
        private static string GetHeadersJson(string randomString, string timestamp, string signature)
        {
            var headers = new Dictionary<string, string>
            {
                { "AppKey", AppKey },
                { "Sign", signature },
                { "TimeStamp", timestamp },
                { "RandomString", randomString },
                { "Content-Type", "multipart/form-data" }
            };
            
            return JsonSerializer.Serialize(headers);
        }
    }
}
