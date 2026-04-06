## 发票接口文档目录

这是一个用于对接发票接口(数电发票)的SDK，支持发票开具、红冲、查询等功能。
发票 电子发票/数电发票/全电发票/数电票/开票




| 语言 | SDK | GitHub | Gitee | 其他                                                                                                                                                                                                                                                                |
|-----|-----|--------|-------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Java| [central.sonatype.com](https://central.sonatype.com/artifact/io.github.fapiaoapi/invoice)  | [invoice-sdk-java](https://github.com/fapiaoapi/invoice-sdk-java) | [invoice-sdk-java](https://github.com/fapiaoapi/invoice-sdk-java) | java8-java16<br/>[开票demo](https://gitee.com/fapiaoapi/invoice/blob/master/BasicExample.java)<br/>[红冲demo](https://gitee.com/fapiaoapi/invoice/blob/master/RedInvoiceExample.java)<br/>[税额计算demo](https://gitee.com/fapiaoapi/invoice/blob/master/TaxExample.java) |
| PHP | [packagist.org](https://packagist.org/packages/tax/invoice)| [invoice-sdk-php](https://github.com/fapiaoapi/invoice-sdk-php) | [invoice-sdk-php](https://gitee.com/fapiaoapi/invoice-sdk-php) |                                                                                                                                                                                                                                                                   |
| Python | [pypi.org](https://pypi.org/project/tax-invoice) | [invoice-sdk-python](https://github.com/fapiaoapi/invoice-sdk-python) | [invoice-sdk-python](https://gitee.com/fapiaoapi/invoice-sdk-python) |                                                                                                                                                                                                                                                                   |
| Golang | [pkg.go.dev](https://pkg.go.dev/github.com/fapiaoapi/invoice-sdk-golang)| [invoice-sdk-golang](https://github.com/fapiaoapi/invoice-sdk-golang) | [invoice-sdk-golang](https://gitee.com/fapiaoapi/invoice-sdk-golang) |                                                                                                                                                                                                                                                                   |
| Nodejs| [www.npmjs.com](https://www.npmjs.com/package/tax-invoice)  | [invoice-sdk-nodejs](https://github.com/fapiaoapi/invoice-sdk-nodejs) | [invoice-sdk-nodejs](https://gitee.com/fapiaoapi/invoice-sdk-nodejs) |                                                                                                                                                                                                                                                                   |
| C# | [www.nuget.org](https://www.nuget.org/packages/Tax.Invoice) | [invoice-sdk-csharp](https://github.com/fapiaoapi/invoice-sdk-csharp) | [invoice-sdk-csharp](https://gitee.com/fapiaoapi/invoice-sdk-csharp) | C#8-C#11<br/>[开票demo](https://gitee.com/fapiaoapi/invoice/blob/master/BasicExample.cs)<br/>[红冲demo](https://gitee.com/fapiaoapi/invoice/blob/master/RedInvoiceExample.cs)<br/>[税额计算demo](https://gitee.com/fapiaoapi/invoice/blob/master/TaxExample.cs)           |
| C++ | |  |  | [apiClient.cpp](https://gitee.com/fapiaoapi/invoice/blob/master/apiClient.cpp)                                                                                                                                                                                    |
| postman|  |  |  | 下载<br/>[collection](fa-piao.com/fa-piao.postman_collection.json)<br/>和<br/>[environment](fa-piao.com/fa-piao.postman_environment.json)<br/>后导入postman可测试                                                                                                                                |
| html | |  |  | [前端模拟页面](fa-piao.com/fapiao.html)                                                                                                                                                                                                                                    |

[中文文档](https://fa-piao.com/doc.html "文档")

基础

* 获取授权
* 登录数电发票平台
* 获取人脸二维码
* 获取人脸二维码认证状态
* 获取认证状态

发票开具

* 数电蓝票开具接口
* 获取销项数电版式文件

发票红冲

* 申请红字前查蓝票信息接口
* 申请红字信息表
* 开负数发票



#### 接入说明

1. 在[开放平台](https://open.fa-piao.com)进行企业账号注册
2. 添加企业获得AppKey和AppSecret
3. 接入sdk调试

#### 公共请求Header参数

| 名称            | 类型     | 示例值                                                     | 必须 | 参数说明                                      |
| ------------- | ------ | ------------------------------------------------------- | -- | ----------------------------------------- |
| AppKey        | String | eyJf                                                    | 是  | 访问令牌                                      |
| TimeStamp     | String | 1743861024                                              | 是  | 时间戳（秒）10位                                 |
| RandomString  | String | YCBtd52riWWKz5i5x6FD                                    | 是  | 随机字符串 20位                                 |
| Sign          | String | 3E89AA3F89184CACDE46E80F013186DCa                       | 是  | 计算出来的签名值（具体计算方式，请到开发所需公共参数中进行查看）          |
| Authorization | String | eyJhbGciOiJI.eyJleHAiOjEIiwidHlwZSI6IjEifQ.p\_\_oAUSdVd | 是  | [获取授权](javascript:void\(0\) "获取授权")接口返回数据 |

#### 数电发票开票流程

![数电发票开票流程图](fapiao.png)

| 项目           | 说明内容                                                 | 备注                  |
| ------------ | ---------------------------------------------------- | ------------------- |
| 调用方式         | https                                                | POST方式提交            |
| 接口地址         | https://api.fa-piao.com/v5/enterprise/authorization |                     |
| 字符编码         | UTF-8                                                |                     |
| 接口描述         | 获取授权                                                 | Authorization token |
| Content-Type | form-data                                            |                     |

| 名称       | 类型     | 必须 | 参数描述       |
|----------| ------ |----|------------|
| nsrsbh   | String | 是  | 纳税人识别号     |
| type     | String | 否  | 账户类型 6基础 7标准 |
| username | String | 否  | 账号         |
| password | String | 否  | 密码         |

| 示例报文                        |
| --------------------------- |
| nsrsbh : 915101820724315989 |

| 字段    | 名称        | 类型     | 说明                                             |
| ----- | --------- | ------ | ---------------------------------------------- |
| code  | 接口返回code码 | int    | 成功：200 [code详情](javascript:void\(0\) "code详情") |
| msg   | 接口返回信息    | String | 成功/失败                                          |
| data  |           |        |                                                |
| token | 授权token   | String | 公共请求Header参数Authorization                      |

| 响应报文                                                                                                                                                                                                                                                                                                                                  |
| ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| ```
{
    "code": 200,
    "data": {
                "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE3NDQxMTQ4NTAsImlhdCI6MTc0NDExNDczMCwiaXNzIjoieXVlMDA1IiwibnNyc2JoIjoiOTI1MDAxMDNNQUQ3RjhIMTdEIiwidHlwZSI6IjEifQ.p__oAUSdVdA9inkuqvVYisjfxBIMzxGPkoMDuZ7hy04"
            },
    "msg": "成功",
    "total": 0
}
    
``` |

| 项目           | 说明内容                                             | 备注       |
| ------------ | ------------------------------------------------ | -------- |
| 调用方式         | https                                            | POST方式提交 |
| 接口地址         | https://api.fa-piao.com/v5/enterprise/loginDppt |          |
| 字符编码         | UTF-8                                            |          |
| 接口描述         | 登录数电发票平台                                         | 登录电票平台   |
| Content-Type | form-data                                        |          |

| 名称       | 类型     | 必须 | 参数描述                                                                           |
| -------- | ------ | -- | ------------------------------------------------------------------------------ |
| nsrsbh   | String | 是  | 纳税人识别号                                                                         |
| username | String | 是  | 用户电票平台账号                                                                       |
| password | String | 是  | 用户电票平台密码                                                                       |
| sms      | String | 否  | 验证码（第一次调用不传验证码，会发送验证码，第二次调用传验证码登录，会返回uuid）                                     |
| sf       | String | 否  | 电子税务局身份01:法定代表人，02:财务负责人，03:办税员，05:管理员,08:社保经办人，09:开票员，10:销售人员                 |
| ewmlx    | String | 否  | 1 税务人脸二维码登录，10 税务 app 扫码登录2 个税人脸二维码登录，3 个税 app 扫码确认登录                          |
| ewmid    | String | 否  | 第一次调用只传二维码类型(ewmlx)，会返回 ewmid 和二维码的 base64，第二次调用二维码类型跟第一次调用值必须一样，ewmid 使用第一次返回 |

| 示例报文                                                                              |
| --------------------------------------------------------------------------------- |
| nsrsbh : 915101820724315989username : 123213123password : 1231241241412421124sms: |

| 响应报文                                                                                                                                                                                                                                                                                                                 |
| -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| ```
{
    "code": 200,
    "msg": "验证码已发送到手机号：176****2696"
}
二维码第一次调用返回
{
	"code": 310,
	"msg": "已返回二维码信息",
	"data": {
		"ewmid": "bb885989d18e4852804643205a528380",
		"qrcode": "base64 二维码"
	}
}
输入验证码或者二维码认证，二次调用返回
{
	"code": 200,
	"msg": "成功",
	"data": "8a5152c171a04fcc90438305a7420c71",
	"total": 0
}
``` |

| 项目           | 说明内容                                              | 备注       |
| ------------ | ------------------------------------------------- | -------- |
| 调用方式         | https                                             | GET 方式提交 |
| 接口地址         | https://api.fa-piao.com/v5/enterprise/getFaceImg |          |
| 字符编码         | UTF-8                                             |          |
| 接口描述         | 获取人脸二维码                                           |          |
| Content-Type | form-data                                         |          |

| 字段       | 名称       | 必填 | 说明                                                          |
| -------- | -------- | -- | ----------------------------------------------------------- |
| username | 用户电票平台账号 | 否  | 电票平台账号，适用于一个税号多个账号，如果登录接口该参数有值，则取该参数的值，不传使用管理端平台默认维护的电票平台账号 |
| nsrsbh   | 纳税人识别号   | 是  | 纳税人识别号                                                      |
| type     | 类型       | 否  | 值为2获取个人所得税二维码，不传或者其他值都是税局app二维码                             |

| 示例报文                        |
| --------------------------- |
| nsrsbh : 915101820724315989 |

| 字段     | 名称        | 类型     | 说明                                                                |
| ------ | --------- | ------ | ----------------------------------------------------------------- |
| code   | 接口返回code码 | int    | 成功：200 [code详情](javascript:void\(0\) "code详情")                    |
| msg    | 接口返回信息    | String | 成功/失败                                                             |
| data   |           |        |                                                                   |
| rzid   | 认证id      | String | 认证id                                                              |
| nsrsbh | 纳税人识别号    | String | 纳税人识别号                                                            |
| ewm    | 二维码       | String | 二维码（type!=2时需要用户自己生成一个二维码把这些数据放进去）                                |
| slzt   | 受理状态      | String | 受理                                                                |
| ewmly  | 二维码来源     | String | 值为swj需要用税务局app扫码，并且需要用户自己生成二维码值为grsds需要用个人所得税app扫码，直接返回二维码的base64 |

| 响应报文                                                                                                                                                                                                                                                                                                                                                                                                                                  |
| ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| ```
正确状态报文：
{
	"code": 200,
	"msg": "成功",
	"data": {
		"rzid": "5246703dc22842b5a3d7826f375e6c7d",
		"nsrsbh": "9151123123122031211",
		"ewm": "qrcode_id=gYyixYMScMK4GQc2LfzqvKVnk33kJHs7p5wnpig3QdYFAdAvmDp7i7Yobk7zzkNM&areaPrefix=5100&interfaceCode=0004",
		"slzt": null,
		"emwly",
		"swj";
	},
	"total": 0
}
报错返回报文：  
{
	"code": 999,
	"msg": "失败",
	"data": "销方税号有误",
	"total": 0
}                  
                
``` |

| 项目           | 说明内容                                                | 备注       |
| ------------ | --------------------------------------------------- | -------- |
| 调用方式         | https                                               | GET 方式提交 |
| 接口地址         | https://api.fa-piao.com/v5/enterprise/getFaceState |          |
| 字符编码         | UTF-8                                               |          |
| 接口描述         | 获取人脸二维码认证状态                                         |          |
| Content-Type | form-data                                           |          |

| 字段       | 名称       | 必填 | 说明                                                          |
| -------- | -------- | -- | ----------------------------------------------------------- |
| username | 用户电票平台账号 | 否  | 电票平台账号，适用于一个税号多个账号，如果登录接口该参数有值，则取该参数的值，不传使用管理端平台默认维护的电票平台账号 |
| nsrsbh   | 纳税人识别号   | 是  | 纳税人识别号                                                      |
| rzid     | 认证id     | 是  | 认证id                                                        |
| type     | 类型       | 否  | 2查询个人所得税二维码认证状态，不填或其他值为税务app                                |

| 示例报文                                                               |
| ------------------------------------------------------------------ |
| nsrsbh : 915101820724315989rzid : 5246703dc22842b5a3d7826f375e6c7d |

| 字段     | 名称        | 类型     | 说明                                             |
| ------ | --------- | ------ | ---------------------------------------------- |
| code   | 接口返回code码 | int    | 成功：200 [code详情](javascript:void\(0\) "code详情") |
| msg    | 接口返回信息    | String | 成功/失败                                          |
| data   |           |        |                                                |
| rzid   | 认证id      | String | 认证id                                           |
| nsrsbh | 纳税人识别号    | String | 纳税人识别号                                         |
| ewm    | 二维码       | String | 二维码                                            |
| slzt   | 受理状态      | String | 受理状态:1未认证，2成功，3二维码过期                           |

| 响应报文                                                                                                                                                                                                                                                                                                     |
| -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| ```
正确状态报文：
{
	"code": 200,
	"msg": "成功",
	"data": {
		"rzid": null,
		"nsrsbh": "91510112332131211",
		"ewm": null,
		"slzt": "1"
	},
	"total": 0
}                   
报错返回报文：
{
	"code": 999,
	"msg": "失败",
	"data": "销方税号有误",
	"total": 0
}                                     
                
``` |

|   |
| - |

| 项目           | 说明内容                                                      | 备注             |
| ------------ | --------------------------------------------------------- | -------------- |
| 调用方式         | https                                                     | POST方式提交       |
| 接口地址         | https://api.fa-piao.com/v5/enterprise/queryFaceAuthState |                |
| 字符编码         | UTF-8                                                     |                |
| 接口描述         | 获取认证状态                                                    | 获取当前纳税人是否要人脸识别 |
| Content-Type | form-data                                                 |                |

| 名称       | 类型     | 必须 | 参数描述                                                        |
| -------- | ------ | -- | ----------------------------------------------------------- |
| username | String | 否  | 电票平台账号，适用于一个税号多个账号，如果登录接口该参数有值，则取该参数的值，不传使用管理端平台默认维护的电票平台账号 |
| nsrsbh   | String | 是  | 纳税人识别号                                                      |

| 示例报文                        |
| --------------------------- |
| nsrsbh : 91510113MA6739XPX2 |

| 字段   | 名称        | 类型     | 说明                                                                  |
| ---- | --------- | ------ | ------------------------------------------------------------------- |
| code | 接口返回code码 | int    | 成功：200 短信认证：420 人脸二维码认证：430 [code详情](javascript:void\(0\) "code详情") |
| msg  | 接口返回信息    | String | 成功/失败                                                               |

| 响应报文                                                                                                                                                                          |
| ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| ```
{
	"code": 200,
	"msg": "成功",
	"data": "eyJZampiIjoiMDEiLCJTeGxiIjoiMyIsIlNmc2wiOiJZIiwiSXRzU2NhbkZsYWciOiJOIn0=",
	"total": 0
}                    
                
``` |

| 项目           | 说明内容                                              | 备注        |
| ------------ | ------------------------------------------------- | --------- |
| 调用方式         | https                                             | POST 方式提交 |
| 接口地址         | https://api.fa-piao.com/v5/enterprise/blueTicket |           |
| 字符编码         | UTF-8                                             |           |
| 接口描述         | 数电蓝票开具接口                                          |           |
| Content-Type | form-data                                         |           |

| 字段                                                                                                       | 名称                     | 必填 | 类型         | 说明                                                                                                                                                                                                                                                |
| -------------------------------------------------------------------------------------------------------- | ---------------------- | -- | ---------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| fpqqlsh                                                                                                  | 发票请求流水号                | 否  | string     | 唯一值，如若不传自行生成                                                                                                                                                                                                                                      |
| username                                                                                                 | 用户电票平台账号               | 否  | String     | 电票平台账号，适用于一个税号多个账号，如果登录接口该参数有值，则取该参数的值，不传使用管理端平台默认维护的电票平台账号                                                                                                                                                                                       |
| fplxdm                                                                                                   | 发票类型代码                 | 是  | String     | 发票类型代码(详见[附件4](javascript:void\(0\)))                                                                                                                                                                                                             |
| tdyslxDm                                                                                                 | 特定要素类型代码               | 否  | String     | 特殊票种(详见[附件3](javascript:void\(0\)))                                                                                                                                                                                                               |
| kplx                                                                                                     | 开票类型                   | 是  | String     | 0正数发票                                                                                                                                                                                                                                             |
| qdbz                                                                                                     | 清单标志                   | 否  | String     | 开具 85，86 发票，并且商品明细大于 8 行传 1，代表是清单发票                                                                                                                                                                                                               |
| xhdwsbh                                                                                                  | 销方识别号                  | 是  | String     | 销方识别号                                                                                                                                                                                                                                             |
| xhdwmc                                                                                                   | 销方名称                   | 是  | String     | 销方名称                                                                                                                                                                                                                                              |
| xhdwdzdh                                                                                                 | 销方地址电话                 | 是  | String     | 销方地址电话                                                                                                                                                                                                                                            |
| xhdwyhzh                                                                                                 | 销方银行账户                 | 是  | String     | 销方银行账户                                                                                                                                                                                                                                            |
| ghdwsbh                                                                                                  | 购方税号                   | 否  | String     | 购方税号                                                                                                                                                                                                                                              |
| ghdwmc                                                                                                   | 购方名称                   | 是  | String     | 购方名称                                                                                                                                                                                                                                              |
| ghdwdzdh                                                                                                 | 购方地址电话                 | 否  | String     | 购方地址电话                                                                                                                                                                                                                                            |
| ghdwyhzh                                                                                                 | 购方银行账号                 | 否  | String     | 购方银行账号                                                                                                                                                                                                                                            |
| zsfs                                                                                                     | 征收方式                   | 否  | String     | 0：普通征税，2：差额征税全额开具，3：差额征税差额开具，默认为0                                                                                                                                                                                                                 |
| fyxm\[0]\[fphxz]                                                                                         | 发票行性质                  | 是  | String     | 0:正常行，1:折扣行，2:被折扣行                                                                                                                                                                                                                                |
| fyxm\[0]\[spmc]                                                                                          | 商品名称                   | 是  | String     | 商品名称                                                                                                                                                                                                                                              |
| fyxm\[0]\[ggxh]                                                                                          | 规格型号                   | 否  | String     | 规格型号,tdyslxDm为14时该字段为【车辆识别代号/车架号码】                                                                                                                                                                                                                |
| fyxm\[0]\[dw]                                                                                            | 单位                     | 否  | String     | 单位                                                                                                                                                                                                                                                |
| fyxm\[0]\[spsl]                                                                                          | 商品数量                   | 否  | BigDecimal | 商品数量                                                                                                                                                                                                                                              |
| fyxm\[0]\[dj]                                                                                            | 单价                     | 否  | BigDecimal | 单价                                                                                                                                                                                                                                                |
| fyxm\[0]\[je]                                                                                            | 金额                     | 是  | BigDecimal | 金额                                                                                                                                                                                                                                                |
| fyxm\[0]\[sl]                                                                                            | 税率                     | 是  | String     | 税率                                                                                                                                                                                                                                                |
| fyxm\[0]\[se]                                                                                            | 税额                     | 是  | BigDecimal | 税额                                                                                                                                                                                                                                                |
| fyxm\[0]\[hsbz]                                                                                          | 含税标志                   | 是  | String     | 0 不含税，1 含税                                                                                                                                                                                                                                        |
| fyxm\[0]\[spbm]                                                                                          | 商品编码                   | 是  | String     | 商品编码                                                                                                                                                                                                                                              |
| fyxm\[0]\[yhzcbs]                                                                                        | 优惠赠策标识                 | 否  | String     | 0未使用，1使用asDX                                                                                                                                                                                                                                      |
| fyxm\[0]\[lslbs]                                                                                         | 零税率标识                  | 否  | String     | 0代表正常税率，1 出口免税和其他免税优惠政策（免税），2 不征增值税（不征税），3 普通零税率（0%）                                                                                                                                                                                              |
| fyxm\[0]\[zzstsgl]                                                                                       | 增值税特殊管理                | 否  | String     | 增值税特殊管理(详见[附件2](javascript:void\(0\)))                                                                                                                                                                                                            |
| fyxm\[0]\[mtzlDm]                                                                                        | 煤炭种类代码                 | 否  | String     | 商品编码为1020101000000000000，1020102000000000000，1020199000000000000需要增加该节点，传参为0100政府保供煤，0201长协煤-协议期不足半年，0202长协煤-协议期在半年至一年之间，0203长协煤-协议期在一年至两年之间，0204长协煤-协议期在两年以上，0300市场煤                                                                             |
| hjje                                                                                                     | 合计金额                   | 是  | String     | 合计金额                                                                                                                                                                                                                                              |
| hjse                                                                                                     | 合计税额                   | 是  | String     | 合计税额                                                                                                                                                                                                                                              |
| jshj                                                                                                     | 加税合计                   | 是  | String     | 价税合计                                                                                                                                                                                                                                              |
| kce                                                                                                      | 扣除额                    | 否  | BigDecimal | 扣除额                                                                                                                                                                                                                                               |
| kpr                                                                                                      | 开票人                    | 否  | String     | 开票人                                                                                                                                                                                                                                               |
| skr                                                                                                      | 收款人                    | 否  | String     | 收款人                                                                                                                                                                                                                                               |
| fhr                                                                                                      | 复核人                    | 否  | String     | 复核人                                                                                                                                                                                                                                               |
| gfkhdh                                                                                                   | 购方电话                   | 否  | String     | 购方电话                                                                                                                                                                                                                                              |
| gfkhyx                                                                                                   | 购方邮箱                   | 否  | String     | 购方邮箱                                                                                                                                                                                                                                              |
| slsm                                                                                                     | 税率说明可用值2，3             | 否  | String     | 税率说明（小规模纳税人开具3 税率使用）固定传 2，解释说明：2：2023 年 1 月 1 日至 2027 年12 月31日，月销售额10万元以下(含本数)的小规模纳税人免征增值税，取得的适用 3%征收率的应税销售收入，可减按 1%征收率征收增值税。您如想享受上述政策，开具普通发票时应选择 1%征收率；3：您现在选择 3%征收率，是否因为前期已开具 3%征收率的发票，发生销售折让、中止或者退回等情形需要开具 3%征收率的红字发票或者开票有误需要重新开具3%征收率的发票，请确认。 |
| bz                                                                                                       | 备注                     | 否  | String     | 备注                                                                                                                                                                                                                                                |
| gfzrrbs                                                                                                  | 购方自然人标识                | 否  | String     | N：企业，Y：个人，不传默认为N                                                                                                                                                                                                                                  |
| xfzrrbs                                                                                                  | 销方自然人标识                | 否  | String     | 可用值N，Y（不传值为N）                                                                                                                                                                                                                                     |
| gfxxConfirm                                                                                              | 确认购方信息是否存在             | 否  | String     | 值为1是不确认购方信息可能会报（当前未查询到购买方纳税人信息，请确认是否继续开具 ），默认是确认意思是不管购方信息是否真实存在都进行开具                                                                                                                                                                              |
| spflxConfirm                                                                                             | 是否开启自然人校验              | 否  | String     | 1：开启空：不开启/继续开票                                                                                                                                                                                                                                    |
| gfzrrbs                                                                                                  | 购方自然人标识（N，Y）           | 否  | String     | 不传该节点或者节点为空为N，其他为Y，N代表不是自然人                                                                                                                                                                                                                       |
| xfzrrbs                                                                                                  | 销方自然人标识（N，Y）           | 否  | String     | 不传该节点或者节点为空为N，其他为Y，N代表不是自然人                                                                                                                                                                                                                       |
| sfzsgmfyhzh                                                                                              | 是否展示购方银行账号             | 否  | String     | 是否展示购方银行账号到备注里面y/Y展示，其他否                                                                                                                                                                                                                          |
| sfzsxsfyhzh                                                                                              | 是否展示销方银行账号             | 否  | String     | 是否展示销方银行账号到备注里面y/Y展示，其他否                                                                                                                                                                                                                          |
| 以上调用参数适用于普通业务增值税数电普票和数电专票发票开具，如果有其他特定业务或者特殊票种请根据实际业务需求添加以下内容                                             |                        |    |            |                                                                                                                                                                                                                                                   |
| 数电纸质发票添加以下字段                                                                                             |                        |    |            |                                                                                                                                                                                                                                                   |
| zpFppzDm                                                                                                 | 纸票票种代码                 | 否  | String     | fplxdm为85或86时必填（详见 [附件6](javascript:void\(0\))）                                                                                                                                                                                                   |
| zzfpdm                                                                                                   | 纸质发票代码                 | 否  | String     | fplxdm为85或86时必填，通过调用获取数电纸质发票代码号码接口获取                                                                                                                                                                                                              |
| zzfphm                                                                                                   | 纸质发票号码                 | 否  | String     | fplxdm为85或86时必填，1.23接口获取                                                                                                                                                                                                                          |
| 煤炭发票销售金额超过 1000 万添加以下节点                                                                                  |                        |    |            |                                                                                                                                                                                                                                                   |
| mtfrl                                                                                                    | 煤炭发热量                  | 否  | String     | 当前发票煤炭销售金额超过1000万必填 示例值：200                                                                                                                                                                                                                       |
| gjql                                                                                                     | 干基全硫                   | 否  | String     | 当前发票煤炭销售金额超过1000万必填 示例值：10                                                                                                                                                                                                                        |
| gzwhjhff                                                                                                 | 干燥无灰基挥发分               | 否  | String     | 当前发票煤炭销售金额超过1000万必填 示例值：20                                                                                                                                                                                                                        |
| 开具减按征税发票添加以下节点                                                                                           |                        |    |            |                                                                                                                                                                                                                                                   |
| jazslxDm                                                                                                 | 减按征收类型代码               | 否  | String     | 03销售自己使用过的固定资产，05住房租赁                                                                                                                                                                                                                             |
| 开具代办退税发票添加以下节点                                                                                           |                        |    |            |                                                                                                                                                                                                                                                   |
| cktslxDm                                                                                                 | 出口退税类型代码               | 否  | String     | 01代办退税专用                                                                                                                                                                                                                                          |
| 建筑和不动产添加以下节点                                                                                             |                        |    |            |                                                                                                                                                                                                                                                   |
| fwfsd                                                                                                    | 服务发生地                  | 否  | String     | tdyslxDm 为 03，05，06 必填省市区中间得加- 例如北京市-石景山区河北省-承德市-平泉县                                                                                                                                                                                              |
| fullAddress                                                                                              | 服务发生地详细地址              | 否  | String     | tdyslxDm为03，05，06选填                                                                                                                                                                                                                               |
| kdsbz                                                                                                    | 跨地（市）标志                | 否  | String     | tdyslxDm 为 03，05，06 必填，y 或者 Y 是，其他值或者默认不传都为否。当 tdyslxDm 为 03 并且 kdsbz 传是的时候需要调用查询跨区城涉税事项报验管理编号                                                                                                                                                    |
| tdzzsxmbh                                                                                                | 土地增值税项目编号              | 否  | String     | tdyslxDm为03，05，06选填                                                                                                                                                                                                                               |
| jzxmmc                                                                                                   | 建筑项目名称                 | 否  | String     | tdyslxDm为03必填                                                                                                                                                                                                                                     |
| kqysssxbgglbm                                                                                            | 跨区域涉税事项报验管理编号          | 否  | String     | tdyslxDm 为 03 并且 kdsbz 为是，该字段必填（查询跨区城涉税事项报验管理编号接口返回）                                                                                                                                                                                              |
| wqhtbabh                                                                                                 | 不动产单元代码/网签合同备案编号       | 否  | String     | tdyslxDm为05选填                                                                                                                                                                                                                                     |
| zlrqq                                                                                                    | 租赁日期起                  | 否  | String     | tdyslxDm06 必填 yyyy-MM-dd如果商品编码是用以下3040502020200000000格式填：yyyy-MM-dd HH:mm 实例 2024-10-26 00:00                                                                                                                                                     |
| zlrqz                                                                                                    | 租赁日期止                  | 否  | String     | tdyslxDm06 必填 yyyy-MM-dd如果商品编码是用以下3040502020200000000格式填：yyyy-MM-dd HH:mm 实例 2024-10-26 00:00                                                                                                                                                     |
| cph                                                                                                      | 车牌号                    | 否  | String     | tdyslxDm为06选填                                                                                                                                                                                                                                     |
| hdjsjg                                                                                                   | 核定计税价格                 | 否  | String     | tdyslxDm为05选填                                                                                                                                                                                                                                     |
| sjcjhsje                                                                                                 | 实际成交含税金额               | 否  | String     | tdyslxDm为05选填                                                                                                                                                                                                                                     |
| cqzsh                                                                                                    | 房屋产权证书/不动产权证号          | 否  | String     | tdyslxDm为05，06必填                                                                                                                                                                                                                                  |
| dw                                                                                                       | 单位                     | 否  | String     | tdyslxDm为05，06必填，详见[附件8](javascript:void\(0\))                                                                                                                                                                                                    |
| 拖拉机联合收割机添加以下节点                                                                                           |                        |    |            |                                                                                                                                                                                                                                                   |
| fdjhm                                                                                                    | 发动机号码                  | 否  | String     | tdyslxDm为13和sfyytljdj为Y必填                                                                                                                                                                                                                         |
| dphgzbh                                                                                                  | 底盘号/机架号                | 否  | String     | tdyslxDm为13和sfyytljdj为Y必填                                                                                                                                                                                                                         |
| sfyytljdj                                                                                                | 是否用于拖拉机登记              | 否  | Sring      | y或Y为是，如果为Y，商品名称只允许1行                                                                                                                                                                                                                              |
| 货运和旅客运输添加以下节点                                                                                            |                        |    |            |                                                                                                                                                                                                                                                   |
| hwys\[0]\[ysgjzl]                                                                                        | 运输工具种类                 | 否  | String     | tdyslxDm为04必填，详见[附件12](javascript:void\(0\))                                                                                                                                                                                                      |
| hwys\[0]\[ysgjhp]                                                                                        | 运输工具牌号                 | 否  | String     | tdyslxDm为04必填                                                                                                                                                                                                                                     |
| hwys\[0]\[yshwmc]                                                                                        | 运输货物名称                 | 否  | String     | tdyslxDm为04必填                                                                                                                                                                                                                                     |
| hwys\[0]\[qyd]                                                                                           | 出发地                    | 否  | String     | tdyslxDm为04，09必填                                                                                                                                                                                                                                  |
| hwys\[0]\[ddd]                                                                                           | 到达地                    | 否  | String     | tdyslxDm为04，09必填                                                                                                                                                                                                                                  |
| hwys\[0]\[cxr]                                                                                           | 出行人                    | 否  | String     | tdyslxDm为09必填                                                                                                                                                                                                                                     |
| hwys\[0]\[cxrq]                                                                                          | 出行日期                   | 否  | String     | tdyslxDm为09必填yyyy-MM-dd                                                                                                                                                                                                                           |
| hwys\[0]\[sfzjlx]                                                                                        | 身份证件类型                 | 否  | String     | tdyslxDm为09必填，详见[附件9](javascript:void\(0\))                                                                                                                                                                                                       |
| hwys\[0]\[sfzjhm]                                                                                        | 身份证件号码                 | 否  | String     | tdyslxDm为09必填                                                                                                                                                                                                                                     |
| hwys\[0]\[jtgjlx]                                                                                        | 交通工具类型                 | 否  | String     | tdyslxDm为09必填，详见[附件10](javascript:void\(0\))                                                                                                                                                                                                      |
| hwys\[0]\[dengj]                                                                                         | 交通工具等级                 | 否  | String     | tdyslxDm为09必填，详见[附件11](javascript:void\(0\))                                                                                                                                                                                                      |
| 差额征税添加以下节点                                                                                               |                        |    |            |                                                                                                                                                                                                                                                   |
| cepz\[0]\[pzlx]                                                                                          | 差额凭证类型                 | 否  | String     | zsfs为2/3时必填，详见[附件7](javascript:void\(0\))判断是否填写                                                                                                                                                                                                   |
| cepz\[0]\[fpdm]                                                                                          | 差额征税发票代码               | 否  | String     | zsfs为2/3根据[附件7](javascript:void\(0\))判断是否填写                                                                                                                                                                                                       |
| cepz\[0]\[fphm]                                                                                          | 差额征税数电发票号码             | 否  | String     | zsfs为2/3根据[附件7](javascript:void\(0\))判断是否填写                                                                                                                                                                                                       |
| cepz\[0]\[zzfphm]                                                                                        | 差额征税纸质发票号码             | 否  | String     | zsfs为2/3根据[附件7](javascript:void\(0\))判断是否填写                                                                                                                                                                                                       |
| cepz\[0]\[pzhm]                                                                                          | 差额征税凭证号码               | 否  | String     | zsfs为2/3根据[附件7](javascript:void\(0\))判断是否填写                                                                                                                                                                                                       |
| cepz\[0]\[kjrq]                                                                                          | 差额征收票据开具日期（yyyy-mm-dd） | 否  | String     | zsfs为2/3根据[附件7](javascript:void\(0\))判断是否填写                                                                                                                                                                                                       |
| cepz\[0]\[bz]                                                                                            | 备注                     | 否  | String     | zsfs为2/3根据[附件7](javascript:void\(0\))判断是否填写                                                                                                                                                                                                       |
| cepz\[0]\[bckcje]                                                                                        | 本次扣除额                  | 否  | String     | zsfs为2/3根据[附件7](javascript:void\(0\))判断是否填写                                                                                                                                                                                                       |
| cepz\[0]\[pzhjje]                                                                                        | 凭证合计                   | 否  | String     | zsfs为2/3根据[附件7](javascript:void\(0\))判断是否填写                                                                                                                                                                                                       |
| 经办人信息添加以下节点                                                                                              |                        |    |            |                                                                                                                                                                                                                                                   |
| gmfjbr                                                                                                   | 购方买经办人                 | 否  | String     | 购方买经办人                                                                                                                                                                                                                                            |
| jbrsfzjlx                                                                                                | 经办人身份证件类型              | 否  | String     | 详见[附件9](javascript:void\(0\))                                                                                                                                                                                                                     |
| jbrsfzjhm                                                                                                | 经办人身份证件号码              | 否  | String     | 经办人身份证件号码                                                                                                                                                                                                                                         |
| jbrgjlx                                                                                                  | 经办人国籍类型                | 否  | String     | 详见[附件13](javascript:void\(0\))                                                                                                                                                                                                                    |
| jbrzrrnsrsbh                                                                                             | 经办人自然人纳税人识别号           | 否  | String     | 经办人自然人纳税人识别号                                                                                                                                                                                                                                      |
| 销购方银行账户优化                                                                                                |                        |    |            |                                                                                                                                                                                                                                                   |
| gmfdz                                                                                                    | 购买方地址                  | 否  | String     | 购买方地址（注意：有gmfdz节点就优先去gmfdz的值，不取ghdwdzdh节点值）                                                                                                                                                                                                       |
| gmfdh                                                                                                    | 购买方电话                  | 否  | String     | 购买方电话（注意：有gmfdh节点就优先去gmfdh的值，不取ghdwdzdh节点值）                                                                                                                                                                                                       |
| gmfkhh                                                                                                   | 购买方开户行                 | 否  | String     | 购买方开户行（注意：有gmfkhh节点就优先去gmfkhh的值，不取ghdwyhzh节点值）                                                                                                                                                                                                    |
| gmfyhzh                                                                                                  | 购买方银行账号                | 否  | String     | 购买方银行账号（注意：有gmfyhzh节点就优先去gmfyhzh的值，不取ghdwyhzh节点值）                                                                                                                                                                                                 |
| xsfdz                                                                                                    | 销售方地址                  | 否  | String     | 销售方地址（注意：有xsfdz节点就优先去xsfdz的值，不取xhdwdzdh节点值）                                                                                                                                                                                                       |
| xsfdh                                                                                                    | 销售方电话                  | 否  | String     | 销售方电话（注意：有xsfdh节点就优先去xsfdh的值，不取xhdwdzdh节点值）                                                                                                                                                                                                       |
| xsfkhh                                                                                                   | 销售方开户行                 | 否  | String     | 销售方开户行（注意：有xsfkhh节点就优先去xsfkhh的值,不取xhdwyhzh节点值）                                                                                                                                                                                                    |
| xsfyhzh                                                                                                  | 销售方银行账号                | 否  | String     | 销售方银行账号（注意：有xsfyhzh节点就优先去xsfyhzh的值，不取xhdwyhzh节点值）                                                                                                                                                                                                 |
| 附加信息添加以下节点                                                                                               |                        |    |            |                                                                                                                                                                                                                                                   |
| fjys\[0]\[uuid]                                                                                          | 附加要素uuid               | 否  | String     | 1.30查询接口返回的id值，如果想添加附加信息该节点必传                                                                                                                                                                                                                     |
| fjys\[0]\[fjysz]                                                                                         | 附加要素数值                 | 否  | String     | 附加要素数值必填，跟当前uuid维护的附加要素数据类型匹配，date需要用户传yyyy-MM-dd格式                                                                                                                                                                                               |
| 农产品收购添加以下节点                                                                                              |                        |    |            |                                                                                                                                                                                                                                                   |
| 开具农产品收购，需要把销方信息和购方信息相反传                                                                                  |                        |    |            |                                                                                                                                                                                                                                                   |
| ncpsgzjlx                                                                                                | 销售方农产品收购证件类型           | 否  | String     | 如果开具农产品收购发票该节点必填详见[附件14](javascript:void\(0\))                                                                                                                                                                                                    |
| zrrzjhm                                                                                                  | 自然人证件号码                | 否  | String     | 开具农产品收购发票该节点选填不传默认取购方身份证号                                                                                                                                                                                                                         |
| zrrgjDm                                                                                                  | 自然人国家代码                | 否  | String     | 开具农产品收购发票该节点选填不传默认是中国国籍详见[附件17](javascript:void\(0\))                                                                                                                                                                                             |
| 开具农产品返回：                                                                                                 |                        |    |            |                                                                                                                                                                                                                                                   |
| 尊敬的纳税人，从贵公司开具的农产品收购发票分析，可能存在农产品收购发票开具不规范的情况，请按照《中华人民共和国增值税暂行条例》及其实施细则、《中华人民共和国发票管理办法》及其实施细则等规定开具农产品收购发票。 |                        |    |            |                                                                                                                                                                                                                                                   |
| ncpsrrzrcgzConfirm                                                                                       | 以上返回内容需增加此节点           | 否  | String     | 传 Y                                                                                                                                                                                                                                               |
| 报废产品收购特殊情况添加以下节点                                                                                         |                        |    |            |                                                                                                                                                                                                                                                   |
| bfcpsgXslz                                                                                               | 报废产品收购销售类型             | 否  | String     | 目前已知（1110701000000000000）商品编码需要传该节点，值为01或 02，默认为 0201：销售自己使用过的报废产品02：销售收购的报废产品                                                                                                                                                                    |
| ncpsrrzrcgzConfirm                                                                                       | 报废产品收购确认               | 否  | String     | 当开具接口返回 code=7706 时，该节点需要传参数为bf03-confirm                                                                                                                                                                                                         |
| gmfXzjd                                                                                                  | 销售方行政地址                | 否  | String     | 获取行政区省市代码接口选择的地址（Jdxzmc）开具报废产品收购票该节点必传                                                                                                                                                                                                            |
| gmfXzjdDm                                                                                                | 销售方行政代码                | 否  | String     | 获取行政区省市代码接口选择的代码（JdxzDm）开具报废产品收购票该节点必传                                                                                                                                                                                                            |
| gmfFullAddres                                                                                            | 销售方详细地址                | 否  | String     | 销售方详细地址开具报废产品收购票该节点必传                                                                                                                                                                                                                             |
| xzqhszDm                                                                                                 | 行政区省市代码                | 否  | String     | 获取行政区省市代码接口返回字段（XxzqhszDm）开具报废产品收购票该节点必传                                                                                                                                                                                                          |
| 不动产共同购买方添加以下节点                                                                                           |                        |    |            |                                                                                                                                                                                                                                                   |
| dfgtgmbq                                                                                                 | 是否共同购买发票标签             | 否  | String     | Y 为共同购买 N 反之                                                                                                                                                                                                                                      |
| gtgm\[0]\[gtgmf]                                                                                         | 共同购买方                  | 否  | String     | dfgtgmbq=Y 必填                                                                                                                                                                                                                                     |
| gtgm\[0]\[zjlx]                                                                                          | 证件类型                   | 否  | String     | dfgtgmbq=Y 必填证件类型 详细示例 [附件9](javascript:void\(0\))                                                                                                                                                                                                |
| gtgm\[0]\[zjhm]                                                                                          | 证件号码                   | 否  | String     | dfgtgmbq=Y 必填                                                                                                                                                                                                                                     |
| 不动产多行明细 tdyslxDm05 和 06 填入传入下面字段，上面的 fwfsd 字段会自动作废                                                       |                        |    |            |                                                                                                                                                                                                                                                   |
| bdc\[0]\[fwfsd]                                                                                          | 服务发生地                  | 否  | String     | tdyslxDm05，06 必填服务发生地                                                                                                                                                                                                                             |
| bdc\[0]\[fullAddress]                                                                                    | 明细地址                   | 否  | String     | tdyslxDm05，06 必填明细地址                                                                                                                                                                                                                              |
| bdc\[0]\[wqhtbabh]                                                                                       | 不动产单元代码                | 否  | String     | tdyslxDm05 选填不动产单元代码                                                                                                                                                                                                                              |
| bdc\[0]\[kdsbz]                                                                                          | 跨地市标志                  | 否  | String     | tdyslxDm05，06 必填跨地市标志                                                                                                                                                                                                                             |
| bdc\[0]\[tdzzsxmbh]                                                                                      | 土地增值税项目编号              | 否  | String     | tdyslxDm05 选填土地增值税项目编号                                                                                                                                                                                                                            |
| bdc\[0]\[hdjsjg]                                                                                         | 核定计税价格                 | 否  | String     | tdyslxDm05 选填核定计税价格                                                                                                                                                                                                                               |
| bdc\[0]\[sjcjhsje]                                                                                       | 实际成交含税金额               | 否  | String     | tdyslxDm05 选填实际成交含税金额                                                                                                                                                                                                                             |
| bdc\[0]\[cqzsh]                                                                                          | 产权证书号                  | 否  | String     | tdyslxDm05，06 必填没有则填无，产权证书号                                                                                                                                                                                                                       |
| bdc\[0]\[dw]                                                                                             | 单位                     | 否  | String     | tdyslxDm05，06 必填单位，详见[附件8](javascript:void\(0\))                                                                                                                                                                                                  |
| bdc\[0]\[zlrqq]                                                                                          | 租赁日期起                  | 否  | String     | tdyslxDm06 必填yyyy-MM-dd（2024-08-01）                                                                                                                                                                                                               |
| bdc\[0]\[zlrqz]                                                                                          | 租赁日期止                  | 否  | String     | tdyslxDm06 必填yyyy-MM-dd（2024-08-01）                                                                                                                                                                                                               |
| 数电纸质机动车发票添加以下节点                                                                                          |                        |    |            |                                                                                                                                                                                                                                                   |
| cpxh                                                                                                     | 厂牌型号                   | 否  | String     | 开具数电纸质机动车该节点必填使用查询机动车车架号是否合格接口返回数据                                                                                                                                                                                                                |
| cd                                                                                                       | 产地                     | 否  | String     | 开具数电纸质机动车该节点必填                                                                                                                                                                                                                                    |
| hgzh                                                                                                     | 合格证号                   | 否  | String     | 开具数电纸质机动车该节点必填使用查询机动车车架号是否合格接口返回数据                                                                                                                                                                                                                |
| jkzmsh                                                                                                   | 进口证明书号                 | 否  | String     | 开具数电纸质机动车该节点选填使用查询机动车车架号是否合格接口返回数据                                                                                                                                                                                                                |
| sjdh                                                                                                     | 商检单号                   | 否  | String     | 开具数电纸质机动车该节点选填                                                                                                                                                                                                                                    |
| fdjhm                                                                                                    | 发动机号码                  | 否  | String     | 开具数电纸质机动车该节点必填使用查询机动车车架号是否合格接口返回数据                                                                                                                                                                                                                |
| cjh                                                                                                      | 车辆识别代号/车架号码            | 否  | String     | 开具数电纸质机动车该节点必填                                                                                                                                                                                                                                    |
| cldw                                                                                                     | 吨位                     | 否  | String     | 开具数电纸质机动车该节点选填                                                                                                                                                                                                                                    |
| xcrs                                                                                                     | 限乘人数                   | 否  | String     | 开具数电纸质机动车该节点选填                                                                                                                                                                                                                                    |
| wspzhm                                                                                                   | 完税凭证号码                 | 否  | String     | 开具数电纸质机动车该节点选填                                                                                                                                                                                                                                    |
| cllxDm                                                                                                   | 车辆类型代码                 | 否  | String     | 开具数电纸质机动车该节点必填使用查询机动车车架号是否合格接口返回数据                                                                                                                                                                                                                |
| scqymc                                                                                                   | 生产企业名称                 | 否  | String     | 开具数电纸质机动车该节点必填使用查询机动车车架号是否合格接口返回数据                                                                                                                                                                                                                |
| jdctzclsbdhuuid                                                                                          | 机动车 uuid               | 否  | String     | 开具数电纸质机动车该节点必填使用查询机动车车架号是否合格接口返回数据                                                                                                                                                                                                                |
| zrrzjlxDm                                                                                                | 自然人认证类型代码              | 否  | String     | 开具数电纸质机动车该节点选填不传默认是身份证                                                                                                                                                                                                                            |
| zrrzjhm                                                                                                  | 自然人证件号码                | 否  | String     | 开具数电纸质机动车该节点选填不传默认取购方身份证号                                                                                                                                                                                                                         |
| zzrgjdm                                                                                                  | 自然人国家代码                | 否  | String     | 开具数电纸质机动车该节点选填不传默认是中国国籍[附件17](javascript:void\(0\))                                                                                                                                                                                               |
| 数电纸质二手车发票添加以下节点                                                                                          |                        |    |            |                                                                                                                                                                                                                                                   |
| tdyslxDm                                                                                                 | 特定要素类型代码               | 否  | String     | 开具数电纸质二手车车该节点必填正常开具51,反向开具52                                                                                                                                                                                                                      |
| cjh                                                                                                      | 车辆识别代号/车架号码            | 否  | String     | 开具数电纸质二手车车该节点必填                                                                                                                                                                                                                                   |
| cphm                                                                                                     | 车牌号码                   | 否  | String     | 车牌号码                                                                                                                                                                                                                                              |
| cpxh                                                                                                     | 厂牌型号                   | 否  | String     | 开具数电纸质二手车车该节点必填                                                                                                                                                                                                                                   |
| djzh                                                                                                     | 登记证号                   | 否  | String     | 开具数电纸质二手车车该节点必填                                                                                                                                                                                                                                   |
| zrdclglsmc                                                                                               | 转入地车辆管理所名称             | 否  | String     | 开具数电纸质二手车车该节点必填                                                                                                                                                                                                                                   |
| mfmc                                                                                                     | 卖方名称                   | 否  | String     | 开具数电纸质二手车车该节点必填                                                                                                                                                                                                                                   |
| mfsbh                                                                                                    | 卖方识别号                  | 否  | String     | 开具数电纸质二手车车该节点必填                                                                                                                                                                                                                                   |
| mfdz                                                                                                     | 卖方地址                   | 否  | String     | 开具数电纸质二手车车该节点必填                                                                                                                                                                                                                                   |
| mfdh                                                                                                     | 卖方电话                   | 否  | String     | 开具数电纸质二手车车该节点必填                                                                                                                                                                                                                                   |
| escyqrhyxz                                                                                               | 二手车企业性质                | 否  | String     | 开具数电纸质二手车车该节点必填07二手车市场，08二手车经销                                                                                                                                                                                                                    |
| zrrzjlxDm                                                                                                | 自然人认证类型代码              | 否  | String     | 开具数电纸质二手车车该节点选填购买方为自然人不传默认是身份证                                                                                                                                                                                                                    |
| zrrzjhm                                                                                                  | 自然人证件号码                | 否  | String     | 开具数电纸质二手车车该节点选填购买方为自然人不传默认取购方身份证号                                                                                                                                                                                                                 |
| zrrgjDm                                                                                                  | 自然人国家代码                | 否  | String     | 开具数电纸质二手车车该节点选填购买方为自然人 不传默认是中国国籍[附件17](javascript:void\(0\))                                                                                                                                                                                      |
| xsfZrrzjlxDm                                                                                             | 自然人认证类型代码              | 否  | String     | 开具数电纸质二手车车该节点选填销方为自然人，不传默认是身份证[附件9](javascript:void\(0\))                                                                                                                                                                                         |
| xsfZrrgjDm                                                                                               | 自然人国家代码                | 否  | String     | 开具数电纸质二手车车该节点选填销方为自然人，不传默认是中国国籍[附件17](javascript:void\(0\))                                                                                                                                                                                       |
| xsfZrrzjhm                                                                                               | 自然人证件号码                | 否  | String     | 开具数电纸质二手车车该节点选填销方为自然人，不传默认取购方身份证号                                                                                                                                                                                                                 |
| 代征车船税添加以下节点                                                                                              |                        |    |            |                                                                                                                                                                                                                                                   |
| bxdh                                                                                                     | 保险单号                   | 否  | String     | 开具代征车船税发票该节点必填                                                                                                                                                                                                                                    |
| cphcbdjh                                                                                                 | 车牌号/船舶登记号              | 否  | String     | 开具代征车船税发票该节点必填                                                                                                                                                                                                                                    |
| skssq                                                                                                    | 税款所属期                  | 否  | String     | 开具代征车船税发票该节点必填示例（2024-01 2024-04）                                                                                                                                                                                                                 |
| dsccsje                                                                                                  | 代收车船税金额                | 否  | String     | 开具代征车船税发票该节点必填只允许保留 2 位小数点                                                                                                                                                                                                                        |
| znj                                                                                                      | 滞纳金金额                  | 否  | String     | 开具代征车船税发票该节点必填只允许保留 2 位小数点                                                                                                                                                                                                                        |
| dsjehj                                                                                                   | 代收金额合计                 | 否  | String     | 开具代征车船税发票该节点必填只允许保留 2 位小数点                                                                                                                                                                                                                        |
| cjh                                                                                                      | 车辆识别代号/车架号码            | 否  | String     | 开具代征车船税发票该节点必填                                                                                                                                                                                                                                    |
| 支付信息增加节点：支持多行支付信息进行传参                                                                                    |                        |    |            |                                                                                                                                                                                                                                                   |
| zfxx\[0]\[zfqdDm]                                                                                        | 支付渠道代码                 | 否  | String     | 选择现金该节点传001详见[附件18](javascript:void\(0\))                                                                                                                                                                                                         |
| zfxx\[0]\[jydh]                                                                                          | 交易单号                   | 否  | String     | 交易单号，用户手动输入                                                                                                                                                                                                                                       |

|                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         |
| ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| 示例报文                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    |
| fpqqlsh:dyf14145263fplxdm:81kplx:1xhdwsbh:915101827712031211xhdwmc:武汉金拱门食品有限公司武汉站餐厅xhdwdzdh:1 1xhdwyhzh:1111 111ghdwsbh:202103030001777777ghdwmc:测试 2021ghdwdzdh:地址 123ghdwyhzh:中国光大银行股份有限公司苏州工业园区支 370401880075720619zsfs:0fyxm\[0]\[fphxz]:0fyxm\[0]\[spmc]:\*原电池\*CR1632（裸电池）fyxm\[0]\[spsm]:fyxm\[0]\[ggxh]:CR1632fyxm\[0]\[dw]:只fyxm\[0]\[spsl]:fyxm\[0]\[dj]:fyxm\[0]\[je]:0fyxm\[0]\[sl]:0fyxm\[0]\[se]:0.00fyxm\[0]\[hsbz]:0fyxm\[0]\[spbm]:1090413010300000000fyxm\[0]\[zxbm]:03fyxm\[0]\[yhzcbs]:fyxm\[0]\[lslbs]:1fyxm\[0]\[zzstsgl]:hjje:0hjse:0.00jshj:0kce:kpr:测试3tdyslxDm:gfkhdh:gfkhyx:slsm:bz:免税政策fyxm\[0]\[yhzcbs]:1fyxm\[0]\[lslbs]:1fyxm\[0]\[zzstsgl]:免税不征税fyxm\[0]\[yhzcbs]:1fyxm\[0]\[lslbs]:2fyxm\[0]\[zzstsgl]:不征税普通零税率fyxm\[0]\[lslbs]:3其他优惠政策（只需要替换zzstsgl节点即可）fyxm\[0]\[yhzcbs]:1fyxm\[0]\[lslbs]:fyxm\[0]\[zzstsgl]:简易征税代征车船税：bxdh:保险单号 123456cphcbdjh:车牌号京 A123321skssq:2024-01 2024-04dsccsje:1.17znj:20.18dsjehj:100.57cjh:车架号 123共同购买不动产：dfgtgmbq:"Y"gtgm\[0]\[gtgmf]:"某某某"gtgm\[0]\[zjlx]:"201"gtgm\[0]\[zjhm]:"513021\*\*\*\*\*\*\*\*908X"数电纸质机动车发票：fpqqlsh:dyf14145263fplxdm:87zzfpdm:zzfphm:zpFppzDm:kplx:0xhdwsbh:915101827712031211xhdwmc:武汉金拱门食品有限公司武汉站餐厅xhdwdzdh:1 1xhdwyhzh:1111 111ghdwsbh:202103030001777777ghdwmc:测试 2021zsfs:0fyxm\[0]\[fphxz]:0fyxm\[0]\[spmc]:\*机动车\*摩托车，排气量在 250 毫升以下（不含 250 毫升）fyxm\[0]\[ggxh]:fyxm\[0]\[dw]:fyxm\[0]\[spsl]:fyxm\[0]\[dj]:fyxm\[0]\[je]:99.01fyxm\[0]\[sl]:0.01fyxm\[0]\[se]:0.99fyxm\[0]\[hsbz]:0fyxm\[0]\[spbm]:1090312010000000000fyxm\[0]\[yhzcbs]:fyxm\[0]\[lslbs]:1fyxm\[0]\[zzstsgl]:hjje:99.01hjse:0.99jshj:100kpr:测试 3bz:一车一票cpxh:cd:hgzh:jkzmsh:sjdh:fdjhm:cjh:cldw:xcrs:wspzhm:cllxDm:scqymc:jdctzclsbdhuuid:zrrzjlxDm:zrrzjhm:zzrgjdm:数电纸质二手车发票（商品税率税额和合计税额固定传 0，二手车没有税额税率）fpqqlsh:dyf14145263fplxdm:88tdyslxDm:zzfpdm:zzfphm:zpFppzDm:kplx:0xhdwsbh:二手车市场识别号xhdwmc:二手车市场名称xhdwdzdh:二手车市场地址 电话xhdwyhzh:二手车市场银行 账号ghdwsbh:买方识别号ghdwmc:买方名称ghdwdzdh:买方地址 电话mfmc:卖方名称mfsbh:卖方识别号mfdz:卖方地址mfdh:卖方电话zrrzjlxDm：自然人认证类型代码zrrgjDm：自然人国家代码zrrzjhm：自然人证件号码zsfs:0fyxm\[0]\[fphxz]:0fyxm\[0]\[spmc]:\*机动车\*摩托车，排气量在 250 毫升以下（不含 250 毫升）fyxm\[0]\[ggxh]:fyxm\[0]\[dw]:fyxm\[0]\[spsl]:fyxm\[0]\[dj]:fyxm\[0]\[je]:100fyxm\[0]\[sl]:0fyxm\[0]\[se]:0fyxm\[0]\[hsbz]:0fyxm\[0]\[spbm]:1090312010000000000fyxm\[0]\[yhzcbs]:fyxm\[0]\[lslbs]:1fyxm\[0]\[zzstsgl]:hjje:100hjse:0jshj:100kpr:测试 3bz:cpxh:cjh:cphm:djzh:zrdclglsmc:二手车 tdyslxDm 反向开具报文其他请求参数一致，购买方和开票方一致，只填写卖方信息即可，反向开具只允许经销企业使用escyqrhyxz:08tdyslxDm:52xhdwsbh:二手车经销识别号xhdwmc:二手车经销名称xhdwdzdh:二手车经销地址 电话xhdwyhzh:二手车经销银行 账号ghdwsbh:二手车经销识别号ghdwmc:二手车经销名称ghdwdzdh:二手车经销地址 电话mfmc:卖方名称mfsbh:卖方识别号mfdz:卖方地址mfdh:卖方电话支付信息传参示例zfxx\[0]\[zfqdDm]:001zfxx\[0]\[jydh]:123zfxx\[1]\[zfqdDm]:002zfxx\[1]\[jydh]:456 |

| 字段           | 名称        | 类型     | 说明                                                    |
| ------------ | --------- | ------ | ----------------------------------------------------- |
| code         | 接口返回code码 | int    | 成功：200 [code详情](javascript:void\(0\) "code详情")        |
| msg          | 接口返回信息    | String | 成功/失败                                                 |
| data         | 接口返回具体信息  |        |                                                       |
| Fphm         | 发票号码      | String | 发票号码                                                  |
| Kprq         | 开票日期      | String | 开票日期                                                  |
| Gmfyx        | 购买方邮箱     | String | 购买方邮箱                                                 |
| GmfSsjswjgdm | 购买方税局机关代码 | String | 购买方税局机关代码                                             |
| ewm          | 发票打印的二维码  | String | 返回明文 格式：01,32,23922000000015868252,0.94,20230804,22D4 |
| zzfpdm       | 纸质发票代码    | String | 当发票流水号开具成功后并且是纸票才会返回                                  |
| zzfphm       | 纸质发票号码    | String | 当发票流水号开具成功后并且是纸票才会返回                                  |

| 响应报文                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
| ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| ```
正确状态报文：
{
	"code": 200,
	"msg": "成功",
	"data": {
		"Fphm": "22111111111111111180",
		"Kprq": "2022-11-28 15:28:11",
		"Gmfyx": null,
		"GmfSsjswjgdm": null
	},
	"total": 0
}
报错返回报文：
{
	"code": 999,
	"msg": "已过实人认证时间，请重新实人认证",
	"data": null,
	"total": 0
}
返回以下信息说明需要进行人脸认证了，可以解析 ewm 里边的内容进行扫脸：
{
	"code": 200,
	"msg": "成功",
	"message": "成功",
	"data": {
		"nsrsbh": "915333333333333333",
		"rzid": "6eb01d52edfd47878d6ed4487913a655",
		"slzt": null,
		"ewm": "qrcode_id=6O+iMFGsDxgC96nASdms0L5Lme6TP+bpbr/jIM3d0ZwFAdAvmDp7i7Yobk7zzkNM&areaPrefix=5100&interfaceCode=0004",
		"ewmly": "swj"
	},
	"total": 0
}
           
``` |

| 项目           | 说明内容                                                    | 备注        |
| ------------ | ------------------------------------------------------- | --------- |
| 调用方式         | https                                                   | POST 方式提交 |
| 接口地址         | https://api.fa-piao.com/v5/enterprise/getInvoicePdfOfd |           |
| 字符编码         | UTF-8                                                   |           |
| 接口描述         | 获取销项数电版式文件                                              | 销项版式获取    |
| Content-Type | form-data                                               |           |

| 字段       | 名称       | 必填 | 说明                                                          |
| -------- | -------- | -- | ----------------------------------------------------------- |
| downflag | 获取版式类型   | 是  | 1：PDF 2：OFD 3：XML 4：下载地址5：base64文件                          |
| nsrsbh   | 纳税人识别号   | 是  | 纳税人识别号                                                      |
| username | 用户电票平台账号 | 否  | 电票平台账号，适用于一个税号多个账号，如果登录接口该参数有值，则取该参数的值，不传使用管理端平台默认维护的电票平台账号 |
| fphm     | 发票号码     | 是  | 发票号码                                                        |
| kprq     | 开票日期     | 否  | 格式：yyyyMMddHHmmss                                           |
| addSeal  | 是否添加签章   | 否  | 默认不添加，1-添加，其余任意值-不添加                                        |

| 示例报文                                                                                     |
| ---------------------------------------------------------------------------------------- |
| fphm:22512000000000007325downflag:1nsrsbh:915101820724315989kprq:20230201120326addSeal:1 |

| 字段   | 名称            | 类型     | 说明                                                           |
| ---- | ------------- | ------ | ------------------------------------------------------------ |
| code | 接口返回code码     | int    | 成功：200 [code详情](javascript:void\(0\) "code详情")               |
| msg  | 接口返回信息        | String | 成功/失败或其他错误提示信息                                               |
| data | 返回base64加密字符串 | String | 1-3时返回base64加密字符串(xml为zip压缩包的加密字符串)4时返回 ofdUrl、pdfUrl、xmlUrl |

| ```
正确状态报文：
{
	"code": 200,
	"msg": "成功",
	"data": "base64加密字符串",
	"total": 0
}
{
	"code": 200,
	"msg": "成功",
	"data": {“
		ofdUrl“: "",
		“pdfUrl“: "",
		“xmlUrl“: ""
	}
	"total": 0
}
报错返回报文：
{
	"code": 999,
	"msg": "失败",
	"data": "total": 0
}
{
	"code": 234,
	"msg": "获取文件超时，请稍后重试。",
	"data": null,
	"total": 0
}                    
                
``` |
| ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |

|   |
| - |

| 项目           | 说明内容                                                | 备注        |
| ------------ | --------------------------------------------------- | --------- |
| 调用方式         | https                                               | POST 方式提交 |
| 接口地址         | https://api.fa-piao.com/v5/enterprise/retInviceMsg |           |
| 字符编码         | UTF-8                                               |           |
| 接口描述         | 数电申请红字前查蓝票信息接口                                      |           |
| Content-Type | form-data                                           |           |

| 字段           | 名称       | 必填 | 说明                                                          |   |
| ------------ | -------- | -- | ----------------------------------------------------------- | - |
| nsrsbh       | 数电企业税号   | 是  | 纳税人识别号                                                      |   |
| fphm         | 发票号码     | 是  | 数电票发票号码                                                     |   |
| sqyy         | 申请类型     | 是  | （暂时只支持）销方申请：2：销方全额红冲申请,3: 购方全额红冲                            |   |
| username     | 用户电票平台账号 | 否  | 电票平台账号，适用于一个税号多个账号，如果登录接口该参数有值，则取该参数的值，不传使用管理端平台默认维护的电票平台账号 |   |
| 购方申请红字增加以下节点 |          |    |                                                             |   |
| xhdwsbh      | 销方税号     | 是  | 原票销方税号                                                      |   |
| kprq         | 原发票开票日期  | 是  | yyyy-MM-dd HH:mm:ss                                         |   |
| tdyslxDm     | 特定要素类型代码 | 是  | 原票有就填                                                       |   |
| sqyy         | 申请原因     | 是  | 3购方全额红冲                                                     |   |
| 税控开数电票以下必填   |          |    |                                                             |   |
| fpdm         | 发票代码     | 是  | 发票代码                                                        |   |
| fplxdm       | 发票类型代码   | 是  | 026电子普票，028电子专票,007纸质普票，004纸质专票                             |   |

| 示例报文                                         |
| -------------------------------------------- |
| fphm: 2XXXXXXXXXXXXXXXXXXXxhdwsbh: 123123123 |

| 字段       | 名称        | 类型         | 说明                                             |
| -------- | --------- | ---------- | ---------------------------------------------- |
| code     | 接口返回code码 | int        | 成功：200 [code详情](javascript:void\(0\) "code详情") |
| msg      | 接口返回信息    | String     | 成功/失败                                          |
| data     | 接口返回具体信息  |            | 接口返回具体信息                                       |
| fphm     | 发票号码      | String     | 发票号码                                           |
| message  | message   | String     | message                                        |
| xhdwsbh  | 销方税号      | String     | 销方税号                                           |
| xhdwmc   | 销方名称      | String     | 销方名称                                           |
| ghdwsbh  | 购方税号      | String     | 购方税号                                           |
| ghdwmc   | 购方名称      | String     | 购方名称                                           |
| kprq     | 蓝票开票日期    | String     | 蓝票开票日期                                         |
| hjje     | 蓝票合计金额    | BigDecimal | 蓝票合计金额                                         |
| hjse     | 蓝票合计税额    | BigDecimal | 蓝票合计税额                                         |
| fplxdm   | 蓝票发票类型代码  | String     | 蓝票发票类型代码                                       |
| tdyslxDm | 特定要素类型代码  | String     | 特定要素类型代码                                       |
| jbr      |           | String     |                                                |
| mxzb     |           | List\<Map> |                                                |
| xh       | 序号        | String     | 序号                                             |
| spbm     | 商品编码      | String     | 商品编码                                           |
| spmc     | 商品名称      | String     | 商品名称                                           |
| ggxh     | 规格型号      | String     | 规格型号                                           |
| dw       | 单位        | String     | 单位                                             |
| spdj     | 商品单价      | String     | 商品单价                                           |
| spsl     | 商品数量      | String     | 商品数量                                           |
| je       | 金额        | String     | 金额                                             |
| sl       | 税率        | String     | 税率                                             |
| se       | 税额        | String     | 税额                                             |
| hsbz     | 含税标志      | String     | 含税标志                                           |
| yhzcbs   | 优惠赠策标识    | String     | 优惠赠策标识                                         |
| zzstsgl  | 增值税特殊管理   | String     | 增值税特殊管理                                        |
| lslbs    | 零税率标识     | String     | 零税率标识                                          |
| XfsytDm  | 消费税用途状态   | String     | 00 未勾选                                         |
| ZzsytDm  | 增值税用途状态   | String     | 01已确认03未勾选                                     |
| FprzztDm | 发票入账状态    | String     | 00 未入账                                         |

| 示例报文                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      |
| ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| ```
正确状态报文：
{
	"code": 200,
	"msg": "成功",
	"data": {
		"fphm": "22123123123123123115",
		"message": "成功,本张发票可以开负数!",
		"xhdwsbh": "91512332122222274",
		"xhdwmc": "成都 XXXXXXXXXXX 公司",
		"ghdwsbh": "9151012222222222",
		"ghdwmc": "昕诺 XXXXXXXXX 有限公司",
		"kprq": "2022-11-23 22:48:06",
		"hjje": -208.05,
		"hjse": -27.05,
		"fplxdm": "81",
		"tdyslxDm": null,
		"jbr": null,
		"mxzb": [{
			"xh": 1,
			"sl": 0.13,
			"dw": "个",
			"spmc": "*有色金属冶炼压延品*电气底座 3+P2 HLF_R",
			"se": -27.05,
			"je": -208.05,
			"spdj": "104.025",
			"ggxh": "444170080041",
			"spsl": "-2",
			"spbm": "1080310990000000000",
			"hsbz": "",
			"yhzcbs": "",
			"zzstsgl": "",
			"sqdh": "",
			"lslbs": ""
		}]
	}
}                    
报错返回报文：
{
	"code": 999,
	"msg": "未能判断当前纳税人与发票中身份,请检查"
}                    
                
``` |

| 项目           | 说明内容                                           | 备注        |
| ------------ | ---------------------------------------------- | --------- |
| 调用方式         | https                                          | POST 方式提交 |
| 接口地址         | https://api.fa-piao.com/v5/enterprise/hzxxbsq |           |
| 字符编码         | UTF-8                                          |           |
| 接口描述         | 申请红字信息表                                        |           |
| Content-Type | form-data                                      |           |

| 字段              | 名称          | 必填 | 说明                                                                                                                            |   |
| --------------- | ----------- | -- | ----------------------------------------------------------------------------------------------------------------------------- | - |
| xhdwsbh         | 销方税号        | 是  | 销方税号                                                                                                                          |   |
| yfphm           | 发票号码        | 是  | 申请红字信息表的发票号码                                                                                                                  |   |
| chyydm          | 申请红字信息表原因代码 | 是  | 01开票有误,02销货退回,03服务中止,04销售折让目前局端部分冲红只支持：02销货退回,03服务中止,04销售折让,商品服务编码仅为服务时红冲原因不允许选择"02销售退回"如原蓝字发票商品服务编码仅为货物或劳务时红冲原因不允许选择"03服务中止" |   |
| sqyy            | 申请类型        | 是  | 2：销方全额红冲,3: 购方全额红冲如果加上bfch节点，那么就是2：销方申请,3：购方申请                                                                                |   |
| sdfpbz          | 数电发票标志      | 否  | 只有数电纸票才需要该节点，蓝票是数电纸票，开具负数数电发票，该节点传1                                                                                           |   |
| username        | 用户电票平台账号    | 否  | 电票平台账号，适用于一个税号多个账号，如果登录接口该参数有值，则取该参数的值，不传使用管理端平台默认维护的电票平台账号                                                                   |   |
| 购方申请红字增加以下节点    |             |    |                                                                                                                               |   |
| nsrsbh          | 当前纳税人识别号    | 是  | 购方纳税人识别号                                                                                                                      |   |
| kprq            | 原发票开票日期     | 是  | yyyy-MM-dd HH:mm:ss                                                                                                           |   |
| tdyslxDm        | 特定要素类型代码    | 是  | 原票有就填                                                                                                                         |   |
| 税控开数电票以下必填      |             |    |                                                                                                                               |   |
| yfpdm           | 原发票代码       | 是  | 原发票代码                                                                                                                         |   |
| fplxdm          | 发票类型代码      | 是  | 026电子普票，028电子专票,007纸质普票，004纸质专票声明：目前只支持数电电票冲红税控电票数电电票冲红税控纸票                                                                   |   |
| 数电部分冲红以下必填      |             |    |                                                                                                                               |   |
| bfch            | 部分冲红标志      | 是  | 值为1是部分冲红                                                                                                                      |   |
| hjje            | 合计金额        | 是  | 部分冲红总金额                                                                                                                       |   |
| hjse            | 合计税额        | 是  | 部分冲红总税额                                                                                                                       |   |
| fyxm\[0]\[xh]   | 部分冲红商品明细    | 是  | 蓝字商品明细行数，例如开具蓝票第二行商品部分冲红，那么该参数值为2                                                                                             |   |
| fyxm\[0]\[spsl] | 部分冲红商品数量    | 否  | 冲红的商品数量，负数，冲红原因为04不需要该参数，如果原票有商品数量，那么开具负数该参数必填                                                                                |   |
| fyxm\[0]\[je]   | 部分冲红金额      | 是  | 冲红的金额，负数                                                                                                                      |   |
| fyxm\[0]\[se]   | 部分冲红税额      | 是  | 冲红的税额，负数                                                                                                                      |   |
| fyxm\[0]\[hsbz] | 含税标志        | 否  | 1 含税其余不含税，不传该参数为不含税                                                                                                           |   |
| jyrzzt          | 校验入账状态      | 是  | 如果是部分红冲发票，状态是未入账如果需要入账再申请红字 传1否则传空                                                                                            |   |

| 示例报文                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| 数电票销方申请红字信息表示例xhdwsbh:9151123123123123yfphm:123321chyydm:01sqyy:2税控纸票销方申请红字信息表示例xhdwsbh:9232123123123123X222chyydm:01sqyy:2yfphm:1111111yfpdm:03212312312304fplxdm:007数电纸票销方申请红字信息表（红字信息表申请成功后负数发票为数电电票 ）xhdwsbh:9151123123123123xhdwsbh:9151123123123123chyydm:01sqyy:2sdfpbz:1数电纸票销方申请红字信息表（红字信息表申请成功后负数发票依旧为数电纸票）xhdwsbh:9151123123123123yfphm:123321chyydm:01sqyy:2sdfpbz:0数电票销售申请部分冲红信息表xhdwsbh:9151123123123123yfphm:123321chyydm:01sqyy:2fyxm\[0]\[xh]:1fyxm\[0]\[spsl]:-0.5fyxm\[0]\[je]:-53.1fyxm\[0]\[se]:-6.9fyxm\[0]\[hsbz]:0hjje:-53.1hjse:-6.9bfch:1购方申请数电票红字信息表xhdwsbh:91440101753491772Bchyydm:01sqyy:3yfphm:21155896username:18202001640nsrsbh:91441721MA56R6304Q // 当前购方的纳税人识别号kprq:2023-09-11 11:49:06购方申请数电票红字信息表部分冲红xhdwsbh:91440101753491772Bchyydm:01sqyy:3yfphm:21155896username:18202001640nsrsbh:91441721MA56R6304Q// 当前购方的纳税人识别号kprq:2023-09-11 11:49:06fyxm\[0]\[xh]:1fyxm\[0]\[spsl]:-0.5fyxm\[0]\[je]:-53.1fyxm\[0]\[se]:-6.9fyxm\[0]\[hsbz]:0hjje:-53.1hjse:-6.9bfch:1需要入账再申请红字 传 1 否着传空jyrzzt：1清单发票部分红冲，申请红字信息表，需要额外在第一行 fyxm 里边固定传 xh 为 0，je 为红票的hjje，se 为红票的 hjse 这几个参数，参考示例如下：fyxm\[0]\[xh]:0fyxm\[0]\[je]:-1309.74fyxm\[0]\[se]:-170.26fyxm\[1]\[xh]:需要冲清单明细里面的第几行商品 |

| 字段      | 名称        | 类型     | 说明                                             |
| ------- | --------- | ------ | ---------------------------------------------- |
| code    | 接口返回code码 | int    | 成功：200 [code详情](javascript:void\(0\) "code详情") |
| msg     | 接口返回信息    | String | 成功/失败                                          |
| data    | 接口返回具体信息  |        |                                                |
| xxbbh   | 信息表编号     | String | 申请成功的红字信息表编号                                   |
| uuid    | 红字信息表业务id | String | 红字信息表业务id                                      |
| xxbztDm | 红字信息表状态代码 | String | （详见 [附件5](javascript:void\(0\))）               |
| xxbzt   | 红字信息表状态   | String | （详见 [附件5](javascript:void\(0\))）               |
| sqsj    | 红字信息表申请时间 | String | 红字信息表申请时间                                      |

| 响应报文                                                                                                                                                                                                                                                                                                                                                               |
| ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| ```
正确状态报文：
{
    "code": 200,
    "msg": "成功",
    "data": {
        "xxbbh": "51011321231231231101348",
        "uuid": "d3f9176a2asja2315bcf1483fc4fa3f85",
        "xxbzt":"无需确认",
        "xxbztDm":"01",
        "sqsj":"2023-12-06 11:32:35"
    }
}
报错返回报文：
{
    "code": 999,
    "msg": "未能判断当前纳税人与发票中身份,请检查"
}                    
                
``` |

| 项目           | 说明内容                                          | 备注        |
| ------------ | --------------------------------------------- | --------- |
| 调用方式         | https                                         | POST 方式提交 |
| 接口地址         | https://api.fa-piao.com/v5/enterprise/hzfpkj |           |
| 字符编码         | UTF-8                                         |           |
| 接口描述         | 数电票负数开具                                       |           |
| Content-Type | form-data                                     |           |

| 字段             | 名称       | 必填 | 说明                                                          |
| -------------- | -------- | -- | ----------------------------------------------------------- |
| fpqqlsh        | 发票请求流水号  | 是  | 发票请求流水号                                                     |
| username       | 用户电票平台账号 | 否  | 电票平台账号，适用于一个税号多个账号，如果登录接口该参数有值，则取该参数的值，不传使用管理端平台默认维护的电票平台账号 |
| xhdwsbh        | 销方税号     | 是  | 销方税号                                                        |
| tzdbh          | 红字信息表编号  | 是  | 红字信息表编号                                                     |
| yfphm          | 原发票号码    | 是  | 原发票号码                                                       |
| tdyslxDm       | 特殊票种     | 否  | 特殊票种（[附件3](javascript:void\(0\))）                           |
| 税控开数电票以下必填     |          |    |                                                             |
| yfpdm          | 原发票代码    | 是  | 原发票代码                                                       |
| 数电纸票红冲数电纸票以下必填 |          |    |                                                             |
| zpFppzDm       | 纸票票种代码   | 否  | 纸票票种代码（详见 [附件6](javascript:void\(0\))）                      |

| 示例报文                                                                                                                                                                                                                                                                                                                    |
| ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| fpqqlsh:uuidxhdwsbh:91510113MA6739XPX2xhdwdzdh:成都市青白江区清泉大道二段6668号附990号（欧洲产业城） 13540471704xhdwyhzh:工商银行成都华金大道支行 4402032009000058076ghdwsbh:91110108MA019HQRX9ghdwmc:重庆悦江河科技有限公司ghdwdzdh:北京市海淀区紫雀路33号院3号楼二层3201 010-62408884ghdwyhzh:北京市海淀区紫雀路33号院3号楼二层3201 010-62408884kpr:tzdbh:510123321yfphm:235123123123tdyslxDm:bz: |

| 字段      | 名称        | 类型     | 说明                                             |
| ------- | --------- | ------ | ---------------------------------------------- |
| code    | 接口返回code码 | int    | 成功：200 [code详情](javascript:void\(0\) "code详情") |
| msg     | 接口返回信息    | String | 成功/失败                                          |
| data    | 返回值       | Map    | 返回值                                            |
| fphm    | 发票号码      | String | 发票号码                                           |
| kprq    | 开票日期      | String | 开票日期                                           |
| xhdwsbh | 销方税号      | String | 销方税号                                           |
| ghdwsbh | 购方税号      | String | 购方税号                                           |
| ghdwmc  | 购方名称      | String | 购方名称                                           |
| xhdwmc  | 销方名称      | String | 销方名称                                           |
| jshj    | 价税合计      | String | 价税合计                                           |
| xxbbh   | 信息表编号     | String | 信息表编号                                          |
| zzfpdm  | 纸质发票代码    | String | 纸质发票代码                                         |
| zzfphm  | 纸质发票号码    | String | 纸质发票号码                                         |

| 响应报文                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           |
| -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| ```
正确状态报文：
{
	"code": 200,
	"msg": "成功",
	"data": {
		"fphm": "2351200012312312385",
		"kprq": "2023-05-17 14:50:55",
		"xhdwsbh": "9151023213121K",
		"ghdwsbh": "913212312312313D8JX2",
		"xhdwmc": "四川 XXXXX 科技有限公司",
		"ghdwmc": "明 XXXXX 有限公司",
		"jshj": -10.0,
		"xxbbh": "5106032312312312601543"
	}
}
红字信息表已开具过发票返回：
{
	"code": 310,
	"msg": "该蓝字发票 235122222285 和 3201552020202 信息表已开具过负数发票,不可重复开具",
	"data": {
		"fphm": "2351200012312312385",
		"kprq": "2023-05-17 14:50:55",
		"zzfpdm": "2221156", // 数电纸票返回
        "zzfphm": "13333"// 数电纸票返回
    }
}
报错返回报文：
{
    "code": 999,
    "msg": "失败"
}                    
                
``` |

| 项目           | 说明内容                                              | 备注                         |
| ------------ | ------------------------------------------------- | -------------------------- |
| 调用方式         | https                                             | POST方式提交                   |
| 接口地址         | https://api.fa-piao.com/v5/enterprise/changeUser |                            |
| 字符编码         | UTF-8                                             |                            |
| 接口描述         | 切换电子税务局账号                                         | 适用于相同省份相同电子税局登录账号 不同企业间的切换 |
| Content-Type | from-data                                         |                            |

| 名称        | 类型     | 必须 | 参数描述                                                                     |
| --------- | ------ | -- | ------------------------------------------------------------------------ |
| oldNsrsbh | String | 是  | 当前登录的纳税人识别号                                                              |
| newNsrsbh | String | 是  | 需要切换的纳税人识别号                                                              |
| username  | String | 是  | 电票平台账号                                                                   |
| sf        | String | 是  | 电子税务局身份，切换后纳税人的身份01:法定代表人，02:财务负责人，03:办税员，05:管理员,08:社保经办人，09:开票员，10:销售人员 |

| 示例报文：                                                                         |
| ----------------------------------------------------------------------------- |
| oldNsrsbh:92511521123123H6FnewNsrsbh:9251123123136PH3PXYusername:1231231sf:03 |

| 字段   | 名称        | 类型     | 说明                                             |
| ---- | --------- | ------ | ---------------------------------------------- |
| code | 接口返回code码 | int    | 成功：200 [code详情](javascript:void\(0\) "code详情") |
| msg  | 接口返回信息    | String | 成功/失败                                          |

| 响应报文                                                                                                                             |
| -------------------------------------------------------------------------------------------------------------------------------- |
| ```
{
    "code": 200,
    "msg": "成功"
}

{
    "code": 999,
    "msg": "电子税务局身份不可为空"
}                    
                
``` |

| 项目           | 说明内容                                              | 备注       |
| ------------ | ------------------------------------------------- | -------- |
| 调用方式         | https                                             | POST方式提交 |
| 接口地址         | https://api.fa-piao.com/v5/enterprise/creditLine |          |
| 字符编码         | UTF-8                                             |          |
| 接口描述         | 授信额度查询                                            |          |
| Content-Type | form-data                                         |          |

| 名称       | 类型     | 必须 | 参数描述                                                        |
| -------- | ------ | -- | ----------------------------------------------------------- |
| username | String | 否  | 电票平台账号，适用于一个税号多个账号，如果登录接口该参数有值，则取该参数的值，不传使用管理端平台默认维护的电票平台账号 |
| nsrsbh   | String | 是  | 纳税人识别号                                                      |

| 示例报文                        |
| --------------------------- |
| nsrsbh : 915101820724315989 |

| 字段      | 名称          | 类型     | 说明                                             |
| ------- | ----------- | ------ | ---------------------------------------------- |
| code    | 接口返回 code 码 | int    | 成功：200 [code详情](javascript:void\(0\) "code详情") |
| msg     | 接口返回信息      | String | 成功/失败                                          |
| sysxed  | 剩余授信额度      | String | 剩余多少授信额度-保留2 位小数                               |
| zsxed   | 总授信额度       | String | 总共拥有多少授信额度-保留2 为小数                             |
| kjlpzs  | 开具蓝票张数      | String | 开具蓝票张数                                         |
| fpejse  | 发票税额        | String | 发票税额-保留 2 为小数                                  |
| ysysxed | 已使用总授信额度    | String | 已使用总授信额度-保留2 为小数                               |
| fphjje  | 发票金额        | String | 发票金额-保留 2 为小数                                  |
| axkp    | 是否是按需开票     | String | 返回 Y 则是按需开票                                    |

| 响应报文                                                                                                                                                                                                                                                                                                             |
| ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| ```
{
	"code": 200,
	"msg": "成功",
	"data": {
		"sysxed": "剩余授信额度-保留 2 位小数",
		"zsxed": "总授信额度-保留 2 为小数",
		"kjlpzs": "开具蓝票张数",
		"fpejse": "发票税额-保留 2 为小数",
		"ysysxed": "已使用总授信额度-保留 2 为小数",
		"fphjje": "发票金额-保留 2 为小数",
		"axkp": "按需开票 Y-是否是按需开票"
	},
	"total": 0
}                    
                
``` |

## Code 码详情

| code 值 | message                 | 描述                         |
| ------ | ----------------------- | -------------------------- |
| 200    | 成功                      | 业务成功                       |
| 234    | 下载超时                    | 税局错误或下载超时，建议重试1\~2次        |
| 310    | 其他错误                    | 根据提示修改                     |
| 400    | 权限不足                    | 路径不存在/非法访问/权限不足            |
| 403    | 签名参数错误                  | 签名参数错误                     |
| 404    | 路径不存在                   | 路径不存在                      |
| 420    | 短信认证                    | 电票平台登陆失效，不需要验证码的省份直接调用登陆即可 |
| 430    | 人脸认证                    | 人脸二维码认证                    |
| 503    | 服务器繁忙请重试                | 服务器繁忙请重试                   |
| 2006   | 暂无此接口的权限                | 联系相关人员，申请开通调用接口权限          |
| 3002   | 当前 API 有效时间已超期，请先续费后使用！ | 联系相关人员，续期调用接口权限            |
| 999    | 请进行商户授权                 | 开放平台未绑定账号                  |
| 999    | token 获取失败，请联系管理员       | 检查开放平台账号是否正确               |
| 999    | 详见返回值                   | 详见返回值                      |
| 9002   | 未获取纳税人信息                | 管理端查询是否维护纳税人数电企业信息         |

## 附件

### 附件 1: 标准单位

* 分
* 角
* 元
* 千元
* 万元
* 亿元
* 两
* 斤
* 毫克
* 克
* 千克(公斤)
* 吨
* 千吨
* 万吨
* 磅
* 毫米
* 厘米
* 分米
* 米
* 千米（公里）
* 万米
* 码
* 英尺

- 平方毫米
- 平方厘米
- 平方分米
- 平方米
- 平方公里
- 公顷
- 亩
- 毫升(立方厘米)
- 公升(立方分米)
- 立方米（方）
- 千立方米
- 万立方米
- 亿立方米
- 份
- 组
- 本
- 箱
- 卷
- 瓦（W）
- 千瓦
- 万瓦
- 千伏安时
- 千瓦时（度）

* 千千瓦时（千度）
* 万千瓦时
* 位（Bit）
* 字节（B）
* 千字节（KB）
* 兆字节（MB）
* 吉字节（GB）
* 太字节（TB）
* 皮字节（PB）
* 个
* 支
* 只
* 把
* 条
* 台
* 架
* 辆
* 头
* 件
* 双
* 套
* 打
* 包

- 批
- 匹
- 部
- 座
- 艘
- 张
- 枝
- 根
- 块
- 卷
- 副
- 片
- 瓶
- 标准箱
- 万支
- 盒
- 桶
- 枚
- 次
- 项
- 株（棵）
- 户
- 点

* 袋
* 档
* 朵
* 顶
* 罐
* 回路
* 间
* 捆
* 例
* 令
* 箩
* 对
* 门
* 面
* 付
* 盘
* 具
* 圈
* 扇
* 束
* 刷
* 卡
* 听

- 筒
- 粒
- 串
- 盏
- 合
- 轴
- 尊
- 节
- 万双
- 板
- 版
- 盆
- 亿支
- 台（套）
- 万台
- 万块
- 万部
- 床
- 羽
- 克拉
- 盎司
- 秒
- 分钟
- 小时
- 日
- 月
- 季
- 年
- 分贝

### 附件 2: 增值税特殊管理

* 简易征收
* 稀土产品
* 免税
* 不征税
* 先征后退
* 100%先征后退
* 50%先征后退
* 按 3%简易征收
* 按 5%简易征收
* 按 5%简易征收减按 1.5%计征
* 即征即退 30%
* 即征即退 50%
* 即征即退 70%
* 即征即退 100%
* 超税负 3%即征即退
* 超税负 8%即征即退
* 超税负 12%即征即退
* 超税负 6%即征即退

### 附件 3: 特定要素类型代码

| 代码    | 说明                                   |
| ----- | ------------------------------------ |
| ""（空） | "非特殊票种"                              |
| "01"  | "成品油发票"（支持）没有特殊字段                    |
| "02"  | "稀土发票"（支持）没有特殊字段                     |
| "03"  | "建筑服务发票"（支持）有特殊字段                    |
| "04"  | "货物运输服务发票"（支持）有特殊字段                  |
| "05"  | "不动产销售服务发票"（支持）有特殊字段                 |
| "06"  | "不动产经营租赁服务"（支持）有特殊字段                 |
| "07"  | "代收车船税"（支持）有特殊字段                     |
| "08"  | "通行费"                                |
| "09"  | "旅客运输服务发票"（支持）有特殊字段                  |
| "10"  | "医疗服务（住院）发票"                         |
| "11"  | "医疗服务（门诊）发票"                         |
| "12"  | "自产农产品销售发票"（支持） 没有特殊字段               |
| "13"  | "拖拉机和联合收割机发票"（支持）有特殊字段               |
| "14"  | "机动车"（支持）没有特殊字段，商品明细规格型号，单位，单价，数量都必填 |
| "15"  | "二手车"                                |
| "16"  | "农产品收购发票"（支持） 有特殊字段（需要购销方颠倒）         |
| "17"  | "光伏收购发票"（支持）没有特殊字段（需要购销方颠倒）          |
| "18"  | "卷烟发票"                               |
| "19"  | "出口发票"                               |
| "20"  | "农产品"                                |
| "22"  | "航空运输电子客票行程单"                        |
| "24"  | "报废产品收购发票"（支持） 有特殊字段（需要购销方颠倒）        |
| "52"  | "反向开具"                               |

### 附件 4: 发票类型代码

| 代码  | 说明                  |
| --- | ------------------- |
| 51  | "数电发票（铁路电子客票）"      |
| 61  | "数电发票（航空运输电子客票行程单）" |
| 81  | "数电发票（增值税专用发票）"     |
| 82  | "数电发票（普通发票）"        |
| 84  | "数电票（二手车销售统一发票）"    |
| 85  | "数电纸质发票（增值税专用发票）"   |
| 86  | "数电纸质发票（普通发票）"      |
| 87  | "数电纸质发票（机动车发票）"     |
| 88  | "数电纸质发票（二手车发票）"     |
| 025 | "税控卷票"              |
| 026 | "增值税电子普票发票"         |
| 027 | "通行费发票"             |
| 028 | "增值税电子专用发票"         |
| 003 | "税控机动车发票"           |
| 004 | "增值税专用发票"           |
| 006 | "税控二手车销售统一发票"       |
| 007 | "增值税普通发票"           |

### 附件 5: 红字信息表状态代码

| 代码   | 说明               |
| ---- | ---------------- |
| "01" | "无需确认"           |
| "02" | "销方录入待购方确认"      |
| "03" | "购方录入待销方确认"      |
| "04" | "购销双方已确认"        |
| "05" | "作废(销方录入购方否认"    |
| "06" | "作废(购方录入销方否认"    |
| "07" | "作废(超 72 小时未确认)" |
| "08" | "作废(发起方已撤销)"     |
| "09" | "作废(确认后撤销)"      |
| "10" | "作废(异常凭证)"       |

### 附件 6: 数电纸质发票票种代码

#### 以下发票票种代码只做参考，调用接口时请以查询可开具纸票票种代码接口返回信息为准：

#### 数电纸质普通发票

| 代码           | 说明                      |
| ------------ | ----------------------- |
| 04           | 2016 版增值税普通发票（二联折叠票）    |
| 05           | 2016 版增值税普通发票（五联折叠票）    |
| 000008101200 | 2008 版增值税普通发票（二联无金额限制版） |
| 000008101500 | 2008 版增值税普通发票（五联无金额限制版） |

#### 数电纸质专用发票

| 代码   | 说明                  |
| ---- | ------------------- |
| 1130 | 增值税专用发票（中文三联无金额限制版） |
| 1160 | 增值税专用发票(中文六联无金额限制版) |

#### 数电纸质机动车发票

| 代码           | 说明                  |
| ------------ | ------------------- |
| 000008102600 | 机动车销售统一发票（六联无金额限制版） |

#### 数电纸质二手车发票

| 代码 | 说明              |
| -- | --------------- |
| 17 | 2017 版二手车销售统一发票 |

### 附件 7: 差额凭证类型

zsfs 为 2 时可以不填写差额凭证，为 3 必填用户需要传入数值

差额征税商品只允许为 1 条凭证合计金额必传

差额凭证类型为 01 时发票号码，开票日期必填

差额凭证类型为 02，03，04 时发票代码，纸质发票号码，开具日期必填

差额凭证类型为 08，09 时差额备注必填

| 代码 | 说明      |
| -- | ------- |
| 01 | 数电发票    |
| 02 | 增值税专用发票 |
| 03 | 增值税普通发票 |
| 04 | 营业税发票   |
| 05 | 财政票据    |
| 06 | 法院裁决书   |
| 07 | 契税完税凭证  |
| 08 | 其他发票类   |
| 09 | 其他扣除凭证  |

### 附件 8: 不动产单位

用户需要传入数值

| 代码 | 说明   |
| -- | ---- |
| 1  | 平方千米 |
| 2  | 平方米  |
| 3  | 孔公里  |
| 4  | 公顷   |
| 5  | 亩    |
| 6  | h ㎡  |
| 7  | k ㎡  |
| 8  | ㎡    |
| 9  | 米    |

### 附件 9: 身份证类型

用户需要传入数值

| 代码  | 说明                     |
| --- | ---------------------- |
| 101 | 组织机构代码证                |
| 102 | 营业执照                   |
| 103 | 税务登记证                  |
| 199 | 其他单位证件                 |
| 201 | 居民身份证                  |
| 202 | 军官证                    |
| 203 | 武警警官证                  |
| 204 | 士兵证                    |
| 205 | 军队离退休干部证               |
| 206 | 残疾人证                   |
| 207 | 残疾军人证（1-8 级）           |
| 208 | 外国护照                   |
| 210 | 港澳居民来往内地通行证            |
| 212 | 中华人民共和国往来港澳通行证         |
| 213 | 台湾居民来往大陆通行证            |
| 214 | 大陆居民往来台湾通行证            |
| 215 | 外国人居留证                 |
| 216 | 外交官证                   |
| 217 | 使（领事）馆证                |
| 218 | 海员证                    |
| 219 | 香港永久性居民身份证             |
| 220 | 台湾身份证                  |
| 221 | 澳门特别行政区永久性居民身份证        |
| 222 | 外国人身份证件                |
| 224 | 就业失业登记证                |
| 225 | 退休证                    |
| 226 | 离休证                    |
| 227 | 中国护照                   |
| 228 | 城镇退役士兵自谋职业证            |
| 229 | 随军家属身份证明               |
| 230 | 中国人民解放军军官转业证书          |
| 231 | 中国人民解放军义务兵退出现役证        |
| 232 | 中国人民解放军士官退出现役证         |
| 233 | 外国人永久居留身份证（外国人永久居留证）   |
| 234 | 就业创业证                  |
| 235 | 香港特别行政区护照              |
| 236 | 澳门特别行政区护照              |
| 237 | 中华人民共和国港澳居民居住证         |
| 238 | 中华人民共和国台湾居民居住证         |
| 239 | 《中华人民共和国外国人工作许可证》（A 类） |
| 240 | 《中华人民共和国外国人工作许可证》（B 类） |
| 241 | 《中华人民共和国外国人工作许可证》（C 类） |
| 291 | 医学出生证明                 |
| 299 | 其他个人证件                 |

### 附件 10: 出行工具类型

用户需要传入数值

| 代码 | 说明   |
| -- | ---- |
| 1  | 飞机   |
| 2  | 火车   |
| 3  | 长途汽车 |
| 4  | 公共交通 |
| 5  | 出租车  |
| 6  | 汽车   |
| 7  | 船舶   |
| 8  | 其他   |

### 附件 11: 出行工具类型等级

用户需要传入数值

出行类型为 1，选 1-3，必填

出行类型为 2，选 4-7，必填

出行类型为 7，选 8-10，必填

出行类型不是 1，2，7 需要用户直接输入并非选填

| 代码 | 说明        |
| -- | --------- |
| 1  | 公务舱       |
| 2  | 头等舱       |
| 3  | 经济舱       |
| 4  | 一等座       |
| 5  | 二等座       |
| 6  | 软席（软座、软卧） |
| 7  | 硬席（硬座、硬卧） |
| 8  | 一等舱       |
| 9  | 二等舱       |
| 10 | 三等舱       |

### 附件 12: 运输工具种类

用户需要传入数值

| 代码 | 说明     |
| -- | ------ |
| 1  | 铁路运输   |
| 2  | 公路运输   |
| 3  | 水路运输   |
| 4  | 航空运输   |
| 5  | 管道运输   |
| 6  | 其他运输工具 |

### 附件 13: 国籍类型

用户需要传入数值

| 代码  | 说明        |
| --- | --------- |
| 156 | 中华人民共和国   |
| 158 | 中国台湾      |
| 344 | 中国香港特别行政区 |
| 446 | 中国澳门特别行政区 |

### 附件 14: 农产品收购证件类型

用户需要传入数值

| 代码  | 说明                   |
| --- | -------------------- |
| 103 | 税务登记证                |
| 201 | 居民身份证                |
| 208 | 外国护照                 |
| 210 | 港澳居民来往内地通行证          |
| 213 | 台湾居民来往大陆通行证          |
| 215 | 外国人居留证               |
| 219 | 香港永久性居民身份证           |
| 221 | 澳门特别行政区永久性居民身份证      |
| 233 | 外国人永久居留身份证（外国人永久居留证） |
| 299 | 其他个人证件               |

### 附件 15: 发票状态类型

用户需要传入数值

| 代码 | 说明   |
| -- | ---- |
| 00 | 正数发票 |
| 01 | 负数发票 |
| 02 | 空白作废 |
| 03 | 正废发票 |
| 04 | 负废发票 |

### 附件 16: 申请额度审核状态

| 代码 | 说明    |
| -- | ----- |
| 01 | 已提交   |
| 02 | 已撤销   |
| 03 | 受理中   |
| 04 | 不予受理  |
| 05 | 待补正   |
| 06 | 审核中   |
| 07 | 审核通过  |
| 08 | 审核不通过 |

### 附件 17: 国籍

用户需要传入数值

| 代码  | 说明               |
| --- | ---------------- |
| 156 | 中华人民共和国          |
| 158 | 中国台湾             |
| 344 | 中国香港特别行政区        |
| 446 | 中国澳门特别行政区        |
| 004 | 阿富汗              |
| 008 | 阿尔巴尼亚共和国         |
| 010 | 南极洲              |
| 012 | 阿尔及利亚民主人民共和国     |
| 016 | 美属萨摩亚            |
| 020 | 安道尔公国            |
| 024 | 安哥拉共和国           |
| 028 | 安提瓜和巴布达          |
| 031 | 阿塞拜疆共和国          |
| 032 | 阿根廷共和国           |
| 036 | 澳大利亚联邦           |
| 040 | 奥地利共和国           |
| 044 | 巴哈马联邦            |
| 048 | 巴林国              |
| 050 | 孟加拉人民共和国         |
| 051 | 亚美尼亚共和国          |
| 052 | 巴巴多斯             |
| 056 | 比利时王国            |
| 060 | 百慕大              |
| 064 | 不丹王国             |
| 068 | 玻利维亚共和国          |
| 070 | 波斯尼亚和黑塞哥维那       |
| 072 | 博茨瓦纳共和国          |
| 074 | 布维岛              |
| 076 | 巴西联邦共和国          |
| 084 | 伯利兹              |
| 086 | 英属印度洋领地          |
| 090 | 所罗门群岛            |
| 092 | 英属维尔京群岛          |
| 096 | 文莱达鲁萨兰国          |
| 100 | 保加利亚共和国          |
| 104 | 缅甸联邦             |
| 108 | 布隆迪共和国           |
| 112 | 白俄罗斯共和国          |
| 116 | 柬埔寨王国            |
| 120 | 喀麦隆共和国           |
| 124 | 加拿大              |
| 132 | 佛得角共和国           |
| 136 | 开曼群岛             |
| 140 | 中非共和国            |
| 144 | 斯里兰卡民主社会主义共和国    |
| 148 | 乍得共和国            |
| 152 | 智利共和国            |
| 162 | 圣诞岛              |
| 166 | 科科斯（基林）群岛        |
| 170 | 哥伦比亚共和国          |
| 174 | 科摩罗伊斯兰联邦共和国      |
| 175 | 马约特              |
| 178 | 刚果共和国            |
| 180 | 刚果民主共和国          |
| 184 | 库克群岛             |
| 188 | 哥斯达黎加共和国         |
| 191 | 克罗地亚共和国          |
| 192 | 古巴共和国            |
| 196 | 塞浦路斯共和国          |
| 203 | 捷克共和国            |
| 204 | 贝宁共和国            |
| 208 | 丹麦王国             |
| 212 | 多米尼克国            |
| 214 | 多米尼加共和国          |
| 218 | 厄瓜多尔共和国          |
| 222 | 萨尔瓦多共和国          |
| 226 | 赤道几内亚共和国         |
| 231 | 埃塞俄比亚联邦民主共和国     |
| 232 | 厄立特里亚国           |
| 233 | 爱沙尼亚共和国          |
| 234 | 法罗群岛             |
| 238 | 福克兰群岛（马尔维纳斯）     |
| 239 | 南乔治亚岛和南桑德韦奇岛     |
| 242 | 斐济群岛共和国          |
| 246 | 芬兰共和国            |
| 250 | 法兰西共和国           |
| 254 | 法属圭亚那            |
| 258 | 法属波利尼西亚          |
| 260 | 法属南部领地           |
| 262 | 吉布提共和国           |
| 266 | 加蓬共和国            |
| 268 | 格鲁吉亚             |
| 270 | 冈比亚共和国           |
| 275 | 巴勒斯坦国            |
| 276 | 德意志联邦共和国         |
| 288 | 加纳共和国            |
| 292 | 直布罗陀             |
| 296 | 基里巴斯共和国          |
| 300 | 希腊共和国            |
| 304 | 格陵兰              |
| 308 | 格林纳达             |
| 312 | 瓜德罗普             |
| 316 | 关岛               |
| 320 | 危地马拉共和国          |
| 324 | 几内亚共和国           |
| 328 | 圭亚那合作共和国         |
| 332 | 海地共和国            |
| 334 | 赫德岛和麦克唐纳岛        |
| 336 | 梵蒂冈城国            |
| 340 | 洪都拉斯共和国          |
| 348 | 匈牙利共和国           |
| 352 | 冰岛共和国            |
| 356 | 印度共和国            |
| 360 | 印度尼西亚共和国         |
| 364 | 伊朗伊斯兰共和国         |
| 368 | 伊拉克共和国           |
| 372 | 爱尔兰              |
| 376 | 以色列国             |
| 380 | 意大利共和国           |
| 384 | 科特迪瓦共和国          |
| 388 | 牙买加              |
| 392 | 日本国              |
| 398 | 哈萨克斯坦共和国         |
| 400 | 约旦哈希姆王国          |
| 404 | 肯尼亚共和国           |
| 408 | 朝鲜民主主义人民共和国      |
| 410 | 大韩民国             |
| 414 | 科威特国             |
| 417 | 吉尔吉斯共和国          |
| 418 | 老挝人民民主共和国        |
| 422 | 黎巴嫩共和国           |
| 426 | 莱索托王国            |
| 428 | 拉脱维亚共和国          |
| 430 | 利比里亚共和国          |
| 434 | 大阿拉伯利比亚人民社会主义民众国 |
| 438 | 列支敦士登公国          |
| 440 | 立陶宛共和国           |
| 442 | 卢森堡大公国           |
| 450 | 马达加斯加共和国         |
| 454 | 马拉维共和国           |
| 458 | 马来西亚             |
| 462 | 马尔代夫共和国          |
| 466 | 马里共和国            |
| 470 | 马耳他共和国           |
| 474 | 马提尼克             |
| 478 | 毛里塔尼亚伊斯兰共和国      |
| 480 | 毛里求斯共和国          |
| 484 | 墨西哥合众国           |
| 492 | 摩纳哥公国            |
| 496 | 蒙古国              |
| 498 | 摩尔多瓦共和国          |
| 499 | 黑山               |
| 500 | 蒙特塞拉特            |
| 504 | 摩洛哥王国            |
| 508 | 莫桑比克共和国          |
| 512 | 阿曼苏丹国            |
| 516 | 纳米比亚共和国          |
| 520 | 瑙鲁共和国            |
| 524 | 尼泊尔王国            |
| 528 | 荷兰王国             |
| 530 | 荷属安的列斯           |
| 533 | 阿鲁巴              |
| 540 | 新喀里多尼亚           |
| 548 | 瓦努阿图共和国          |
| 554 | 新西兰              |
| 558 | 尼加拉瓜共和国          |
| 562 | 尼日尔共和国           |
| 566 | 尼日利亚联邦共和国        |
| 570 | 纽埃               |
| 574 | 诺福克岛             |
| 578 | 挪威王国             |
| 580 | 北马里亚纳自由联邦        |
| 581 | 美国本土外小岛屿         |
| 583 | 密克罗尼西亚联邦         |
| 584 | 马绍尔群岛共和国         |
| 585 | 帕劳共和国            |
| 586 | 巴基斯坦伊斯兰共和国       |
| 591 | 巴拿马共和国           |
| 598 | 巴布亚新几内亚独立国       |
| 600 | 巴拉圭共和国           |
| 604 | 秘鲁共和国            |
| 608 | 菲律宾共和国           |
| 612 | 皮特凯恩             |
| 616 | 波兰共和国            |
| 620 | 葡萄牙共和国           |
| 624 | 几内亚比绍共和国         |
| 626 | 东帝汶              |
| 630 | 波多黎各             |
| 634 | 卡塔尔国             |
| 638 | 留尼汪              |
| 642 | 罗马尼亚             |
| 643 | 俄罗斯联邦            |
| 646 | 卢旺达共和国           |
| 654 | 圣赫勒拿             |
| 659 | 圣基茨和尼维斯联邦        |
| 660 | 安圭拉              |
| 662 | 圣卢西亚             |
| 666 | 圣皮埃尔和密克隆         |
| 670 | 圣文森特和格林纳丁斯       |
| 674 | 圣马力诺共和国          |
| 678 | 圣多美和普林西比民主共和国    |
| 682 | 沙特阿拉伯王国          |
| 686 | 塞内加尔共和国          |
| 688 | 塞尔维亚             |
| 690 | 塞舌尔共和国           |
| 694 | 塞拉利昂共和国          |
| 702 | 新加坡共和国           |
| 703 | 斯洛伐克共和国          |
| 704 | 越南社会主义共和国        |
| 705 | 斯洛文尼亚共和国         |
| 706 | 索马里共和国           |
| 710 | 南非共和国            |
| 716 | 津巴布韦共和国          |
| 724 | 西班牙王国            |
| 728 | 南苏丹              |
| 732 | 西撒哈拉             |
| 736 | 苏丹共和国            |
| 740 | 苏里南共和国           |
| 744 | 斯瓦尔巴岛和扬马延岛       |
| 748 | 斯威士兰王国           |
| 752 | 瑞典王国             |
| 756 | 瑞士联邦             |
| 760 | 阿拉伯叙利亚共和国        |
| 762 | 塔吉克斯坦共和国         |
| 764 | 泰王国              |
| 768 | 多哥共和国            |
| 772 | 托克劳              |
| 776 | 汤加王国             |
| 780 | 特立尼达和多巴哥共和国      |
| 784 | 阿拉伯联合酋长国         |
| 788 | 突尼斯共和国           |
| 792 | 土耳其共和国           |
| 795 | 土库曼斯坦            |
| 796 | 特克斯和凯科斯群岛        |
| 798 | 图瓦卢              |
| 800 | 乌干达共和国           |
| 804 | 乌克兰              |
| 807 | 前南斯拉夫马其顿共和国      |
| 818 | 阿拉伯埃及共和国         |
| 826 | 大不列颠及北爱尔兰联合王国    |
| 831 | 根西岛              |
| 832 | 泽西岛              |
| 833 | 马恩岛              |
| 834 | 坦桑尼亚联合共和国        |
| 840 | 美利坚合众国           |
| 850 | 美属维尔京群岛          |
| 854 | 布基纳法索            |
| 858 | 乌拉圭东岸共和国         |
| 860 | 乌兹别克斯坦共和国        |
| 862 | 委内瑞拉共和国          |
| 876 | 瓦利斯和富图纳          |
| 882 | 萨摩亚独立国           |
| 887 | 也门共和国            |
| 891 | 南斯拉夫联盟共和国        |
| 894 | 赞比亚共和国           |
| A00 | 科索沃              |

### 附件 18: 支付渠道代码

用户需要传入数值

| 代码  | 说明          |
| --- | ----------- |
| 001 | 现金          |
| 002 | 银行转账        |
| 003 | 票据          |
| 004 | 借记卡         |
| 005 | 信用卡         |
| 006 | 购物卡/卷       |
| 007 | 优惠卷         |
| 008 | 商场积分        |
| 009 | 支付宝         |
| 010 | 微信支付        |
| 011 | 云闪付         |
| 012 | Apple Pay   |
| 013 | Samsung Pay |
| 099 | 其他          |

© 2025 数电发票接口文档 
