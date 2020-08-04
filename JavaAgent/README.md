# JavaAgent

	通过传入的一个Json字符串, 解析成 ClassInfos.
	逐个使用Vm工具类进行热替换.
	
	调用方为ClassHotSwap类
	
### 注意点
	热更class时候, 也需要替换jar包. 以免重启后, 更新被还原.
