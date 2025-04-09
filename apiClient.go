package main

import (
	"crypto/hmac"
	"crypto/rand"
	"crypto/sha256"
	"encoding/hex"
	"encoding/json" // 新增导入
	"fmt"
	"io"
	"mime/multipart"
	"net/http"
	"strings"
	"time"
)

const (
	BaseURL   = "https://api.fa-piao.com"
	AppKey    = "YOUR_APP_KEY"    // 替换为您的AppKey
	AppSecret = "YOUR_SECRET_KEY" // 替换为您的密钥
)

// 生成随机字符串
func generateRandomString(length int) string {
	const charset = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
	b := make([]byte, length)
	rand.Read(b) // 安全随机数生成
	for i := range b {
		b[i] = charset[b[i]%byte(len(charset))]
	}
	return string(b)
}

// 计算HMAC-SHA256签名
func calculateSignature(method, path, randomString, timestamp string) string {
	signContent := fmt.Sprintf(
		"Method=%s&Path=%s&RandomString=%s&TimeStamp=%s&AppKey=%s",
		method, path, randomString, timestamp, AppKey,
	)

	mac := hmac.New(sha256.New, []byte(AppSecret))
	mac.Write([]byte(signContent))
	return strings.ToUpper(hex.EncodeToString(mac.Sum(nil)))
}

// 创建multipart/form-data请求体
func createRequestBody(params map[string]string) (io.Reader, string) {
	body := &strings.Builder{}
	writer := multipart.NewWriter(body)

	for key, value := range params {
		_ = writer.WriteField(key, value)
	}
	writer.Close()

	return strings.NewReader(body.String()), writer.FormDataContentType()
}

func main() {
	// 准备请求参数
	path := "/v5/enterprise/authorization"
	method := "POST"
	params := map[string]string{
		"nsrsbh": "915101820724315989"
	}

	// 生成签名参数
	randomString := generateRandomString(20)
	timestamp := fmt.Sprintf("%d", time.Now().Unix())

	// 计算签名
	signature := calculateSignature(method, path, randomString, timestamp)

	// 创建请求体
	payload, contentType := createRequestBody(params)

	// 创建HTTP请求
	req, _ := http.NewRequest(method, BaseURL+path, payload)

	// 设置请求头
	req.Header.Set("AppKey", AppKey)
	req.Header.Set("Sign", signature)
	req.Header.Set("TimeStamp", timestamp)
	req.Header.Set("RandomString", randomString)
	req.Header.Set("Content-Type", contentType)

	// 打印请求信息
	fmt.Printf("%s: %s%s\n", method, BaseURL, path)

	// 转换headers为可打印格式
	headers := map[string]string{
		"AppKey":       AppKey,
		"Sign":         signature,
		"TimeStamp":    timestamp,
		"RandomString": randomString,
		"Content-Type": contentType,
	}
	headersJson, _ := json.Marshal(headers)
	fmt.Printf("headers: %s\n", headersJson)

	// 打印form-data参数
	paramsJson, _ := json.Marshal(params)
	fmt.Printf("form-data: %s\n", paramsJson)

	// 发送请求
	client := &http.Client{}
	resp, err := client.Do(req)
	if err != nil {
		fmt.Printf("请求失败: %v\n", err)
		return
	}
	defer resp.Body.Close()

	// 读取响应
	body, _ := io.ReadAll(resp.Body)
	fmt.Printf("HTTP状态码: %d\n", resp.StatusCode)
	fmt.Printf("响应内容: %s\n", string(body))
}
