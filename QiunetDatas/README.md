# QiunetDatas

### Redis Entity
![IredisObj 结构图](imgs/IRedisObj.png)     

### 测试
    因为测试需要数据库, 所以需要先导入sql/test.sql 到数据库测试使用
    jdbc.properties 和 TestLog4jJdbc 需要修改对应的账号密码信息.
    然后执行 `mvn test`


### 结构
    分为几个部分
    DataSupport 衍生4个类可以与开发基本一一对应
    IBaseEntityInfo 类的信息. 为DataSupport的构造时候使用. 4个接口在开发中一一对应
    IEntityDbInfo 和数据库交互的都是该接口直接或者间接实现类
    IRedisEntity  和redis相关的都是该接口直接或者间接实现类 已经实现了4个通用的类.  在(org.qiunet.data.redis.support)包下
    
    
### Redis
     redis 主要是AbstractRedis. 

### Mysql
	
