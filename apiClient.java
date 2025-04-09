import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class ApiClient {
    
    private static final String BASE_URL = "https://api.fa-piao.com";
    private static final String APP_KEY = "YOUR_APP_KEY";
    private static final String APP_SECRET = "YOUR_SECRET_KEY";
    
    public static void main(String[] args) throws Exception {
        String path = "/v5/enterprise/authorization";
        String method = "POST";
        
        // 请求参数
        Map<String, String> formData = new HashMap<>();
        formData.put("nsrsbh", "915101820724315989");
        
        // 生成签名参数
        String randomString = generateRandomString(20);
        String timestamp = String.valueOf(Instant.now().getEpochSecond());
        
        // 计算签名
        String signature = calculateSignature(method, path, randomString, timestamp);
        
        // 构建请求体
        String boundary = "----JavaHttpBoundary";
        String requestBody = buildMultipartBody(formData, boundary);
        
        // 发送请求
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .header("AppKey", APP_KEY)
                .header("Sign", signature)
                .header("TimeStamp", timestamp)
                .header("RandomString", randomString)
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        // 打印调试信息
        System.out.println(method + ": " + BASE_URL + path);
        System.out.println("headers: " + getHeadersString(signature, timestamp, randomString, boundary));
        System.out.println("form-data: " + formData);

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        System.out.println("HTTP状态码: " + response.statusCode());
        System.out.println("响应内容: " + response.body());
    }
    
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
    
    private static String calculateSignature(String method, String path, 
                                            String randomString, String timestamp) 
                                            throws Exception {
        String content = String.format(
            "Method=%s&Path=%s&RandomString=%s&TimeStamp=%s&AppKey=%s",
            method, path, randomString, timestamp, APP_KEY
        );
        
        Mac sha256 = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(APP_SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256.init(secretKey);
        
        byte[] hash = sha256.doFinal(content.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hash).toUpperCase();
    }
    
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
    
    private static String buildMultipartBody(Map<String, String> data, String boundary) {
        StringBuilder body = new StringBuilder();
        
        for (Map.Entry<String, String> entry : data.entrySet()) {
            body.append("--").append(boundary).append("\r\n")
                .append("Content-Disposition: form-data; name=\"")
                .append(entry.getKey()).append("\"\r\n\r\n")
                .append(entry.getValue()).append("\r\n");
        }
        body.append("--").append(boundary).append("--\r\n");
        
        return body.toString();
    }
    
    private static String getHeadersString(String signature, String timestamp, 
                                         String randomString, String boundary) {
        return String.format(
            "{AppKey=%s, Sign=%s, TimeStamp=%s, RandomString=%s, Content-Type=%s}",
            APP_KEY, signature, timestamp, randomString, 
            "multipart/form-data; boundary=" + boundary
        );
    }
}
