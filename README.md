
#申请接入

[https://fa-piao.com](https://fa-piao.com) 数电发票接口申请入口，获取appKey和secret



## 响应参数code

| code值 | 含义       |
|-------| ---------- |
| 200   | 成功       |
| 432   | 签名异常   |
| 430   | 参数异常   |
| 419   | 短信验证 |
| 420   | 人脸验证 |
| 503   | 服务异常 |


# 发票管理

## 开票接口(快速开票)

接口路径：/v3/invoice/add

请求方法：POST

接口描述：提交消费公司信息，金额，商品信息，商家信息去开票(自动化开票，开发票API，只支持数电发票/数电票)。

请求头Header：

| 字段名   | 字段类型 | 是否必填| 默认值  | 描述                |
| -------- | -------- | -------- | ------- | ------------------ |
| authorization | string   | 是       |  | 身份令牌Token       |
| account | string   | 是       |   | 开票员账号(手机号码)       |

```
{
  "authorization": "eyJ0eXAiOiJKV1QiLCJyuGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczovL2FwaS5mYS1waWFv5689vbS92Mi9hdXRoL2xvZ2luIiwiaWF0IjoxNzI4Mjg3MTQ0LCJleHAiOjE3MjgzNzM1NDQsIm5iZiI6MTcyODI4NzE0NCwianRpIjoiRVR6WFBGRENBeGpSUHVzQiIsInN1YiI6IjEiLCJwcnYiOiI5M2JkY2M1OGRkMDFjZTM2ZWM1NmUzMmI1YmI1ODBkODMwMzJmZDE4IiwibnVtIjoiOTE1MDAxMTJNQURGQVE5SjJQ15645291cmNlIjowfQ.ubqFSNNrrFW8VQU5YTakF4LmsoMGXUoXCagClY3J6Wk",
  "account": "13266808205"
}
```


请求参数Body：

| <div style="width:200px">字段名</div> | <div style="width:60px">字段类型</div> | <div style="width:60px">是否必填</div> | <div style="width:50px">默认值</div> | 描述                                                                           |
|------------------------------------|----------------------------------|------------------------------------|-----------------------------------|------------------------------------------------------------------------------|
| customer                           | object                           | 是                                  |                                   | 消费者抬头信息                                                                      |
| &emsp;title                        | string                           | 是                                  |                                   | 密码，不为空                                                                       |
| &emsp;tax_number                   | string                           |                                    |                                   | 抬头名称                                                                         |
| &emsp;address                      | 	string                          |                                    |                                   | 单位地址                                                                         |
| &emsp;telephone                    | 	string                          |                                    |                                   | 单位电话(手机)号码                                                                   | 
| &emsp;bank_name                    | 	string                          |                                    |                                   | 开户行银行名称                                                                      | 
| &emsp;bank_account                 | 	int                             |                                    |                                   | 开户行银行账号                                                                      |
| fplxdm                             | 	string                          | 是                                  | 82                                | 发票类型代码 81增值税专用发票82普通发票                                                       | 
| hjje	                              | float                            |                                    |                                   | 合计金额                                                                         |                                                
| hjse	                              | float                            |                                    |                                   | 合计税额                                                                         |                                                
| jshj	                              | float                            | 是                                  |                                   | 价税合计(开票金额)                                                                   |                                         
| name	                              | string                           |                                    |                                   | 开票员姓名                                                                        |                                            
| bz	                                | string                           |                                    |                                   | 备注                                                                           |                                                 
| out_order_no                       | 	string                          | 是                                  |                                   | 商户订单号(商户系统内部订单号，只能是数字、大小写字母_-*且在同一个商户号下唯一)                                   | 
| products                           | 	array| 是                                  |                                   | 商品信息                                                                         |
| &emsp;hsbz                         | 	int | 是                                  | 1                                 | 含税标志 0不含税,1含税                                                                |
| &emsp;name                         | 	 string                           | 是                                  | 餐饮费                               | 商品名称                                                                         |
| &emsp;spbmjc                       | 	string | 是                                  | 餐饮服务                              | 商品编码简称([商品和服务税收分类编码表](https://api.fa-piao.com/storage/商品和服务税收分类编码表.xlsx)可查询) | 
| &emsp;spbm                         | 	 int | 是                                  | 3070401000000000000               | 商品编码([商品和服务税收分类编码表](https://api.fa-piao.com/storage/商品和服务税收分类编码表.xlsx)可查询)      | 
| &emsp;dj                           | 	 float|                                    |                                   | 单价                                                                           | 
| &emsp;spsl                         | 	float|                                    |                                   | 数量                                                                           |
| &emsp;dw                           | 	string|                                    |                                   | 计量单位                                                                         |
| &emsp;ggxh                         | 	string|                                    |                                   | 规格型号                                                                         |
| &emsp;lslbs                        | 	int| 是                                  | 0                                 | 零税率标识 0无,1出口免税和其他免税优惠政策,2不征增值税,3普通零税率                                        |
| &emsp;sl                           | 	float | 是                                  | 0.01                              | 税率                                                                           |
| &emsp;se                           | 	float|                                    |                                   | 税额                                                                           |
| &emsp;je                           | 	float|                                    |                                   | 金额                                                                           |
| hwys                               |	array |                                    |                                   | tdyslxDm为 04,09必填                                                            |
| &emsp;ysgjzl                       |	string|                                    |                                   | 运输工具种类 tdyslxDm为 04必填                                                        |  
| &emsp;ysgjhp	                      |string|                                    |                                   | 运输工具牌号 tdyslxDm为 04必填                                                        |
| &emsp;yshwmc	                      |string	|                                    |                                   | 运输货物名称 tdyslxDm为 04必填                                                        |
| &emsp;qyd	                         |string|                                    |                                   | 出发地 tdyslxDm为 04,09必填                                                        | 
| &emsp;ddd	                               |string|                                    |                                   | 到达地 tdyslxDm为 04,09必填                                                        |
| &emsp;cxr	                               |string|                                    |                                   | 出行人 tdyslxDm为09必填                                                            |
| &emsp;cxrq                               |	string|                                    |                                   | 出行日期yyyy-MM-dd tdyslxDm为09必填                                                 |
| &emsp;sfzjlx	                            |string	|                                    |                                   | 身份证件类型 tdyslxDm为09必填                                                         |
| &emsp;sfzjhm                             |	string	|                                    |                                   | 身份证件号码 tdyslxDm为09必填                                                         |
| &emsp;jtgjlx                             |	string|                                    |                                   | 交通工具类型 tdyslxDm为09必填                                                         |
| &emsp;dengj	                             |string	|                                    |                                   | 交通工具等级 tdyslxDm为09必填                                                         |
| address                            |	string|                                    |                                   | 邮箱                                                                           |


```json
{
    "customer": {
        "title": "个人",
        "tax_number": "",
        "address": "",
        "telephone": "",
        "bank_name": "",
        "bank_account": null
    },
    "fplxdm": "82",
    "hjje": null,
    "hjse": null,
    "jshj": null,
    "name": "",
    "bz": "",
    "out_order_no": "",
    "products": [
        {
            "hsbz": 1,
            "name": "餐饮费",
            "spbmjc": "餐饮服务",
            "spbm": 3070401000000000000,
            "dj": null,
            "spsl": null,
            "dw": "",
            "ggxh": "",
            "lslbs": 0,
            "sl": 0.01,
            "se": null,
            "je": null
        }
    ],
    "more": {
        "sfzsgmfyhzh": 0,
        "sfzsxsfyhzh": 0,
        "tdyslxDm": "",
        "fwfsd": "",
        "fullAddress": "",
        "kdsbz": null,
        "tdzzsxmbh": "",
        "jzxmmc": "",
        "zlrqq": "",
        "zlrqz": "",
        "cqzsh": "",
        "dw": "",
        "hwys": [
            {
                "ysgjzl": "",
                "ysgjhp": "",
                "yshwmc": "",
                "qyd": "",
                "ddd": "",
                "cxr": "",
                "cxrq": "",
                "sfzjlx": "",
                "sfzjhm": "",
                "jtgjlx": "",
                "dengj": ""
            }
        ]
    },
    "address": ""
}
```

响应结果：

| 字段名 | 字段类型   | 示例值 | 描述                          |
| ------ |--------|----| ----------------------------- |
| code   | int    | 200 | 响应状态码，值可能是200或1或2 |
| message    | string | 成功 | 响应消息                      |
| data   | object | [] |                           |

```json
{
    "code": 200,
    "message": "",
    "data": {
        "out_order_no": ""
    }
}
```

## 发票红冲

接口路径：/v3/invoice/red

请求方法：POST

接口描述：数电发票红冲

请求头Header：

| 字段名   | 字段类型 | 是否必填| 默认值  | 描述                |
| -------- | -------- | -------- | ------- | ------------------ |
| authorization | string   | 是       |  | 身份令牌Token       |
| account | string   | 是       |   | 开票员账号(手机号码)       |

```
{
  "authorization": "eyJ0eXAiOiJKV1QiLCJyuGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczovL2FwaS5mYS1waWFv5689vbS92Mi9hdXRoL2xvZ2luIiwiaWF0IjoxNzI4Mjg3MTQ0LCJleHAiOjE3MjgzNzM1NDQsIm5iZiI6MTcyODI4NzE0NCwianRpIjoiRVR6WFBGRENBeGpSUHVzQiIsInN1YiI6IjEiLCJwcnYiOiI5M2JkY2M1OGRkMDFjZTM2ZWM1NmUzMmI1YmI1ODBkODMwMzJmZDE4IiwibnVtIjoiOTE1MDAxMTJNQURGQVE5SjJQ15645291cmNlIjowfQ.ubqFSNNrrFW8VQU5YTakF4LmsoMGXUoXCagClY3J6Wk",
  "account": "13266808205"
}
```


请求参数Body：

| 字段名 | 字段类型 | 是否必填| 默认值 | 描述                                        |
|------------------------------------|----------------------------------|------------------------------------|-----|-------------------------------------------|
| out_order_no                       | string                           | 是                                  |     | 密码，不为空                                    |
| chyydm                   | string                           |      是                              | 01  | 原因代码 01开票有误,02销货退回,03服务中止,04销售折让                                      |




```json
{
    "out_order_no": "",
    "chyydm": "01"
}
```

响应结果：

| 字段名 | 字段类型   | 示例值 | 描述                          |
| ------ |--------|----| ----------------------------- |
| code   | int    | 200 | 响应状态码，值可能是200或1或2 |
| message    | string | 成功 | 响应消息                      |
| data   | object | [] |                           |

```json
{
    "code": 200,
    "message": "成功",
    "data": []
}
```

# 企业管理

## 登录

接口路径：/v3/merchant/login

请求方法：POST

接口描述：数电发票红冲


请求参数Body：

| 字段名 | 字段类型 | 是否必填 | 默认值                | 描述                                        |
|------------------------------------|----------------------------------|------|--------------------|-------------------------------------------|
| tax_number                       | string                           | 是    | 91500105MAC35KKR6G | 统一社会信用代码                                    |
| account                   | string                           | 否    |                    | 开票员账号手机号                                     |




```json
{
    "tax_number": "91500105MAC35KKR6G",
    "account": ""
}
```

响应结果：

| 字段名 | 字段类型   | 示例值 | 描述                          |
| ------ |--------|----| ----------------------------- |
| code   | int    | 200 | 响应状态码，值可能是200或1或2 |
| message    | string | 成功 | 响应消息                      |
| data   | object |  |                           |
| access_token   | string |  |   token                        |
| token_type   | string |  |     token类型                         |
| expires_in   | int |  |        有效期                   |

```json
{
    "code": 200,
    "message": "成功",
    "data": {
        "access_token": "",
        "token_type": "",
        "expires_in": null
    }
}
```

## 编辑企业

接口路径：/v3/invoice/edit

请求方法：POST

接口描述：编辑企业信息

请求头Header：

| 字段名   | 字段类型 | 是否必填| 默认值  | 描述                |
| -------- | -------- | -------- | ------- | ------------------ |
| authorization | string   | 是       |  | 身份令牌Token       |

```
{
  "authorization": "eyJ0eXAiOiJKV1QiLCJyuGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczovL2FwaS5mYS1waWFv5689vbS92Mi9hdXRoL2xvZ2luIiwiaWF0IjoxNzI4Mjg3MTQ0LCJleHAiOjE3MjgzNzM1NDQsIm5iZiI6MTcyODI4NzE0NCwianRpIjoiRVR6WFBGRENBeGpSUHVzQiIsInN1YiI6IjEiLCJwcnYiOiI5M2JkY2M1OGRkMDFjZTM2ZWM1NmUzMmI1YmI1ODBkODMwMzJmZDE4IiwibnVtIjoiOTE1MDAxMTJNQURGQVE5SjJQ15645291cmNlIjowfQ.ubqFSNNrrFW8VQU5YTakF4LmsoMGXUoXCagClY3J6Wk"
}
```


请求参数Body：

| 字段名 | 字段类型 | 是否必填 | 默认值 | 描述                                        |
|------------------------------------|----------------------------------|------|-----|-------------------------------------------|
|title|	string|      |     |营业执照名称  |
|address|	string|      |     |门店地址(发票) | 
|telephone|	string	|      |     |门店电话(发票)  |
|bank_name	|string	|      |     |开户行银行名称(发票) | 
|bank_account|	int|      |     |开户行银行账号(发票)|  
|province	|string		|      |     |省  |
|city	|string|      |     |市 | 
|area	|string|      |     |区 | 
|phone|	int|      |     |联系人手机号码|  
|call_back_url	|string|      |     |回调地址|




```json
{
    "title": "",
    "address": "",
    "telephone": "",
    "bank_name": "",
    "bank_account": null,
    "province": "",
    "city": "",
    "area": "",
    "phone": null,
    "call_back_url": ""
}
```

响应结果：

| 字段名 | 字段类型   | 示例值 | 描述                          |
| ------ |--------|----| ----------------------------- |
| code   | int    | 200 | 响应状态码，值可能是200或1或2 |
| message    | string | 成功 | 响应消息                      |
| data   | object | [] |                           |

```json
{
    "code": 200,
    "message": "成功",
    "data": []
}
```


