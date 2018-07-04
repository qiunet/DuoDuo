# DuoDuo
    自己根据过往开发的经验. 自己抽出来的一套公用代码.
    代码规避了很多可能在开发出现中的坑点. 然后尽量弱化了配置需求. 
    会在使用的过程慢慢完善成为一套可以做游戏和App开发的工具模块汇总.
    
    Duoduo的原则是 理解后,融入自己的代码中. 所以有问题时候,
    能够在第一时间反应过来问题出在哪.
    

## 模块简介:
| 	模块			|	简介					|
|----------		|----------	|
|[Quartz](Quartz/README.md) 			|	 定时调度	|
|[QiunetUtils](QiunetUtils/README.md)		|	 各种基本工具类|
|[QiunetDatas](QiunetDatas/README.md) 		|	 Mysql和Redis使用模块, 实现了异步更新等功能.|
|[ProjectInit](ProjectInit/README.md) 		| 通过xml配置.自动生成po xml 和调用的类.|
|[ExcelToStream](ExcelToStream/README.md)	| Excel转设定的工具. 打包成可以执行的jar包.|
|[FlashHandler](FlashHandler/README.md) 		| 能启动Tcp Http WebSocket作为服务的模块|
|[GameTest](GameTest/README.md)  		|	 模拟机器人测试的模块.|
|[LoggerSender](LoggerSender/README.md)  	|	 日志发送模块|
|[LoggerAcceptor](LoggerAcceptor/README.md)  	|  日志接收模块.| 
|[all](all/README.md)  				|	 打包成一个duoduo-all 方便调用的模块.|
 
## 安装环境
* jdk 1.8
* maven 3.5
* mariadb redis 两个必须都有.
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

## 5.0 修改
* 从Mysql转MariaDB
* 去掉 DbSourceType 类型. 支持多个global库.
* ActiveMQ 的module添加. (单独于 all , 因为很多项目可能用不着)
* 加入 dbInfoKey 和 subId的泛型


## 交流
QQ群: `669409114`
