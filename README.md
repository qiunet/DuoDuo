# DuoDuo
    自己根据过往开发的经验. 自己抽出来的一套公用代码.
    代码规避了很多可能在开发出现中的坑点. 然后尽量弱化了配置需求. 
    会在使用的过程慢慢完善成为一套可以做游戏和App开发的工具模块汇总.
    
    Duoduo的原则是 理解后,融入自己的代码中. 所以有问题时候,
    能够在第一时间反应过来问题出在哪.
    
## 名词解释
* Do Data Object 持久化对象(阿里规范) 负责跟数据库交互的对象
* Bo Business Object 业务对象(阿里规范), 给业务提供支持的对象
* DataSupport 操作数据的类, 会自动搞定异步更新等问题
* Service    处理业务的一些公用方法
* Handler   处理请求的逻辑类. 一个请求一个handler
* TestCase  对应handler的测试类 

## 模块简介
| 	模块			|	简介					|
|----------		|----------	|
|[Quartz](Quartz/README.md) 			|	 定时调度相关的模块	|
|[CfgReader](CfgReader/README.md) 			|	 配置文件读取的模块	|
|[QiunetUtils](QiunetUtils/README.md)		|	 各种基本工具类|
|[QiunetDatas](QiunetDatas/README.md) 		|	 Mysql和Redis以及本地Cache使用模块, 实现了异步更新等功能.|
|[Entity2Table](Entity2Table/README.md) 		|	根据Do对象自动生成和更新数据库结构的模块|
|[ProjectInit](ProjectInit/README.md) 		| 通过xml配置.自动生成Do Bo xml Service和调用的类.|
|[ExcelToStream](ExcelToStream/README.md)	| Excel转设定的工具. 打包成可以执行的jar包.|
|[FlashHandler](FlashHandler/README.md) 		| 能启动Tcp Http WebSocket作为服务的模块|
|[GameTest](GameTest/README.md)  		|	 模拟机器人测试的模块.|
|[JavaAgent](JavaAgent/README.md)  		|	 可以通过javaAgent 最后热加载指定的class|
|[LoggerSender](LoggerSender/README.md)  	|	 日志发送模块|
|[LoggerAcceptor](LoggerAcceptor/README.md)  	|  日志接收模块.| 
|[all](all/README.md)  				|	 打包成一个duoduo-all 方便调用的模块.|
 
## 安装环境
* jdk 1.8
* maven 3.5
* Mysql(MariaDB) Redis 两个必须都有.
* 推荐使用 IntelliJ IDEA(Eclipse 对module的使用感觉不是很好).
 
## install
> 根目录执行 `mvn install -DskipTests` <br />
> 如果本地有测试环境. 可以不加: ` -DskipTests` <br />
可以直接打包到本地. 然后在maven引用.

## 引用
	<dependency>
		<groupId>org.qiunet</groupId>
		<artifactId>duoduo-all</artifactId>
		<version>${version}</version>
	</dependency>

## 示例
自己写了服务器端和H5客户端的交互. 可以参考[项目](https://github.com/qiunet/CocosCreatorAndServer)


## 交流
QQ群: `669409114` , 入群请附带申请留言: "Github"

```
E:\work\company\myself\git\DuoDuo>mvn -version
Apache Maven 3.8.5 (3599d3414f046de2324203b78ddcf9b5e4388aa0)
Maven home: D:\soft_pkg\apache-maven-3.8.5-bin\apache-maven-3.8.5
Java version: 1.8.0_171, vendor: Oracle Corporation, runtime: C:\Program Files\Java\jdk1.8.0_171\jre
Default locale: zh_CN, platform encoding: GBK
OS name: "windows 10", version: "10.0", arch: "amd64", family: "windows"

➜  ~ mvn -version
Apache Maven 3.8.5 (3599d3414f046de2324203b78ddcf9b5e4388aa0)
Maven home: /Users/zhengjian/Documents/soft/apache-maven-3.8.5
Java version: 1.8.0_171, vendor: Oracle Corporation, runtime: /Library/Java/JavaVirtualMachines/jdk1.8.0_171.jdk/Contents/Home/jre
Default locale: zh_CN, platform encoding: UTF-8
OS name: "mac os x", version: "10.12.6", arch: "x86_64", family: "mac"
```
