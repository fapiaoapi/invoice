import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class BasicExample {
    private static final String BASE_URL = "https://api.fa-piao.com";

    public static String appKey = "";
    public static String appSecret = "";

    public static String nsrsbh = "";// 统一社会信用代码
    public static String title = "";// 名称（营业执照）
    public static String username = "";// 手机号码（电子税务局）
    public static String password = "";// 个人用户密码（电子税务局）
    public static String type = "6";// 6 基础 7标准
    public static String xhdwdzdh = "重庆市渝北区龙溪街道丽园路2号XXXX 1325580XXXX"; // 地址和电话 空格隔开
    public static String xhdwyhzh = "工商银行XXXX 15451211XXXX";// 开户行和银行账号 空格隔开

    public static String token = "";
    public static boolean debug = true; // 是否打印日志

    /**
     * 示例入口，演示POST multipart/form-data请求
     */
    public static void main(String[] args) throws Exception {
        try {

            System.setOut(new PrintStream(System.out, true, "UTF-8"));
            System.out.println("java " + System.getProperty("java.version"));
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Future<String> future = executor.submit(() -> new Scanner(System.in).nextLine());

            if (token.isEmpty()) {// 获取token 建议redis缓存 30天
                /*
                 * 获取授权Token文档
                 * 
                 * @see https://fa-piao.com/doc.html#api1?source=github
                 */
                Map<String, Object> formData = new LinkedHashMap<>();
                formData.put("nsrsbh", nsrsbh);
                formData.put("type", type);
                formData.put("username", username);
                formData.put("password", password);
                ApiResponse tokenResponse = postRequest("/v5/enterprise/authorization", formData);
                if (tokenResponse.getCode() == 200) {
                    token = tokenResponse.getTokenString();
                    // todo redis缓存 30天
                    System.out.println("token: " + token);
                }else{
                    System.out.println("获取授权Token失败: " + tokenResponse.getBody());
                    return;
                }
            }
            /*
             * 前端模拟数电发票/电子发票开具 (蓝字发票)
             * 
             * @see https://fa-piao.com/fapiao.html?source=github
             *
             */

            /*
             *
             * 开票参数说明demo
             * 
             * @see TaxExample.java
             */

            // 开具蓝票参数
            Map<String, Object> invoiceParams = new LinkedHashMap<>();
            invoiceParams.put("fplxdm", "82");
            invoiceParams.put("fpqqlsh", appKey + System.currentTimeMillis());
            invoiceParams.put("ghdwmc", "个人");
            // invoiceParams.put("ghdwsbh", "914208XXXXXXX");
            invoiceParams.put("hjje", 396.04);
            invoiceParams.put("hjse", 3.96);
            invoiceParams.put("jshj", 400);
            invoiceParams.put("kplx", 0);
            invoiceParams.put("username", username);
            invoiceParams.put("xhdwdzdh", xhdwdzdh);
            invoiceParams.put("xhdwmc", title);
            invoiceParams.put("xhdwsbh", nsrsbh);
            invoiceParams.put("xhdwyhzh", xhdwyhzh);
            invoiceParams.put("zsfs", 0);
            invoiceParams.put("fyxm[0][fphxz]", 0);
            invoiceParams.put("fyxm[0][spmc]", "*软件维护服务*接口服务费");
            invoiceParams.put("fyxm[0][ggxh]", "");
            invoiceParams.put("fyxm[0][dw]", "次");
            invoiceParams.put("fyxm[0][spsl]", 100);
            invoiceParams.put("fyxm[0][dj]", 1);
            invoiceParams.put("fyxm[0][je]", 100);
            invoiceParams.put("fyxm[0][sl]", 0.01);
            invoiceParams.put("fyxm[0][se]", 0.99);
            invoiceParams.put("fyxm[0][hsbz]", 1);
            invoiceParams.put("fyxm[0][spbm]", "3040201030000000000");
            invoiceParams.put("fyxm[1][fphxz]", 0);
            invoiceParams.put("fyxm[1][spmc]", "*软件维护服务*接口服务费");
            invoiceParams.put("fyxm[1][ggxh]", "");
            invoiceParams.put("fyxm[1][spsl]", 150);
            invoiceParams.put("fyxm[1][dj]", 2);
            invoiceParams.put("fyxm[1][je]", 300);
            invoiceParams.put("fyxm[1][sl]", 0.01);
            invoiceParams.put("fyxm[1][se]", 2.97);
            invoiceParams.put("fyxm[1][hsbz]", 1);
            invoiceParams.put("fyxm[1][spbm]", "3040201030000000000");

            /*
             * 开具数电发票文档
             * 
             * @see https://fa-piao.com/doc.html#api6?source=github
             *
             */
            ApiResponse invoiceResponse = postRequest("/v5/enterprise/blueTicket", invoiceParams);

            switch (invoiceResponse.getCode()) {//
                case 200:
                    String fphm = invoiceResponse.getDataString("Fphm");
                    String kprq = invoiceResponse.getDataString("Kprq");
                    if (fphm == null || kprq == null) {
                        System.out.println("返回字段缺失: " + invoiceResponse.getBody());
                        return;
                    }
                    System.out.println("发票号码: " + fphm);
                    System.out.println("开票日期: " + kprq);

                    Map<String, Object> pdfParams = new HashMap<>();
                    pdfParams.put("downflag", "4");
                    pdfParams.put("nsrsbh", nsrsbh);
                    pdfParams.put("username", username);
                    pdfParams.put("fphm", fphm);
                    pdfParams.put("Kprq", kprq);
                    ApiResponse pdfResponse = postRequest("/v5/enterprise/pdfOfdXml", pdfParams);
                    if (pdfResponse.getCode() == 200) {
                        System.out.println("下载发票成功：" + pdfResponse.getBody());
                    } else {
                        System.out.println("下载发票失败:" + pdfResponse.getMsg());
                    }
                    break;
                case 420:
                    System.out.println("登录(短信认证)");
                    /*
                     * 前端模拟短信认证弹窗
                     * 
                     * @see https://fa-piao.com/fapiao.html?action=sms&source=github
                     */
                    // 1. 发短信验证码
                    /*
                     * @see https://fa-piao.com/doc.html#api2?source=github
                     */
                    Map<String, Object> smsData = new LinkedHashMap<>();
                    smsData.put("nsrsbh", nsrsbh);
                    smsData.put("username", username);
                    smsData.put("password", password);
                    ApiResponse smsResponse = postRequest("/v5/enterprise/loginDppt", smsData);
                    if (smsResponse.getCode() == 200) {
                        System.out.println("请输入验证码");
                        try {
                            System.out.print("300秒内("
                                    + LocalDateTime.now().plusSeconds(300)
                                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                                    + "前)输入内容: ");
                            String smsCode = future.get(300, TimeUnit.SECONDS);

                            System.out.println("🎉 成功获取输入: " + smsCode + " \n" + "当前时间: " + LocalDateTime.now()
                                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                            // 2. 输入验证码
                            /*
                             * @see https://fa-piao.com/doc.html#api2?source=github
                             */
                            smsData.put("sms", smsCode);
                            ApiResponse codeResponse = postRequest("/v5/enterprise/loginDppt", smsData);
                            if (codeResponse.getCode() == 200) {
                                System.out.println("验证短信成功：");
                                ApiResponse invoiceResponse2 = postRequest("/v5/enterprise/blueTicket", invoiceParams);
                                if (invoiceResponse2.getCode() == 200) {
                                    Map<String, Object> pdfParams2 = new HashMap<>();
                                    pdfParams2.put("downflag", "4");
                                    pdfParams2.put("nsrsbh", nsrsbh);
                                    pdfParams2.put("username", username);
                                    pdfParams2.put("fphm", invoiceResponse2.getDataString("Fphm"));
                                    pdfParams2.put("kprq", invoiceResponse2.getDataString("Kprq"));
                                    ApiResponse pdfResponse2 = postRequest("/v5/enterprise/pdfOfdXml", pdfParams2);
                                    if (pdfResponse2.getCode() == 200) {
                                        System.out.println("下载发票成功：" + pdfResponse2.getBody());
                                    }
                                }else{
                                    System.out.println(invoiceResponse2.getCode()+"开票失败:" + invoiceResponse2.getMsg());
                                }
                            } else {
                                System.out.println("验证短信失败:" + codeResponse.getMsg());
                            }

                        } catch (TimeoutException | InterruptedException | ExecutionException e) {
                            System.out.println("\n短信认证输入超时！");
                            future.cancel(true);
                        }
                        executor.shutdown();
                    }

                    break;
                case 430:
                    System.out.println("人脸认证");
                    /*
                     * 前端模拟人脸认证弹窗
                     * 
                     * @see https://fa-piao.com/fapiao.html?action=face&source=github
                     */
                    // 1. 获取人脸二维码
                    /*
                     * @see https://fa-piao.com/doc.html#api3?source=github
                     */
                    Map<String, Object> faceData = new LinkedHashMap<>();
                    faceData.put("nsrsbh", nsrsbh);
                    faceData.put("username", username);
                    ApiResponse faceResponse = getRequest("/v5/enterprise/getFaceImg", faceData);
                    String rzid = "";
                    if (faceResponse.getCode() == 200) {
                        System.out.println("获取人脸二维码成功：" + faceResponse.getBody());
                        String ewmly = faceResponse.getDataString("ewmly");
                        String ewm = faceResponse.getDataString("ewm");
                        rzid = faceResponse.getDataString("rzid");
                        if (ewmly != null) {
                            System.out.println("swj".equals(ewmly) ? "请使用电子税务局app扫码" : "个人所得税app扫码");
                        }
                        if (ewm != null && ewm.length() < 500) {
                            // todo 字符串转图片base 返回给前端
                            // String base64Uri = "data:image/png;base64," + base64;
                            // 前端使用示例: <img src="base64Uri" />
                        }
                        // 字符串转二维码图片 命令行终端打印
                        stringToQrcode(ewm);
                        System.out.println("成功做完人脸认证,请输入数字 1");
                        try {
                            System.out.print("300秒内(" + LocalDateTime.now().plusSeconds(300)
                                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "前)输入内容: ");
                            String inputNum = future.get(300, TimeUnit.SECONDS);
                            System.out.println("🎉 成功获取输入: " + inputNum + " \n" + "当前时间: "
                                    + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                            // 2. 认证完成后获取人脸二维码认证状态
                            /*
                             * @see https://fa-piao.com/doc.html#api4?source=github
                             */
                            Map<String, Object> faceStatusData = new LinkedHashMap<>();
                            faceStatusData.put("nsrsbh", nsrsbh);
                            faceStatusData.put("username", username);
                            faceStatusData.put("rzid", rzid);
                            ApiResponse faceStatusResponse = getRequest("/v5/enterprise/getFaceState", faceStatusData);
                            if (faceStatusResponse.getCode() == 200) {
                                String slzt = faceStatusResponse.getDataString("slzt");
                                if ("2".equals(slzt)) {
                                    System.out.println("认证状态: 成功");
                                    System.out.println("请再次调用blueTicket");
                                    ApiResponse invoiceResponse2 = postRequest("/v5/enterprise/blueTicket",
                                            invoiceParams);
                                    if (invoiceResponse2.getCode() == 200) {
                                        Map<String, Object> pdfParams2 = new HashMap<>();
                                        pdfParams2.put("downflag", "4");
                                        pdfParams2.put("nsrsbh", nsrsbh);
                                        pdfParams2.put("username", username);
                                        pdfParams2.put("fphm", invoiceResponse2.getDataString("Fphm"));
                                        pdfParams2.put("kprq", invoiceResponse2.getDataString("Kprq"));
                                        ApiResponse pdfResponse2 = postRequest("/v5/enterprise/pdfOfdXml", pdfParams2);
                                        if (pdfResponse2.getCode() == 200) {
                                            System.out.println("下载发票成功：" + pdfResponse2.getBody());
                                        }
                                    }
                                } else {
                                    System.out.println("认证状态: " + ("1".equals(slzt) ? "未认证" : "二维码过期"));
                                }
                            } else {
                                System.out.println("获取人脸二维码认证状态失败:" + faceStatusResponse.getMsg());
                            }

                        } catch (TimeoutException | InterruptedException | ExecutionException e) {
                            System.out.println("\n人脸认证输入超时！");
                            future.cancel(true);
                        }
                        executor.shutdown();
                    } else {
                        System.out.println("获取人脸二维码失败:" + faceResponse.getMsg());
                    }

                    break;
                case 401:
                    // token过期 重新获取并缓存token
                    System.out.println("授权失败:" + invoiceResponse.getMsg());
                    System.out.println("401  token过期 重新获取并缓存token");
                    // 重新获取token的逻辑
                    ApiResponse tokenResponse = postRequest("/v5/enterprise/authorization", formData);
                    token = tokenResponse.getTokenString();
                    System.out.println("再调用blueTicket");
                    ApiResponse invoiceResponse2 = postRequest("/v5/enterprise/blueTicket", invoiceParams);
                    if (invoiceResponse2.getCode() == 200) {
                        Map<String, Object> pdfParams2 = new HashMap<>();
                        pdfParams2.put("downflag", "4");
                        pdfParams2.put("nsrsbh", nsrsbh);
                        pdfParams2.put("username", username);
                        pdfParams2.put("fphm", invoiceResponse2.getDataString("Fphm"));
                        pdfParams2.put("kprq", invoiceResponse2.getDataString("Kprq"));
                        ApiResponse pdfResponse2 = postRequest("/v5/enterprise/pdfOfdXml", pdfParams2);
                        if (pdfResponse2.getCode() == 200) {
                            System.out.println("下载发票成功：" + pdfResponse2.getBody());
                        }
                    }
                    break;
                default:
                    System.out.println(invoiceResponse.getCode() + " " + invoiceResponse.getMsg());
                    break;
            }
        } catch (Exception e) {
            System.out.println("请求异常：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * post 请求
     */
    public static ApiResponse postRequest(String path, Map<String, Object> formData) throws Exception {

        String randomString = generateRandomString(20);
        String timestamp = String.valueOf(Instant.now().getEpochSecond());

        String signature = calculateSignature("POST", path, randomString, timestamp, appKey, appSecret);

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
     * 发送GET请求，参数拼接在URL中
     */
    public static ApiResponse getRequest(String path, Map<String, Object> params) throws Exception {
        String query = buildQueryString(params);
        String urlString = BASE_URL + path + (query.isEmpty() ? "" : "?" + query);

        String randomString = generateRandomString(20);
        String timestamp = String.valueOf(Instant.now().getEpochSecond());
        String signature = calculateSignature("GET", path, randomString, timestamp, appKey, appSecret);
        Map<String, String> headers = new LinkedHashMap<>();
        headers.put("AppKey", appKey);
        headers.put("Sign", signature);
        headers.put("TimeStamp", timestamp);
        headers.put("RandomString", randomString);
        if (token != null && !token.isEmpty()) {
            headers.put("Authorization", "Bearer " + token);
        }
        debugRequest(urlString, "GET", headers, params);
        HttpURLConnection connection = openConnection(urlString, "GET", headers);
        ApiResponse response = readResponse(connection);
        debugResponse(response);
        return response;
    }

    /**
     * 发送POST multipart/form-data请求
     */
    public static ApiResponse postMultipart(String path, Map<String, String> headers, Map<String, Object> formData)
            throws IOException {
        String boundary = "----JavaFormBoundary" + generateRandomString(24);
        byte[] body = buildMultipartBody(formData, boundary);
        String urlString = BASE_URL + path;
        debugRequest(urlString, "POST", headers, formData);
        HttpURLConnection connection = openConnection(urlString, "POST", headers);
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        connection.setRequestProperty("Content-Length", String.valueOf(body.length));
        connection.getOutputStream().write(body);
        connection.getOutputStream().flush();
        connection.getOutputStream().close();
        ApiResponse response = readResponse(connection);
        debugResponse(response);
        return response;
    }

    public static void stringToQrcode(String content) {
        File jar = new File("zxing-core-3.5.3.jar");
        if (!jar.isFile()) {
            System.out.print("缺少 zxing-core-3.5.3.jar (Missing zxing-core-3.5.3.jar) !!");
            return;
        }
        try (URLClassLoader loader = new URLClassLoader(new URL[] { jar.toURI().toURL() },
                InputExample.class.getClassLoader())) {
            Class<?> writerClass = Class.forName("com.google.zxing.qrcode.QRCodeWriter", true, loader);
            Class<?> formatClass = Class.forName("com.google.zxing.BarcodeFormat", true, loader);
            Class<?> hintClass = Class.forName("com.google.zxing.EncodeHintType", true, loader);
            Class<?> ecClass = Class.forName("com.google.zxing.qrcode.decoder.ErrorCorrectionLevel", true, loader);
            Map<Object, Object> hints = new HashMap<>();
            hints.put(enumValue(hintClass, "CHARACTER_SET"), "UTF-8");
            hints.put(enumValue(hintClass, "ERROR_CORRECTION"), enumValue(ecClass, "H"));
            hints.put(enumValue(hintClass, "MARGIN"), 4);
            Object writer = writerClass.getDeclaredConstructor().newInstance();
            Method encode = writerClass.getMethod("encode", String.class, formatClass, int.class, int.class, Map.class);
            Object matrix = encode.invoke(writer, content, enumValue(formatClass, "QR_CODE"), 1, 1, hints);
            Method getWidth = matrix.getClass().getMethod("getWidth");
            Method getHeight = matrix.getClass().getMethod("getHeight");
            Method get = matrix.getClass().getMethod("get", int.class, int.class);
            int width = (Integer) getWidth.invoke(matrix);
            int height = (Integer) getHeight.invoke(matrix);
            System.out.println();
            for (int y = 0; y < height; y += 2) {
                StringBuilder line = new StringBuilder(width);
                for (int x = 0; x < width; x++) {
                    boolean top = (Boolean) get.invoke(matrix, x, y);
                    boolean bottom = y + 1 < height && (Boolean) get.invoke(matrix, x, y + 1);
                    line.append(top ? (bottom ? '█' : '▀') : (bottom ? '▄' : ' '));
                }
                System.out.println(line);
            }
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Object enumValue(Class<?> enumClass, String name) {
        for (Object v : enumClass.getEnumConstants()) {
            if (((Enum<?>) v).name().equals(name))
                return v;
        }
        throw new IllegalArgumentException(name);
    }

    private static void debugRequest(String url, String method, Map<String, String> headers,
            Map<String, Object> params) {
        if (!debug) {
            return;
        }
        System.out.println("[debug] url=" + url);
        System.out.println("[debug] method=" + method);
        System.out.println("[debug] headers=" + (headers == null ? "{}" : headers));
        System.out.println("[debug] params=" + (params == null ? "{}" : params));
    }

    private static void debugResponse(ApiResponse response) {
        if (!debug) {
            return;
        }
        System.out.println("[debug] statusCode=" + (response == null ? "" : response.getStatusCode()));
        System.out.println("[debug] response=" + (response == null ? "" : response.getBody()));
    }

    /**
     * 生成随机字符串
     */
    private static String generateRandomString(int length) {
        SecureRandom random = new SecureRandom();
        String chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
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
                method, path, randomString, timestamp, appKey);

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
            if (hex.length() == 1)
                hexString.append('0');
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

        public String getTokenString() {
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
     * 构建URL查询字符串
     */
    private static String buildQueryString(Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            Object value = entry.getValue();
            sb.append(encode(entry.getKey()))
                    .append("=")
                    .append(encode(value == null ? "" : String.valueOf(value)));
        }
        return sb.toString();
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
     * URL编码
     */
    private static String encode(String value) {
        try {
            return URLEncoder.encode(value == null ? "" : value, StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * 按UTF-8写入字符串
     */
    private static void writeString(ByteArrayOutputStream out, String value) throws IOException {
        out.write(value.getBytes(StandardCharsets.UTF_8));
    }
}
