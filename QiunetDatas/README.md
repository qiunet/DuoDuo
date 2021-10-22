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
	使用Server.conf配置
	配置模块在db模块. 可以配置多组db.
	
### IDatabaseSupport
	所有的Mysql操作都是通过IDatabaseSupport 进行. DbSourceDatabaseSupport 会为每个dbSource 创建一个实例.
	通过dbSource. 可以对对应的数据库进行数据操作.
	 	
	
	
### Redis
     使用server.conf配置
	配置在 redis 模块. 可以配置多组


### 自动生成Po Mybatis xml等[ProjectInit](../ProjectInit/README.md)
	因为大量的事情是重复的, 所以使用velocity生成代码, 避免人的疏忽带来的错误.

### 自动根据Po的字段同步数据库的表[Entity2Table](../Entity2Table/README.md)
	默认所有的分库中的表结构都是一样的.只要同步字段,就同时操作所有的库.
	
