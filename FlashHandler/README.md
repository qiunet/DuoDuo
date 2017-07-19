# FlashHandler
	自己想做的一个长连接的服务器通用工具jar.
	
### BootStrap
> 通篇的配置引导. 配置 拦截器, 配置端口. 然后启动

### Acceptor
> 接收器, 接受传入的context, 然后使用线程池执行context, context自己

### Context
* TcpContext
	
* HttpContext

### Handler
* TcpHandler
> handler 会给出requestData, 和一个Iresponse, 随时可以响应数据
* HttpHandler
> handler里面的会给出requestData, 然后要求返回responseData数据 
	
### GameCfg(游戏设定数据)
	游戏数据分扫描,自动加载到GameCfgManagers里面

