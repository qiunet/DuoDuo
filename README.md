# DuoDuo
    自己根据过往开发的经验. 自己抽出来的一套公用代码.
    会在使用的过程慢慢完善成为一套可以做游戏和App开发的工具模块汇总.
    

## 模块简介:
| 	命令			|	描述					|
|----------		|----------	|
|Quartz 			|	 定时调度	|
|QiunetUtils		|	 各种基本工具类|
|QiunetDatas 		|	 Mysql和Redis使用模块, 实现了异步更新等功能.|
|ProjectInit 		| 通过xml配置.自动生成po xml 和调用的类.|
|ExcelToStream	| Excel转设定的工具. 打包成可以执行的jar包.|
|FlashHandler		| 能启动Tcp Http WebSocket作为服务的模块|
|GameTest 		|	 模拟机器人测试的模块.|
|LoggerSender 	|	 日志发送模块|
|LoggerAcceptor 	|  日志接收模块.| 
|all 				|	 打包成一个duoduo-all 方便调用的模块.|
 
## install
> `mvn clean && mvn install -DskipTests` <br />
> 如果本地有测试环境. 可以不加: ` -DskipTests` <br />
可以直接打包到本地. 然后在maven引用.

## 交流
QQ群: `669409114`
