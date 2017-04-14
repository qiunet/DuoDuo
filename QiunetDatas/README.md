# QiunetDatas

### Redis Entity
![IredisObj 结构图](imgs/IRedisObj.png)     

### DB
    先导入sql/test.sql 到数据库测试使用
    jdbc.properties 和 TestLog4jJdbc 需要修改对应的密码.

### 结构
    分为几个部分
    DataSupport 衍生4个类可以与开发基本一一对应
    IBaseEntityInfo 类的信息. 为DataSupport的构造时候使用. 4个接口在开发中一一对应
    IEntityDbInfo 和数据库交互的都是该接口直接或者间接实现类
    IRedisEntity  和redis相关的都是该接口直接或者间接实现类 已经实现了4个通用的类.  在(org.qiunet.data.redis.support)包下
    
    
### Redis
     dbInfoKey 指分库和redis key 需要的那个field 一般指能uid 工会等则指 工会id
     redis 在对象 或者 list对象字段发生改变和缓存不一样时候 , 会返回null, 并且使缓存失效.

### 自动对象生成
     自动生成po  po为对应数据库的 以及 do (Data Object 对应客户端数据对象) ,两种都可以下行给客户端.
     一个对象可能需要多种下行格式,比如playerPo 下行给自己,是下行全部, 下行给好友是给部分, 这个部分就使用do给出. 
     do 使用 `BeanUtils.copyProperties` 方法复制
     
