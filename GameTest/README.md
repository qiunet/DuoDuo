# GameTest
********

 自己的行为树测试框架. 可以通过组合各种`BehaviorAction`来模拟玩家执行玩法请求操作, 最终构造自己的机器人逻辑进行压力测试!  
 
 测试的机器人可以通过向钩子指令`AddRobot ${num}`分批次加入, 直到榨取出机器的最大性能. 建议最终的参考值要留出冗余量, 
 比如最高在线的`60%`左右即可!


## 使用
需要增加maven引用:

	<dependency>
		<groupId>io.github.qiunet</groupId>
		<artifactId>GameTest</artifactId>
		<version>7.2.2</version>
	</dependency>

引用的scope如果有需要, 可以设置为`test`

### 行为(IBehaviorAction)
	1. 所有的action 节点继承 BaseRobotAction . 
	2. action 如果有网络请求. execute 需要返回RUNNING.
	3. 在 runningStatusUpdate 中, 判断是否有得到响应数据. 有则返回SUCCESS
	4. 通过 @TestResponse 可以处理返回的响应数据.
	5. 通过自定义注解对 IStatusTipsHandler.statusHandler 的注解. 实现指定GameStatue 的处理.

### 执行器(IBehaviorExecutor)
可以查看各个类的注释了解详细作用  
* SelectorExecutor 选择执行器
* SequenceExecutor 顺序执行器
* ParallelExecutor 并行执行器
* RandomExecutor 随机执行器


### 示例test

	实现了
	1. 登录 [-> 随机名称 -> 注册 ]-> 玩家数据请求
	2. 经验获取(打怪) -> 升级
	行为树会根据条件筛查合适的action执行.
    
