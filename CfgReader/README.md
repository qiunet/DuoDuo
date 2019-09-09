# `CfgReader`配置文件的读取模块
* 支持格式 `json` `xd`
* 根据定义类字段顺序自动注入数据
* 支持使用ICfgTypeConvert进行复杂字段的注入


#### Cfg实现那个接口?
	自己根据实际情况, 判断你的Cfgs在内存中是怎么存储.

	ISimpleMapConfig<ID> 简单的Map结构, 对应存储为: Map<ID, Cfg>
	INestMapConfig<ID, SubId> 对应存储为: Map<ID, Map<SubId, Cfg>>
	INestListConfig<ID, SubId> 对应存储为: Map<ID, List<Cfg>>
	
	如果有多个字段才能定位一条Cfg, 可以将多个字段拼出一个新的 SubId, 然后再次判断选择.
	 
#### Manager 选择继承哪个类? 
	Cfg确定实现的接口后, 基本就能确定需要继承哪个类了.
	会有:  
		`NextList{格式名}CfgManager`
		`NextMap{格式名}CfgManager`
		`SimpleMap{格式名}CfgManager` 
 	选择对应的CfgManager继承即可. 
 	
 	推荐一个xd json文件对应一个Manager类.


