using System;
using System.Collections.Generic;
using System.IO;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Security.Cryptography;
using System.Text;

public class BasicExample
{
    private static readonly string BASE_URL = "https://api.fa-piao.com";
    private static readonly HttpClient HttpClient = new HttpClient();


    /**
     * 示例入口，演示POST multipart/form-data请求
     */
    public static void Main(string[] args)
    {
        try
        {
            string appKey = "";
            string appSecret = "";

            string nsrsbh = "";
            string title = "";
            string username = "";
            string password = "";
            string type = "7";
            string token = "";
            Console.OutputEncoding = Encoding.UTF8;
            Console.WriteLine("dotnet " + Environment.Version);

            if (string.IsNullOrEmpty(token)) // 获取token 建议redis缓存 30天
            {
                /*
                 * 获取授权Token文档
                 * @see https://fa-piao.com/doc.html#api1?source=github
                 */
                Dictionary<string, object> formData = new Dictionary<string, object>();
                formData["nsrsbh"] = nsrsbh;
                formData["type"] = type;
                // formData["username"] = username;
                // formData["password"] = password;
                ApiResponse tokenResponse = postRequest("/v5/enterprise/authorization", formData, "", appKey, appSecret);
                token = tokenResponse.getTokenString();
                //todo redis缓存 30天
                
                // Console.WriteLine("token: " + token);
            }

            /*
             * 前端模拟数电发票/电子发票开具 (蓝字发票)
             * @see https://fa-piao.com/fapiao.html?source=github
             *
             */

            /*
             *
             * 开票参数说明demo
             * @see TaxExample.java
             */

            //开具蓝票参数
            Dictionary<string, object> invoiceParams = new Dictionary<string, object>();
            invoiceParams["fplxdm"] = "82";
            invoiceParams["fpqqlsh"] = appKey + DateTimeOffset.UtcNow.ToUnixTimeMilliseconds();
            invoiceParams["ghdwmc"] = "个人";
//                    invoiceParams.put("ghdwsbh", "914208XXXXXXX");
            invoiceParams["hjje"] = 396.04;
            invoiceParams["hjse"] = 3.96;
            invoiceParams["jshj"] = 100;
            invoiceParams["kplx"] = 0;
            invoiceParams["username"] = username;
            invoiceParams["xhdwdzdh"] = "重庆市渝北区龙溪街道丽园路2号XXXX 1325580XXXX";
            invoiceParams["xhdwmc"] = title;
            invoiceParams["xhdwsbh"] = nsrsbh;
            invoiceParams["xhdwyhzh"] = "工商银行XXXX 15451211XXXX";
            invoiceParams["zsfs"] = 0;
            invoiceParams["fyxm[0][fphxz]"] = 0;
            invoiceParams["fyxm[0][spmc]"] = "*软件维护服务*接口服务费";
            invoiceParams["fyxm[0][ggxh]"] = "";
            invoiceParams["fyxm[0][dw]"] = "次";
            invoiceParams["fyxm[0][spsl]"] = 100;
            invoiceParams["fyxm[0][dj]"] = 1;
            invoiceParams["fyxm[0][je]"] = 100;
            invoiceParams["fyxm[0][sl]"] = 0.01;
            invoiceParams["fyxm[0][se]"] = 0.99;
            invoiceParams["fyxm[0][hsbz]"] = 1;
            invoiceParams["fyxm[0][spbm]"] = "3040201030000000000";
            invoiceParams["fyxm[1][fphxz]"] = 0;
            invoiceParams["fyxm[1][spmc]"] = "*软件维护服务*接口服务费";
            invoiceParams["fyxm[1][ggxh]"] = "";
            invoiceParams["fyxm[1][spsl]"] = 150;
            invoiceParams["fyxm[1][dj]"] = 2;
            invoiceParams["fyxm[1][je]"] = 300;
            invoiceParams["fyxm[1][sl]"] = 0.01;
            invoiceParams["fyxm[1][se]"] = 2.97;
            invoiceParams["fyxm[1][hsbz]"] = 1;
            invoiceParams["fyxm[1][spbm]"] = "3040201030000000000";

            /*
             * 开具数电发票文档
             * @see https://fa-piao.com/doc.html#api6?source=github
             *
             */
            ApiResponse invoiceResponse = postRequest("/v5/enterprise/blueTicket", invoiceParams, token, appKey, appSecret);

            switch (invoiceResponse.getCode())
            {
                case 200:
                    string fphm = invoiceResponse.getDataString("Fphm");
                    string kprq = invoiceResponse.getDataString("Kprq");
                    if (fphm == null || kprq == null)
                    {
                        Console.WriteLine("返回字段缺失: " + invoiceResponse.getBody());
                        return;
                    }
                    Console.WriteLine("发票号码: " + fphm);
                    Console.WriteLine("开票日期: " + kprq);

                    Dictionary<string, object> pdfParams = new Dictionary<string, object>();
                    pdfParams["downflag"] = "4";
                    pdfParams["nsrsbh"] = nsrsbh;
                    pdfParams["username"] = username;
                    pdfParams["fphm"] = fphm;
                    pdfParams["Kprq"] = kprq;
                    ApiResponse pdfResponse = postRequest("/v5/enterprise/pdfOfdXml", pdfParams, token, appKey, appSecret);
                    if (pdfResponse.getCode() == 200)
                    {
                        Console.WriteLine("下载发票成功：" + pdfResponse.getBody());
                    }
                    else
                    {
                        Console.WriteLine("下载发票失败:" + pdfResponse.getMsg());
                    }
                    break;
                case 420:
                    Console.WriteLine("登录(短信认证)");
                    /*
                     * 前端模拟短信认证弹窗
                     * @see https://fa-piao.com/fapiao.html?action=sms&source=github
                     */
                    // 1. 发短信验证码
                    /*
                     * @see https://fa-piao.com/doc.html#api2?source=github
                     */
                    Dictionary<string, object> smsData = new Dictionary<string, object>();
                    smsData["nsrsbh"] = nsrsbh;
                    smsData["username"] = username;
                    smsData["password"] = password;
//                    ApiResponse smsResponse = postRequest("/v5/enterprise/loginDppt",smsData,token, appKey, appSecret);
//                    if (smsResponse.getCode() == 200) {
//                        Console.WriteLine("发送短信成功：");
//                    } else {
//                        Console.WriteLine("发送短信失败:" + smsResponse.getMsg());
//                    }
                    // // 等待60秒
                    // Thread.sleep(60000);
                    // 2. 输入验证码
//                     smsData.put("sms", 399073); // 假设验证码为399073
//                     ApiResponse codeResponse = postRequest("/v5/enterprise/loginDppt",smsData,token, appKey, appSecret);
//                     if (codeResponse.getCode() == 200) {
//                         Console.WriteLine("验证短信成功：");
//                     } else {
//                         Console.WriteLine("验证短信失败:" + codeResponse.getMsg());
//                     }

                    break;
                case 430:
                    Console.WriteLine("人脸认证");
                    /*
                    * 前端模拟人脸认证弹窗
                    * @see https://fa-piao.com/fapiao.html?action=face&source=github
                    */
                    // 1. 获取人脸二维码
                    /*
                     * @see https://fa-piao.com/doc.html#api3?source=github
                     */
                    Dictionary<string, object> faceData = new Dictionary<string, object>();
                    faceData["nsrsbh"] = nsrsbh;
                    faceData["username"] = username;
//                    ApiResponse faceResponse = getRequest("/v5/enterprise/getFaceImg",faceData,token, appKey, appSecret);
//                    String rzid = "";
//                    if (faceResponse.getCode() == 200) {
//                        Console.WriteLine("获取人脸二维码成功："+ faceResponse.getBody());
//                        String ewmly = faceResponse.getDataString("ewmly");
//                        String ewm = faceResponse.getDataString("ewm");
//                        rzid = faceResponse.getDataString("rzid");
//                        if (ewmly != null) {
//                            Console.WriteLine("swj".equals(ewmly) ? "请使用税务局app扫码" : "个人所得税app扫码");
//                        }
//                        if (ewm != null && ewm.length() < 500) {
//                            //todo 字符串转图片base 返回给前端
//                            // String base64Uri = "data:image/png;base64," + base64;
//                            // 前端使用示例: <img src="base64Uri" />
//                        }
//                    } else {
//                        Console.WriteLine("获取人脸二维码失败:" + faceResponse.getMsg());
//                    }
                    // 2. 认证完成后获取人脸二维码认证状态
                    /*
                     * @see https://fa-piao.com/doc.html#api4?source=github
                     */
//                     Map<String, Object> faceStatusData = new LinkedHashMap<>();
//                     faceStatusData.put("nsrsbh", nsrsbh);
//                     faceStatusData.put("username", username);
//                     faceStatusData.put("rzid", rzid);
//                     ApiResponse faceStatusResponse = getRequest("/v5/enterprise/getFaceState",faceStatusData,token, appKey, appSecret);
//                     if (faceStatusResponse.getCode() == 200) {
//                         String slzt = faceStatusResponse.getDataString("slzt");
//                         String status = "1".equals(slzt) ? "未认证" : ("2".equals(slzt) ? "成功" : "二维码过期");
//                         Console.WriteLine("认证状态: " + status);
//                     } else {
//                         Console.WriteLine("获取人脸二维码认证状态失败:" + faceStatusResponse.getMsg());
//                     }
                    break;
                case 401:
                    // token过期 重新获取并缓存token
                    Console.WriteLine("授权失败:" + invoiceResponse.getMsg());
                    // 重新获取token的逻辑
                    break;
                default:
                    Console.WriteLine(invoiceResponse.getCode() + " " + invoiceResponse.getMsg());
                    break;
            }
        }
        catch (Exception e)
        {
            Console.WriteLine("请求异常：" + e.Message);
            Console.WriteLine(e);
        }
    }


