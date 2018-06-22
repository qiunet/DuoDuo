# QiunetUtil

	主要是工具类. 实现了大部分用得到的工具方法.
	有的比如String.split()因为我们不需要正则表达式切割. 自己也做了优化,放在StringUtil.split里面. 
	这个模块的测试不需要做任何改动. 直接调用 mvn test

### 包以及简介 
|包名| 简介|
|----|----|
| args 			|`处理参数的`|
| classloader	|`类加载器`|
| classScanner	|`类的扫描 可以扫描一些特定的class 避免需要配置来确定关系.`|
| collection.safe |`安全的集合类, 使用后. 不允许修改内容.`|
| common 		|`通用的一些判断, 在不在大的集合等`|
| data 			|`自己定义了一个keyval的类, 大部分的get 转换关系写这`|
| date 			|`时间的工具类`|
| encryptAndDecrypt |`加密 解密相关`|
| hook 			|`需要虚拟机关闭的close东西, 添加到这个目录的工具下`|
| http 			|`Http 的工具类`|
| json 			|`json相关的操作`|
| net 			|`网络相关的操作 得到内网ip什么`|
| math 			|`随机啊什么的工具相关`|
| pool 			|`自己实现的简易池`|
| property 		|`加载properties 相关的工具`|
| shell 		|`操作shell脚本, 以及返回结果`|
| threadLocal 		|`线程变量类的简易封装`|
| timer 		|`调度相关的东西. 是Timer的封装`|

### Test
> 基本都有测试类, 可以自行添加. 自行测试

### install
`mvn install`	
