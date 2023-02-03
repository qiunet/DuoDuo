# ProjectInit
	使用xml文件描述业务对象. 然后自动生成业务需要的 po xml等文件. 
	并且会利用 Entity2Table 自动生成数据库表.
	
	xml 集中放置在一个目录. 每个模块一个xml即可.
	
## 使用
需要增加maven引用:

	<dependency>
		<groupId>io.github.qiunet</groupId>
		<artifactId>ProjectInit</artifactId>
		<version>7.0.10</version>
	</dependency>

引用的scope如果有需要, 可以设置为`test`
	
## 调用 
	 ProjectInitCreator.create(String xmlDirectoryName) 需要给出xml的放置目录
	 有个 Mybatis的配置文件名需要额外配置(指定一些不使用这套逻辑的mybatis mapper),
	 但是可以使用默认值`MybatisConfig.xml`.
	 

## 理解帮助
参考: [QiunetData](../QiunetDatas/README.md)

## 配置 
	1. 根据项目选择一个模式: 
		a. DB 
		b. CACHE
		c. REDIS
	
	2. 参考该项目test resources下的配置
		db模式参考 player.xml   item.xml
		cache模式参考 guild.xml 	guild_member.xml
		redis模式参考  vip.xml 	equip.xml
		
	3. xml 使用xsd校验. 可以自己设置自动提示, 可以点击去xsd 查看字段含义.	