    /**
     * post 请求
     */
    public static ApiResponse postRequest(string path, Dictionary<string, object> formData, string token, string appKey, string appSecret)
    {

        string randomString = generateRandomString(20);
        string timestamp = DateTimeOffset.UtcNow.ToUnixTimeSeconds().ToString();

        string signature = calculateSignature("POST", path, randomString, timestamp, appKey, appSecret);

        Dictionary<string, string> headers = new Dictionary<string, string>();
        headers["AppKey"] = appKey;
        headers["Sign"] = signature;
        headers["TimeStamp"] = timestamp;
        headers["RandomString"] = randomString;
        if (!string.IsNullOrEmpty(token))
        {
            headers["Authorization"] = "Bearer " + token;
        }


        return postMultipart(path, headers, formData);
    }

    /**
     * 发送GET请求，参数拼接在URL中
     */
    public static ApiResponse getRequest(string path, Dictionary<string, object> paramsData, string token, string appKey, string appSecret)
    {
        string query = buildQueryString(paramsData);
        string urlString = BASE_URL + path + (string.IsNullOrEmpty(query) ? "" : "?" + query);

        string randomString = generateRandomString(20);
        string timestamp = DateTimeOffset.UtcNow.ToUnixTimeSeconds().ToString();
        string signature = calculateSignature("GET", path, randomString, timestamp, appKey, appSecret);
        Dictionary<string, string> headers = new Dictionary<string, string>();
        headers["AppKey"] = appKey;
        headers["Sign"] = signature;
        headers["TimeStamp"] = timestamp;
        headers["RandomString"] = randomString;
        if (!string.IsNullOrEmpty(token))
        {
            headers["Authorization"] = "Bearer " + token;
        }
        return sendRequest("GET", urlString, headers, null, null);
    }

