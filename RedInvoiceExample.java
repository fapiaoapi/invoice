import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RedInvoiceExample {
    
    private static final String BASE_URL = "https://api.fa-piao.com";


    /**
     * 示例入口，演示POST multipart/form-data请求
     */
    public static void main(String[] args) throws Exception {
        try {
            String appKey = "";
            String appSecret = "";

            String nsrsbh = "";//统一社会信用代码
            String title = "";//名称（营业执照）
            String username = "";//手机号码（电子税务局）
            String password = "";//密码（电子税务局）
            String type = "7";//6基础版 7标准
            String token = "";
            String fphm = "26502000000536415466";
            String kprq = "";
            System.setOut(new PrintStream(System.out, true, "UTF-8"));
            System.out.println("java " + System.getProperty("java.version"));
            
            if(token.isEmpty()) {// 获取token 建议redis缓存 30天
                /*
                 * 获取授权Token文档
                 * @see https://fa-piao.com/doc.html#api1?source=github
                 */
                Map<String, Object> formData = new LinkedHashMap<>();
                formData.put("nsrsbh", nsrsbh);
                formData.put("type", type);
                // formData.put("username", username);
                // formData.put("password", password);

                /*
                 * 获取授权Token文档
                 * @see https://fa-piao.com/doc.html#api1?source=github
                 */
                ApiResponse tokenResponse = postRequest("/v5/enterprise/authorization",formData,"" ,appKey, appSecret);
                token = tokenResponse.getTokenString();
                if (token == null || token.isEmpty()) {
                    System.out.println("获取授权失败:" + tokenResponse.getMsg());
                    return;
                }
            }

            Map<String, Object> queryParams = new LinkedHashMap<>();
            queryParams.put("nsrsbh", nsrsbh);
            queryParams.put("fphm", fphm);
            queryParams.put("username", username);
            queryParams.put("sqyy", "2");
            if (kprq != null && !kprq.isEmpty()) {
                queryParams.put("kprq", kprq);
            }
            /*
             * 1. 数电申请红字前查蓝票信息接口
             * @link https://fa-piao.com/doc.html#api8?source=github
             */
            ApiResponse queryResponse = postRequest("/v5/enterprise/retMsg", queryParams, token, appKey, appSecret);
            if (queryResponse.getCode() != 200) {
                System.out.println(queryResponse.getCode() + " 查询蓝票失败:" + queryResponse.getMsg());
                System.out.println(queryResponse.getBody());
                return;
            }
            System.out.println("1 可以申请红字");

            Map<String, Object> applyParams = new LinkedHashMap<>();
            applyParams.put("xhdwsbh", nsrsbh);
            applyParams.put("yfphm", fphm);
            applyParams.put("username", username);
            applyParams.put("sqyy", "2");
            applyParams.put("chyydm", "01");

            /*
             * 2. 申请红字信息表
             * @link https://fa-piao.com/doc.html#api9?source=github
             */
            ApiResponse applyResponse = postRequest("/v5/enterprise/hzxxbsq", applyParams, token, appKey, appSecret);
            if (applyResponse.getCode() != 200) {
                System.out.println(applyResponse.getCode() + " 申请红字信息表失败:" + applyResponse.getMsg());
                System.out.println(applyResponse.getBody());
                return;
            }
            String xxbbh = applyResponse.getDataString("xxbbh");
            if (xxbbh == null || xxbbh.isEmpty()) {
                System.out.println("红字信息表缺少字段: xxbbh");
                System.out.println(applyResponse.getBody());
                return;
            }
            System.out.println("2 申请红字信息表成功");

            Map<String, Object> redParams = new LinkedHashMap<>();
            redParams.put("fpqqlsh", "red" + fphm);
            redParams.put("username", username);
            redParams.put("xhdwsbh", nsrsbh);
            redParams.put("tzdbh", xxbbh);
            redParams.put("yfphm", fphm);
            /*
             * 3. 开具红字发票
             * @link https://fa-piao.com/doc.html#api10?source=github
             */
            ApiResponse redResponse = postRequest("/v5/enterprise/hzfpkj", redParams, token, appKey, appSecret);
            if (redResponse.getCode() == 200) {
                System.out.println("3 负数开具成功");
            } else {
                System.out.println(redResponse.getCode() + " 数电票负数开具失败:" + redResponse.getMsg());
                System.out.println(redResponse.getBody());
            }
        } catch (Exception e) {
            System.out.println("请求异常：" + e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * post 请求
     */
    public static ApiResponse postRequest(String path,Map<String, Object> formData, String token, String appKey, String appSecret) throws Exception {

        String randomString = generateRandomString(20);
        String timestamp = String.valueOf(Instant.now().getEpochSecond());

        String signature = calculateSignature("POST", path, randomString, timestamp,appKey,appSecret);

        Map<String, String> headers = new LinkedHashMap<>();
        headers.put("AppKey", appKey);
        headers.put("Sign", signature);
        headers.put("TimeStamp", timestamp);
        headers.put("RandomString", randomString);
        if (token != null && !token.isEmpty()) {
            headers.put("Authorization", "Bearer " + token);
        }


        return postMultipart(path, headers, formData);
    }

    /**
     * 发送POST multipart/form-data请求
     */
    public static ApiResponse postMultipart(String path, Map<String, String> headers, Map<String, Object> formData)
            throws IOException {
        String boundary = "----JavaFormBoundary" + generateRandomString(24);
        byte[] body = buildMultipartBody(formData, boundary);
        String urlString = BASE_URL + path;
        HttpURLConnection connection = openConnection(urlString, "POST", headers);
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        connection.setRequestProperty("Content-Length", String.valueOf(body.length));
        connection.getOutputStream().write(body);
        connection.getOutputStream().flush();
        connection.getOutputStream().close();
        return readResponse(connection);
    }

    /**
     * 生成随机字符串
     */
    private static String generateRandomString(int length) {
        SecureRandom random = new SecureRandom();
        String chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder();
        
        for(int i=0; i<length; i++) {
            int index = random.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }

    /**
     * 计算签名
     */
    private static String calculateSignature(String method, String path, 
                                            String randomString, String timestamp,
                                            String appKey, String appSecret) 
                                            throws Exception {
        String content = String.format(
            "Method=%s&Path=%s&RandomString=%s&TimeStamp=%s&AppKey=%s",
            method, path, randomString, timestamp, appKey
        );
        
        Mac sha256 = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(appSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256.init(secretKey);
        
        byte[] hash = sha256.doFinal(content.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hash).toUpperCase();
    }

    /**
     * 字节转十六进制字符串
     */
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static class ApiResponse {
        private final int statusCode;
        private final String body;
        private final Map<String, List<String>> headers;
        private final Object json;

        public ApiResponse(int statusCode, String body, Map<String, List<String>> headers) {
            this.statusCode = statusCode;
            this.body = body;
            this.headers = headers;
            this.json = parseJson(body);
        }

        public int getStatusCode() {
            return statusCode;
        }

        public String getBody() {
            return body;
        }

        public Map<String, List<String>> getHeaders() {
            return headers;
        }

        public boolean isJson() {
            return json != null;
        }

        public int getCode() {
            Object value = getJsonValue("code");
            if (value instanceof Number) {
                return ((Number) value).intValue();
            }
            return 0;
        }

        public String getMsg() {
            Object value = getJsonValue("msg");
            if (value == null) {
                value = getJsonValue("message");
            }
            return value == null ? null : String.valueOf(value);
        }

        public Object getData() {
            return getJsonValue("data");
        }

        public Object getDataValue(String key) {
            Object data = getData();
            if (!(data instanceof Map)) {
                return null;
            }
            return ((Map<?, ?>) data).get(key);
        }

        public String getDataString(String key) {
            Object value = getDataValue(key);
            return value == null ? null : String.valueOf(value);
        }

        public String getDataAsString() {
            Object data = getData();
            if (data == null) {
                return null;
            }
            if (data instanceof String || data instanceof Number || data instanceof Boolean) {
                return String.valueOf(data);
            }
            return null;
        }

        public String getTokenString () {
            Object data = getData();
            if (data instanceof Map) {
                Object token = ((Map<?, ?>) data).get("token");
                return token == null ? "" : String.valueOf(token);
            }
            return "";
        }

        public Object getJson() {
            return json;
        }

        private Object getJsonValue(String key) {
            if (!(json instanceof Map)) {
                return null;
            }
            return ((Map<?, ?>) json).get(key);
        }

        private static Object parseJson(String text) {
            if (text == null) {
                return null;
            }
            String trimmed = text.trim();
            if (trimmed.isEmpty()) {
                return null;
            }
            if (trimmed.charAt(0) != '{' && trimmed.charAt(0) != '[') {
                return null;
            }
            try {
                return new JsonParser(trimmed).parse();
            } catch (Exception e) {
                return null;
            }
        }
    }

    private static class JsonParser {
        private final String text;
        private int index;

        JsonParser(String text) {
            this.text = text;
            this.index = 0;
        }

        Object parse() {
            skipWhitespace();
            Object value = parseValue();
            skipWhitespace();
            return value;
        }

        private Object parseValue() {
            skipWhitespace();
            if (index >= text.length()) {
                return null;
            }
            char c = text.charAt(index);
            if (c == '{') {
                return parseObject();
            }
            if (c == '[') {
                return parseArray();
            }
            if (c == '"') {
                return parseString();
            }
            if (c == 't' || c == 'f' || c == 'n') {
                return parseLiteral();
            }
            return parseNumber();
        }

        private Map<String, Object> parseObject() {
            Map<String, Object> map = new LinkedHashMap<>();
            index++;
            skipWhitespace();
            if (peek('}')) {
                index++;
                return map;
            }
            while (index < text.length()) {
                skipWhitespace();
                String key = parseString();
                skipWhitespace();
                expect(':');
                Object value = parseValue();
                map.put(key, value);
                skipWhitespace();
                if (peek(',')) {
                    index++;
                    continue;
                }
                if (peek('}')) {
                    index++;
                    break;
                }
            }
            return map;
        }

        private List<Object> parseArray() {
            List<Object> list = new java.util.ArrayList<>();
            index++;
            skipWhitespace();
            if (peek(']')) {
                index++;
                return list;
            }
            while (index < text.length()) {
                Object value = parseValue();
                list.add(value);
                skipWhitespace();
                if (peek(',')) {
                    index++;
                    continue;
                }
                if (peek(']')) {
                    index++;
                    break;
                }
            }
            return list;
        }

        private String parseString() {
            expect('"');
            StringBuilder sb = new StringBuilder();
            while (index < text.length()) {
                char c = text.charAt(index++);
                if (c == '"') {
                    break;
                }
                if (c == '\\') {
                    if (index >= text.length()) {
                        break;
                    }
                    char esc = text.charAt(index++);
                    switch (esc) {
                        case '"':
                            sb.append('"');
                            break;
                        case '\\':
                            sb.append('\\');
                            break;
                        case '/':
                            sb.append('/');
                            break;
                        case 'b':
                            sb.append('\b');
                            break;
                        case 'f':
                            sb.append('\f');
                            break;
                        case 'n':
                            sb.append('\n');
                            break;
                        case 'r':
                            sb.append('\r');
                            break;
                        case 't':
                            sb.append('\t');
                            break;
                        case 'u':
                            sb.append(parseUnicode());
                            break;
                        default:
                            sb.append(esc);
                            break;
                    }
                } else {
                    sb.append(c);
                }
            }
            return sb.toString();
        }

        private char parseUnicode() {
            if (index + 4 > text.length()) {
                return '?';
            }
            String hex = text.substring(index, index + 4);
            index += 4;
            try {
                return (char) Integer.parseInt(hex, 16);
            } catch (Exception e) {
                return '?';
            }
        }

        private Object parseLiteral() {
            if (text.startsWith("true", index)) {
                index += 4;
                return Boolean.TRUE;
            }
            if (text.startsWith("false", index)) {
                index += 5;
                return Boolean.FALSE;
            }
            if (text.startsWith("null", index)) {
                index += 4;
                return null;
            }
            return null;
        }

        private Number parseNumber() {
            int start = index;
            boolean dot = false;
            boolean exp = false;
            if (peek('-')) {
                index++;
            }
            while (index < text.length()) {
                char c = text.charAt(index);
                if (c >= '0' && c <= '9') {
                    index++;
                    continue;
                }
                if (c == '.' && !dot) {
                    dot = true;
                    index++;
                    continue;
                }
                if ((c == 'e' || c == 'E') && !exp) {
                    exp = true;
                    index++;
                    if (peek('+') || peek('-')) {
                        index++;
                    }
                    continue;
                }
                break;
            }
            String num = text.substring(start, index);
            try {
                if (dot || exp) {
                    return Double.parseDouble(num);
                }
                return Long.parseLong(num);
            } catch (Exception e) {
                return 0;
            }
        }

        private void skipWhitespace() {
            while (index < text.length()) {
                char c = text.charAt(index);
                if (c == ' ' || c == '\n' || c == '\r' || c == '\t') {
                    index++;
                    continue;
                }
                break;
            }
        }

        private void expect(char c) {
            if (index < text.length() && text.charAt(index) == c) {
                index++;
            }
        }

        private boolean peek(char c) {
            return index < text.length() && text.charAt(index) == c;
        }
    }

    /**
     * 构建multipart/form-data请求体
     */
    private static byte[] buildMultipartBody(Map<String, Object> data, String boundary) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            writeString(out, "--" + boundary + "\r\n");
            writeString(out, "Content-Disposition: form-data; name=\"" + entry.getKey() + "\"\r\n\r\n");
            Object value = entry.getValue();
            writeString(out, value == null ? "" : String.valueOf(value));
            writeString(out, "\r\n");
        }
        writeString(out, "--" + boundary + "--\r\n");
        return out.toByteArray();
    }

    /**
     * 创建并初始化HTTP连接
     */
    private static HttpURLConnection openConnection(String urlString, String method, Map<String, String> headers)
            throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
        connection.setRequestMethod(method);
        connection.setUseCaches(false);
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        return connection;
    }

    /**
     * 读取HTTP响应内容
     */
    private static ApiResponse readResponse(HttpURLConnection connection) throws IOException {
        int statusCode = connection.getResponseCode();
        byte[] bytes;
        if (statusCode >= 200 && statusCode < 400) {
            bytes = readAllBytes(connection.getInputStream());
        } else {
            bytes = readAllBytes(connection.getErrorStream());
        }
        String body = bytes == null ? "" : new String(bytes, StandardCharsets.UTF_8);
        return new ApiResponse(statusCode, body, connection.getHeaderFields());
    }

    /**
     * 读取输入流为字节数组
     */
    private static byte[] readAllBytes(java.io.InputStream in) throws IOException {
        if (in == null) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int len;
        while ((len = in.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }
        in.close();
        return out.toByteArray();
    }

    /**
     * 按UTF-8写入字符串
     */
    private static void writeString(ByteArrayOutputStream out, String value) throws IOException {
        out.write(value.getBytes(StandardCharsets.UTF_8));
    }
}
