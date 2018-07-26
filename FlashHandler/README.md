# FlashHandler
> 自己想做的一个长连接的服务器通用工具项目,支持netty tcp 以及 netty的http.<br />
> 可以启动 http 和tcp udp的连接服务

### BootstrapServer
> 通篇的配置引导. 配置 拦截器, 配置端口. 然后启动

### Acceptor
 * 接收器
 	> 接受传入的IRequestContext, 然后使用线程池执行context对象.
 * Tcp WebSocket
 	> Acceptor会根据QueueHandlerIndex分配到固定的线程执行(QueueHandlerIndex 可以随时更改)
 * Http
 	> Acceptor会根据id分配到固定的线程执行

### IRequestContext
* ITcpRequestContext
* IHttpRequestContext
* IWebSocketRequestContext

### Facade
*  对Handler隐藏IRequestContext某些特性.只允许访问部分方法

### Handler
`每个Handler子类型下面又会分 Protobuf 和 String 类型. 目前应该是足够使用的. `
* ITcpHandler
	> handler 会给出context, 可以得到requestData 和 response 一个Object 出去.
	也可以调用 ResponseMsgUtil 的方法push 消息
* IHttpHandler
	> handler里面的会给出requestData, 然后要求返回responseData数据 
* IWebSocketHandler
	> handler 会给出context, 可以得到requestData 和 response 一个Object 出去.
    	也可以调用 ResponseMsgUtil 的方法push 消息

### 注解
* RequestHandler
	> 对游戏的Handler进行注解. 标识ID 和 desc 属性
* UriPathHandler
	> 其它类型的请求. 比如平台调用. 给你传入的参数等和游戏完全不一样.
	 
### GameCfg(游戏设定数据)
* 游戏数据分扫描,自动加载到GameCfgManagers里面 
* 支持重新热加载.


### 推荐ID分配规则
> 根据功能切分, 每个人开发不干扰. 

|功能|功能对应ID|请求ID规则(功能ID* 1000 + 自增(1开始0预留给无请求类型)|响应规则ID(请求ID*1000 + 自增)|无请求响应规则(功能ID * 1000 * 1000 + 自增)|
|----|-----|-----|-----|-----|
|登录 | 1|1001 ~ 1999 | 请求ID*1000 + 自增| 1000000 ~ 1000999|
|玩家 | 2|2001 ~ 2999 | 请求ID*1000 + 自增| 2000000 ~ 2000999|
|背包 | 3|3001 ~ 3999 | 请求ID*1000 + 自增| 3000000 ~ 3000999|
|聊天 | 4|4001 ~ 4999 | 请求ID*1000 + 自增| 4000000 ~ 4000999|


### 可靠UDP初步想法
	1. 服务器session持有自增ID一个. 每个session自己的包加1 作为序号.
	2. 线程安全的Map保存该包(序号为Key). 
	3. 因为UDP的包不能太大, 所以大于500的包需要分包. 里面有魔数 校验码 包序号 包的子包数量 子包序号 是否需要回复等信息.
	4. 客户端收到包后, 需要回复包序号  子包序号. 需要设定最大重传数. 超过就放弃.
	5. 服务器清出已经回复的包. 有定时调度每500毫秒处理包的重发. 
	6. 服务器清除不需要等回复的包.如果包数据N ms后没有到齐 
	7. 如果客户端重连换了ip 端口. 则所有的计数重新开始.
