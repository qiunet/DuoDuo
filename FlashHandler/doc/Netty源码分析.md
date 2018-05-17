# Netty 源码分析 


## ServerBootstrap
	初始化好NioServerSocketChannel  注册至 NioEventLoop.
	 添加 ServerBootstrapAcceptor. 到pipeline
	 在接收到消息时候, 移除自身, 添加 ChannelInitiliazer 的内容

## NioEventLoop
	 第一次执行executor(Task) 时候在doStart里面启动
	-> run selector 循环选择 
	-> channel.unsafe().read() 
	-> pipeline-> fireChannelRead()

## AbstractNioChannel
	doRegiest() 里面注册selector



## DefaultChannelPipeline implements ChannelPipeline
	AbstractChannelHandlerContext head
	AbstractChannelHandlerContext tail
	
	addFirst addLast add* 包装成DefaultChannelHandlerContext

## ChannelHandlerContext
* AbstractChannelHandlerContext
	> static fireChannelRegister(DefaultChannelPipeline.head)
	> static fireChannelActivite(DefaultChannelPipeline.head)

* DefaultChannelHandlerContext
	> 通过instanceof 判断是否是In or Out


## AbstractChannel
	* NioServerSocketChannel 
	* NioSocketChannel 
	他们的的unsafe 是不一样的两个实现类.
	
	
## 内存细节
	-XX:MaxDirectMemorySize
	正式使用需要设置该参数
