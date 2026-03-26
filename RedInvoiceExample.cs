using System;
using System.Collections.Generic;
using System.IO;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Security.Cryptography;
using System.Text;

public class RedInvoiceExample
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
            string username = "";
            string type = "7";
            string token = "";
            string fphm = "26502000000536415466";
            string kprq = "";
            Console.OutputEncoding = Encoding.UTF8;
            Console.WriteLine("dotnet " + Environment.Version);

            if (string.IsNullOrEmpty(token))
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

                /*
                 * 获取授权Token文档
                 * @see https://fa-piao.com/doc.html#api1?source=github
                 */
                ApiResponse tokenResponse = postRequest("/v5/enterprise/authorization", formData, "", appKey, appSecret);
                token = tokenResponse.getTokenString();
                if (string.IsNullOrEmpty(token))
                {
                    Console.WriteLine("获取授权失败:" + tokenResponse.getMsg());
                    return;
                }
            }

            Dictionary<string, object> queryParams = new Dictionary<string, object>();
            queryParams["nsrsbh"] = nsrsbh;
            queryParams["fphm"] = fphm;
            queryParams["username"] = username;
            queryParams["sqyy"] = "2";
            if (!string.IsNullOrEmpty(kprq))
            {
                queryParams["kprq"] = kprq;
            }
            /*
             * 1. 数电申请红字前查蓝票信息接口
             * @link https://fa-piao.com/doc.html#api8?source=github
             */
            ApiResponse queryResponse = postRequest("/v5/enterprise/retMsg", queryParams, token, appKey, appSecret);
            if (queryResponse.getCode() != 200)
            {
                Console.WriteLine(queryResponse.getCode() + " 查询蓝票失败:" + queryResponse.getMsg());
                Console.WriteLine(queryResponse.getBody());
                return;
            }
            Console.WriteLine("1 可以申请红字");

            Dictionary<string, object> applyParams = new Dictionary<string, object>();
            applyParams["xhdwsbh"] = nsrsbh;
            applyParams["yfphm"] = fphm;
            applyParams["username"] = username;
            applyParams["sqyy"] = "2";
            applyParams["chyydm"] = "01";

            /*
             * 2. 申请红字信息表
             * @link https://fa-piao.com/doc.html#api9?source=github
             */
            ApiResponse applyResponse = postRequest("/v5/enterprise/hzxxbsq", applyParams, token, appKey, appSecret);
            if (applyResponse.getCode() != 200)
            {
                Console.WriteLine(applyResponse.getCode() + " 申请红字信息表失败:" + applyResponse.getMsg());
                Console.WriteLine(applyResponse.getBody());
                return;
            }
            string xxbbh = applyResponse.getDataString("xxbbh");
            if (string.IsNullOrEmpty(xxbbh))
            {
                Console.WriteLine("红字信息表缺少字段: xxbbh");
                Console.WriteLine(applyResponse.getBody());
                return;
            }
            Console.WriteLine("2 申请红字信息表成功");

            Dictionary<string, object> redParams = new Dictionary<string, object>();
            redParams["fpqqlsh"] = "red" + fphm;
            redParams["username"] = username;
            redParams["xhdwsbh"] = nsrsbh;
            redParams["tzdbh"] = xxbbh;
            redParams["yfphm"] = fphm;
            /*
             * 3. 开具红字发票
             * @link https://fa-piao.com/doc.html#api10?source=github
             */
            ApiResponse redResponse = postRequest("/v5/enterprise/hzfpkj", redParams, token, appKey, appSecret);
            if (redResponse.getCode() == 200)
            {
                Console.WriteLine("3 负数开具成功");
            }
            else
            {
                Console.WriteLine(redResponse.getCode() + " 数电票负数开具失败:" + redResponse.getMsg());
                Console.WriteLine(redResponse.getBody());
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
     * 按UTF-8写入字符串
     */
    private static void writeString(Stream outStream, string value)
    {
        byte[] bytes = Encoding.UTF8.GetBytes(value ?? "");
        outStream.Write(bytes, 0, bytes.Length);
    }
}