    /**
     * 发送POST multipart/form-data请求
     */
    public static ApiResponse postMultipart(string path, Dictionary<string, string> headers, Dictionary<string, object> formData)
    {
        string boundary = "----CSFormBoundary" + generateRandomString(24);
        byte[] body = buildMultipartBody(formData, boundary);
        string urlString = BASE_URL + path;
        string contentType = "multipart/form-data; boundary=" + boundary;
        return sendRequest("POST", urlString, headers, body, contentType);
    }

    /**
     * 生成随机字符串
     */
    private static string generateRandomString(int length)
    {
        string chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        byte[] data = new byte[length];
        using (RandomNumberGenerator rng = RandomNumberGenerator.Create())
        {
            rng.GetBytes(data);
        }
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++)
        {
            sb.Append(chars[data[i] % chars.Length]);
        }
        return sb.ToString();
    }

    /**
     * 计算签名
     */
    private static string calculateSignature(string method, string path,
                                            string randomString, string timestamp,
                                            string appKey, string appSecret)
    {
        string content = string.Format(
            "Method={0}&Path={1}&RandomString={2}&TimeStamp={3}&AppKey={4}",
            method, path, randomString, timestamp, appKey
        );

        using (HMACSHA256 sha256 = new HMACSHA256(Encoding.UTF8.GetBytes(appSecret)))
        {
            byte[] hash = sha256.ComputeHash(Encoding.UTF8.GetBytes(content));
            return bytesToHex(hash).ToUpper();
        }
    }

    /**
     * 字节转十六进制字符串
     */
    private static string bytesToHex(byte[] hash)
    {
        StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < hash.Length; i++)
        {
            string hex = (hash[i] & 0xff).ToString("x2");
            hexString.Append(hex);
        }
        return hexString.ToString();
    }

    public class ApiResponse
    {
        private readonly int statusCode;
        private readonly string body;
        private readonly Dictionary<string, List<string>> headers;
        private readonly object json;

        public ApiResponse(int statusCode, string body, Dictionary<string, List<string>> headers)
        {
            this.statusCode = statusCode;
            this.body = body;
            this.headers = headers;
            this.json = parseJson(body);
        }

        public int getStatusCode()
        {
            return statusCode;
        }

        public string getBody()
        {
            return body;
        }

        public Dictionary<string, List<string>> getHeaders()
        {
            return headers;
        }

        public bool isJson()
        {
            return json != null;
        }

        public int getCode()
        {
            object value = getJsonValue("code");
            if (value is long)
            {
                return (int)(long)value;
            }
            if (value is int)
            {
                return (int)value;
            }
            if (value is double)
            {
                return (int)(double)value;
            }
            return 0;
        }

        public string getMsg()
        {
            object value = getJsonValue("msg");
            if (value == null)
            {
                value = getJsonValue("message");
            }
            return value == null ? null : Convert.ToString(value);
        }

        public object getData()
        {
            return getJsonValue("data");
        }

        public object getDataValue(string key)
        {
            object data = getData();
            if (!(data is Dictionary<string, object> map))
            {
                return null;
            }
            map.TryGetValue(key, out object value);
            return value;
        }

        public string getDataString(string key)
        {
            object value = getDataValue(key);
            return value == null ? null : Convert.ToString(value);
        }

        public string getDataAsString()
        {
            object data = getData();
            if (data == null)
            {
                return null;
            }
            if (data is string || data is long || data is int || data is double || data is bool)
            {
                return Convert.ToString(data);
            }
            return null;
        }

        public string getTokenString()
        {
            object data = getData();
            if (data is Dictionary<string, object> map)
            {
                if (map.TryGetValue("token", out object token))
                {
                    return token == null ? "" : Convert.ToString(token);
                }
                return "";
            }
            return "";
        }

        public object getJson()
        {
            return json;
        }

        private object getJsonValue(string key)
        {
            if (!(json is Dictionary<string, object> map))
            {
                return null;
            }
            map.TryGetValue(key, out object value);
            return value;
        }

        private static object parseJson(string text)
        {
            if (text == null)
            {
                return null;
            }
            string trimmed = text.Trim();
            if (trimmed.Length == 0)
            {
                return null;
            }
            if (trimmed[0] != '{' && trimmed[0] != '[')
            {
                return null;
            }
            try
            {
                return new JsonParser(trimmed).parse();
            }
            catch
            {
                return null;
            }
        }
    }

    private class JsonParser
    {
        private readonly string text;
        private int index;

        public JsonParser(string text)
        {
            this.text = text;
            this.index = 0;
        }

        public object parse()
        {
            skipWhitespace();
            object value = parseValue();
            skipWhitespace();
            return value;
        }

        private object parseValue()
        {
            skipWhitespace();
            if (index >= text.Length)
            {
                return null;
            }
            char c = text[index];
            if (c == '{')
            {
                return parseObject();
            }
            if (c == '[')
            {
                return parseArray();
            }
            if (c == '"')
            {
                return parseString();
            }
            if (c == 't' || c == 'f' || c == 'n')
            {
                return parseLiteral();
            }
            return parseNumber();
        }

        private Dictionary<string, object> parseObject()
        {
            Dictionary<string, object> map = new Dictionary<string, object>();
            index++;
            skipWhitespace();
            if (peek('}'))
            {
                index++;
                return map;
            }
            while (index < text.Length)
            {
                skipWhitespace();
                string key = parseString();
                skipWhitespace();
                expect(':');
                object value = parseValue();
                map[key] = value;
                skipWhitespace();
                if (peek(','))
                {
                    index++;
                    continue;
                }
                if (peek('}'))
                {
                    index++;
                    break;
                }
            }
            return map;
        }

        private List<object> parseArray()
        {
            List<object> list = new List<object>();
            index++;
            skipWhitespace();
            if (peek(']'))
            {
                index++;
                return list;
            }
            while (index < text.Length)
            {
                object value = parseValue();
                list.Add(value);
                skipWhitespace();
                if (peek(','))
                {
                    index++;
                    continue;
                }
                if (peek(']'))
                {
                    index++;
                    break;
                }
            }
            return list;
        }

        private string parseString()
        {
            expect('"');
            StringBuilder sb = new StringBuilder();
            while (index < text.Length)
            {
                char c = text[index++];
                if (c == '"')
                {
                    break;
                }
                if (c == '\\')
                {
                    if (index >= text.Length)
                    {
                        break;
                    }
                    char esc = text[index++];
                    switch (esc)
                    {
                        case '"':
                            sb.Append('"');
                            break;
                        case '\\':
                            sb.Append('\\');
                            break;
                        case '/':
                            sb.Append('/');
                            break;
                        case 'b':
                            sb.Append('\b');
                            break;
                        case 'f':
                            sb.Append('\f');
                            break;
                        case 'n':
                            sb.Append('\n');
                            break;
                        case 'r':
                            sb.Append('\r');
                            break;
                        case 't':
                            sb.Append('\t');
                            break;
                        case 'u':
                            sb.Append(parseUnicode());
                            break;
                        default:
                            sb.Append(esc);
                            break;
                    }
                }
                else
                {
                    sb.Append(c);
                }
            }
            return sb.ToString();
        }

        private char parseUnicode()
        {
            if (index + 4 > text.Length)
            {
                return '?';
            }
            string hex = text.Substring(index, 4);
            index += 4;
            if (int.TryParse(hex, System.Globalization.NumberStyles.HexNumber, null, out int code))
            {
                return (char)code;
            }
            return '?';
        }

        private object parseLiteral()
        {
            if (matchLiteral("true"))
            {
                index += 4;
                return true;
            }
            if (matchLiteral("false"))
            {
                index += 5;
                return false;
            }
            if (matchLiteral("null"))
            {
                index += 4;
                return null;
            }
            return null;
        }

        private bool matchLiteral(string literal)
        {
            if (text.Length - index < literal.Length)
            {
                return false;
            }
            return string.Compare(text, index, literal, 0, literal.Length, StringComparison.Ordinal) == 0;
        }

        private object parseNumber()
        {
            int start = index;
            bool dot = false;
            bool exp = false;
            if (peek('-'))
            {
                index++;
            }
            while (index < text.Length)
            {
                char c = text[index];
                if (c >= '0' && c <= '9')
                {
                    index++;
                    continue;
                }
                if (c == '.' && !dot)
                {
                    dot = true;
                    index++;
                    continue;
                }
                if ((c == 'e' || c == 'E') && !exp)
                {
                    exp = true;
                    index++;
                    if (peek('+') || peek('-'))
                    {
                        index++;
                    }
                    continue;
                }
                break;
            }
            string num = text.Substring(start, index - start);
            if (dot || exp)
            {
                if (double.TryParse(num, System.Globalization.NumberStyles.Float, System.Globalization.CultureInfo.InvariantCulture, out double d))
                {
                    return d;
                }
                return 0d;
            }
            if (long.TryParse(num, out long l))
            {
                return l;
            }
            return 0L;
        }

        private void skipWhitespace()
        {
            while (index < text.Length)
            {
                char c = text[index];
                if (c == ' ' || c == '\n' || c == '\r' || c == '\t')
                {
                    index++;
                    continue;
                }
                break;
            }
        }

        private void expect(char c)
        {
            if (index < text.Length && text[index] == c)
            {
                index++;
            }
        }

        private bool peek(char c)
        {
            return index < text.Length && text[index] == c;
        }
    }

    /**
     * 构建multipart/form-data请求体
     */
    private static byte[] buildMultipartBody(Dictionary<string, object> data, string boundary)
    {
        using (MemoryStream outStream = new MemoryStream())
        {
            foreach (KeyValuePair<string, object> entry in data)
            {
                writeString(outStream, "--" + boundary + "\r\n");
                writeString(outStream, "Content-Disposition: form-data; name=\"" + entry.Key + "\"\r\n\r\n");
                object value = entry.Value;
                writeString(outStream, value == null ? "" : Convert.ToString(value));
                writeString(outStream, "\r\n");
            }
            writeString(outStream, "--" + boundary + "--\r\n");
            return outStream.ToArray();
        }
    }

    /**
     * 构建URL查询字符串
     */
    private static string buildQueryString(Dictionary<string, object> paramsData)
    {
        if (paramsData == null || paramsData.Count == 0)
        {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        foreach (KeyValuePair<string, object> entry in paramsData)
        {
            if (sb.Length > 0)
            {
                sb.Append("&");
            }
            object value = entry.Value;
            sb.Append(encode(entry.Key))
                    .Append("=")
                    .Append(encode(value == null ? "" : Convert.ToString(value)));
        }
        return sb.ToString();
    }

    private static ApiResponse sendRequest(string method, string urlString, Dictionary<string, string> headers, byte[] body, string contentType)
    {
        using (HttpRequestMessage request = new HttpRequestMessage(new HttpMethod(method), urlString))
        {
            if (headers != null)
            {
                foreach (KeyValuePair<string, string> entry in headers)
                {
                    request.Headers.TryAddWithoutValidation(entry.Key, entry.Value);
                }
            }

            if (body != null)
            {
                ByteArrayContent content = new ByteArrayContent(body);
                if (!string.IsNullOrEmpty(contentType))
                {
                    content.Headers.ContentType = MediaTypeHeaderValue.Parse(contentType);
                }
                request.Content = content;
            }

            HttpResponseMessage response = HttpClient.SendAsync(request).GetAwaiter().GetResult();
            string responseBody = response.Content.ReadAsStringAsync().GetAwaiter().GetResult();
            Dictionary<string, List<string>> responseHeaders = buildResponseHeaders(response);
            return new ApiResponse((int)response.StatusCode, responseBody, responseHeaders);
        }
    }

    private static Dictionary<string, List<string>> buildResponseHeaders(HttpResponseMessage response)
    {
        Dictionary<string, List<string>> responseHeaders = new Dictionary<string, List<string>>();
        foreach (KeyValuePair<string, IEnumerable<string>> header in response.Headers)
        {
            responseHeaders[header.Key] = new List<string>(header.Value);
        }
        foreach (KeyValuePair<string, IEnumerable<string>> header in response.Content.Headers)
        {
            responseHeaders[header.Key] = new List<string>(header.Value);
        }
        return responseHeaders;
    }

    /**
     * URL编码
     */
    private static string encode(string value)
    {
        return Uri.EscapeDataString(value ?? "");
    }

    /**
     * 按UTF-8写入字符串
     */
    private static void writeString(Stream outStream, string value)
    {
        byte[] bytes = Encoding.UTF8.GetBytes(value ?? "");
        outStream.Write(bytes, 0, bytes.Length);
    }
}
