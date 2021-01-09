# GameTest

> 主要想做个通用的测试框架, 可以满足大部分的游戏测试.<br />
> 游戏只需要关心小部分的实现和大部分的测试业务即可.

#### FlashHandler
* 自己的Netty框架. 可以自由组合集中协议服务

#### ITestCase
* HTTP
	* HttpStringTestCase
	* HttpProtobufTestCase
* PersistConn
	* PersistConnProtobufTestCase
	* PersistConnStringTestCase


	Tcp Websocket 使用PersistConn的类型TestCase
	Http根据自己的情况使用Http的

#### IResponse
服务器给推送的数据, 自己选择一下类继承. 需要使用`Response` 注解消息ID
* ProtobufResponse
* StringResponse

#### 启动
* RobotExecutor<br/>
构造需要一个`ExecutorParams`类 构造后, 调用的方法有两个`testing()` 和 `pressureTesting(机器人数)`. 
	* addScannerHandler()
		> 添加各种扫描器, 比如 `ResponseScannerHandler`, `GameCfgScannerHandler` ,`PropertiesScannerHandler`
	* setRobotFactory()
		> 设置机器人的生成规则. 必要的提供是 `openid` 和 `sid`
	* setInitializer()
		> 一段自定义的初始化代码. 不是必要的
	*  addTestCase()
		> 顺序添加一个测试`ITestCase`用例.


    
