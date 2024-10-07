import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class Main {
	public static Integer generateRandomNum(Integer min,Integer max) {
        Random random = new Random();
        Integer randomNumber = random.nextInt(max) + min;
    	return randomNumber;
	}

	public static String generateRandomString(int length) {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            char c = characters.charAt(random.nextInt(characters.length()));
            sb.append(c);
        }
        return sb.toString();
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static String md5(String string) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(string.getBytes());
            String hexString = bytesToHex(digest);
            return hexString;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
			return "";
        }

    }

    public static void main(String []args) {
         String url = "https://api.fa-piao.com/v3/merchant/login";
        String appKey = "test";
        String secret = "ZfFNRhzMbdpGQsYArc9dSZZAVEJ3A7";
        Integer requestId = generateRandomNum(1,10000);
        Long timeStamp = System.currentTimeMillis() / 1000;
        String randomString = generateRandomString(20);
        String requestString = generateRandomString(20);

        String string = "RandomString="+randomString+"&RequestId="+requestId+"&TimeStamp="+timeStamp+"&appKey=test&secret="+secret;
        String signString = md5(string).toUpperCase();
        System.out.println(string);
        System.out.println(signString);


        try {
            String jsonString ="{\"id\":88}";

            URL dummyUrl = new URL(url);
            HttpURLConnection con = (HttpURLConnection) dummyUrl.openConnection();
            con.setRequestMethod("POST");
            //设置请求Header
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            con.setRequestProperty("charset", "utf-8");
            con.setRequestProperty("Content-Length", Integer.toString(jsonString.length()));

            con.setRequestProperty("AppKey", appKey);
            con.setRequestProperty("RequestId", String.valueOf(requestId));
            con.setRequestProperty("TimeStamp", String.valueOf(timeStamp));
            con.setRequestProperty("RandomString", randomString);
            con.setRequestProperty("RequestString", requestString);
            con.setRequestProperty("Sign", signString);

            //发送请求
            con.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
            writer.write(jsonString);
            writer.flush();
            //获取响应
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String response = reader.readLine();
            System.out.println(response);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}