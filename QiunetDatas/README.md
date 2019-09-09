# QiunetDatas
	 
* 支持 直接操作数据库的Db模式  本地缓存的 Cache模式 和Redis缓存的Redis模式
* 仅对外提供增删改查, 但是都会自动同步缓存(有的情况), 可以异步 或者 同步更新数据库.
* 提供底层类支持自定义的查询 和 其它操作
	  
## 名词解释
* `Do` Database Object 持久化对象 负责跟数据库交互的对象
* `Bo` Business Object 业务对象, 给业务提供支持的对象
* `DataSupport` 操作数据的类, 会自动搞定异步更新等问题
* `Service`    处理业务的一些公用方法


### 测试
    因为测试需要数据库, 所以需要先导入sql/test.sql 到数据库测试使用
    db.properties 和 TestLog4jJdbc 需要修改对应的账号密码信息.
    然后执行 `mvn test`

### 名词
* `dbIndex` 	分库的索引
* `dbName`    库的名字, 因为多个库公用一个连接池. 所以需要库名区分.
* `dbSourceKey` 能够决定使用哪个数据库连接池, 通常为 0 ~ 99 的数字.在db.properties 里面配置


### 结构
	分为几个部分
	IEntity 单个实体类 有3个子类. `DbEntity` `RedisEntity` `CacheEntity`, 对应3个模式
	IEntityList 实体类列表, 有3个子类. `DbEntityList` `RedisEntityList` `CacheEntityList`, 对应3个模式
	DataSupport 衍生对应每个模式的Entity 和 EntityList的处理DataSupport
    

### Mysql
	使用db.properties 配置, key 的命名遵循:
	database.{数据库源名}.连接池参数=值
	
	# 默认数据库名, DefaultDatabaseSupport 会自动取该数据库操作
	default_database_source=qiunet_db 
	
	参考: 
	# qiunet_db 数据源的配置.
    database.qiunet_db.driverClassName=com.mysql.jdbc.Driver
    database.qiunet_db.url=jdbc:mysql://localhost:3306/qiunet_db?useUnicode=true&characterEncoding=utf-8&useSSL=false
    database.qiunet_db.username=root
    database.qiunet_db.password=Qiuyang
    
    Redis模式是分库分表, 需要额外配置一些字段来表示.
    # 每个数据库实例里面多少数据库
    db_size_per_instance=100
    # 数据库名前缀, 程序会自己拼上 dbIndex作为数据库名
    db_name_prefix=qiunet_
    
    配置参考(其中0 是数据库源名): 
    database.0.driverClassName=com.mysql.jdbc.Driver
    database.0.url=jdbc:mysql://localhost:3306?useUnicode=true&characterEncoding=utf-8&useSSL=false
    database.0.username=root
    database.0.password=Qiuyang88*
	
### IDatabaseSupport
	所有的Mysql操作都是通过IDatabaseSupport 来的. 有两个子类 `DefaultDatabaseSupport` `MoreDbSourceDatabaseSupport`, 对应取默认数据库源和自己指定.
	insert detele select以及update 都需要带上能标识源的一个字符串(由Dbproperties.getDataSourceTypeByDbIndex方法得到). 	
	
	
### Redis
     redis 主要是IRedisUtil. 目前支持pool, 以后扩展集群模式.
     
     db.properties 参数配置:
     redis.${redis名称}.${连接池参数}=${val}  // 注: host pass port 不是参数. 是jedis配置. 
     
     例如:
		 redis.data.host=localhost
		 redis.data.port=6379
		 redis.data.pass=
		 redis.data.timeout=3000


### 自动生成Po Mybatis xml等[ProjectInit](../ProjectInit/README.md)
	因为大量的事情是重复的, 所以使用velocity生成代码. 
	避免人的疏忽带来的错误.

### 自动根据Po的字段同步数据库的表(create drop alter 等)
	默认所有的分库中的表结构都是一样的.只要同步字段,就同时操作所有的库.
	
