# LoggerSender
	如果是集群的情况. 日志需要往一台机器打印. 则可以使用本模块. 


### 类: LoggerSender
	项目中, 需要继承LoggerSender, 给定需要的参数.
	有两种方式发送日志: 
	1. 不重要的日志
		调用 LoggerSender.sendLog()方法, 采用UDP方式发送, 有0.2%的概率丢失日志.
	2. 重要的日志
		调用 LoggerSender.sendImportantLog()方法,采用TCP方式发送, 100%送达.
		


### [接收服务器](../LoggerAcceptor/README.md)

