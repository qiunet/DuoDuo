# ProjectInit
	使用xml文件描述业务对象. 然后自动生成业务需要的 po xml等文件.
	默认使用以下4个文件. 也可以调用接口自己指定文件路径文件名.
	
	xml 是放在test的 resources 下. 
	
	最好是copy测试项目里面的稍微修改. 因为有对xml格式的定义.
	
## 调用 
 > `ProjectInitCreator.create()` <br />
 > 也可以自己指定下面的四个文件的路径和名称.<br />
 调用: `ProjectInitCreator.create(IProjectInitConfig config)`

## 理解帮助
	主要使用的约定大于自定义的方式. 
	Po  和数据库一一映射的对象 存放于entity目录
	Vo  处理Po的装饰类. 和Po存放一个目录
	Info  存放一些跟Po对象相关的特定数据. 帮助DataSupport处理数据的关系. 存放在info目录
	Service 和Po有关系的业务对象 存放在service目录下 

## 配置 
1. #### `xml/entity_create.xml`
		主要配置业务数据库对象. 生成po.里面仅仅包含 entity 元素.
		
		dbInfoKey 使用来分库的一个字段. 一般是uid.
		
		需要注意的是 entityType属性: 
			一对一的对象使用 RedisEntity. 比如 Player.
			一对多的对象使用 RedisList. 比如 Item, 需要指定subKey 用来区分每一条数据的id 比如item_id.
		
		field 默认值:
			int  long  默认值没有指定情况为0
			boolean 没有指定为 false
			String  没有指定为为 "" 	 	
	
2. #### `xml/entity_info_create.xml`
		分几个部分:
			1. redisKey 指定redisKey 在哪个package RedisKey 最好事先IRedisKey
			
			2. beans 
			这里主要定义redis实例和dbinfoKey解析类(根据dbInfoKey 得到连接数据库需要的DbIndex DbName DbSourceKey). 
			infos解析过程需要的参数redisRef 和 dbInfoRef 都会到beans下找对应的定义.
			
			3. infos
			提供一些每个业务不一样的数据. poref 指向 entity_create.xml 中定义的poName
			 
3. #### `xml/mybatis_config_create.xml`
		config 指定mybatis 的配置文件名称 路径
			
		extraConfigs 指定一些不按照现有规则来的 mybatis配置文件. 比如: uid_builder 等.
			
4. #### `xml/mybatis_mapping_create.xml`
		每一个 entity_create.xml 定义的都会在这里有一个对应的定义. 主要是对表的
		增删改查. 
		属性:
		name			定义的xml文件名
		poref 			指向 entity_create.xml 中定义的poName
		splitTable 		是否分表. 默认不分表 . true 则会根据情况. 数据分到10个表.
		tablePrefix 	如果数据库的表名不是name 则自己重新定义下.
		uniqid 			如果subKey 是唯一的. 这里需要填true. 然后把自增的id名填入 selectKey
		selectKey 		自增的表名. 和 uniqid 一般一起用.
