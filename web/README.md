您可以阅读本文，了解Web端超级小班课的集成操作。
## 前提条件
开发前的环境要求如下表所示，详情请参见[使用限制](https://help.aliyun.com/document_detail/71050.html#concept-71050-zh)。
<table class="lake-table" style="width: 747px;"><colgroup><col width="186" span="1"><col width="187" span="1"><col width="187" span="1"><col width="187" span="1"></colgroup><tbody><tr style="height: 33px;"><td style="text-align: left; color: #333333;">支持平台</td><td style="text-align: left; color: #333333;">浏览器</td><td style="text-align: left; color: #333333;">浏览器版本</td><td style="text-align: left; color: #333333;">备注</td></tr></tbody><tbody><tr style="height: 33px;"><td style="text-align: left;">Mac</td><td style="text-align: left;">Chrome</td><td style="text-align: left;">不低于60</td><td style="text-align: left;">—</td></tr><tr style="height: 33px;"><td style="text-align: left;">Mac</td><td style="text-align: left;">Safari</td><td style="text-align: left;">不低于11</td><td style="text-align: left;">—</td></tr><tr style="height: 33px;"><td style="text-align: left;">Windows</td><td style="text-align: left;">Chrome</td><td style="text-align: left;">不低于60</td><td style="text-align: left;">—</td></tr></tbody></table>

Web端环境需要安装Node.js及其包管理器npm([Win/Linux-Node.js安装教程](https://www.runoob.com/nodejs/nodejs-install-setup.html)，[Mac-Node.js安装教程]())

## Demo运行指引
### 步骤1：下载 SDK 和 Demo 源码
**说明：**
• Demo源码中没有集成AliRTC SDK，SDK集成方式通过npm集成。
• 源码压缩文件内分为Android端、iOS端，Web端三个文件。
• 若遇到github代码库下载缓慢的问题，可通过安装加速插件等方式加速下载。
1.下载Demo。
### 步骤2：配置 Demo 工程文件
1. 解压 步骤1 中下载的源码包。
2.找到web/src/core/data/config.js文件。
3.配置appId和appKey （baseUrl无需配置）, 获取方式请参见：[获取AppId和Appkey]()
4.说明：
本文提到的生成 Token 的方案是在客户端代码中配置 AppId和AppKey，该方法中  AppId和AppKey 很容易被反编译逆向破解，一旦泄露，攻击者就可以盗用您的阿里云流量，因此该方法仅适合本地跑通 Demo 和功能调试。
正确的 Token 签发方式是将 Token 的计算代码集成到您的服务端，并提供面向 App 的接口，在需要 Token 时由您的 App 向业务服务器发起请求获取动态 Token。更多详情请参见 生成Token。
### 步骤3：运行 Demo
1. 定位到web文件夹下 package.json 所在的目录，运行install命令以安装项目依赖：
npm install 
2. 安装成功之后，在相同目录下再运行以下命令集成SDK 。
npm install aliyun-webrtc-sdk -S
3. SDK 集成成功之后，在相同目录下再运行以下命令运行项目
npm run serve
4. 运行成功之后，你的浏览器默认会打开示例应用程序。
说明 如果没有自动打开，请在浏览器地址栏手动输入URL：https://localhost:1024