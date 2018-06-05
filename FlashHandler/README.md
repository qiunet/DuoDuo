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

