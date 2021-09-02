# GameTest

> 一个基于行为树的压测框架

#### FlashHandler
* 自己的Netty框架. 可以自由组合集中协议服务

### action
	1. 所有的action 节点继承 BaseRobotAction . 
	2. action 如果有网络请求. execute 需要返回RUNNING.
	3. 在 runningStatusUpdate 中, 判断是否有得到响应数据. 有则返回SUCCESS
	4. 通过 @TestResponse 可以处理返回的响应数据.
	5. 通过自定义注解对 IStatusTipsHandler.statusHandler 的注解. 实现指定GameStatue 的处理.

### 示例
> 在test目录
	
	实现了
	1. 登录 [-> 随机名称 -> 注册 ]-> 玩家数据请求
	2. 经验获取(打怪) -> 升级
	行为树会根据条件筛查合适的action执行.
    
