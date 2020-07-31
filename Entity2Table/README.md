### 模块简介
    数据库表自动生成更新模块
    自动扫描打了Table标签的entity,查询表结构,通过取entity属性列和table列对比来更新数据库表结构.

#### 特点
1. 取消了表的date类型,存时间戳.
2. 取消了Boolean, 用int表示
3. 取消了double,float 各种零散的类型.
4. 只保留: **int, bigint, varchar(255), varchar(1000), text, mediumtext, longtext**.这几个类型.
5. 修改列属性的时候,只允许向下修改(int -> long, varchar(255) -> varchar(255) -> text ...)
6. 为了安全性,不支持删表,删字段

#### 关键标签@interface
##### Table
函数 | 含义
---|---
name        |表名
spliTable   |是否分表(0-9)
comment     |描述
dbSource |使用哪个数据库源操作. serverType == 0时候, 为空使用默认源
##### Column
函数 | 含义
---|---
jdbcType    |字段类型
isNull   |是否为可以为null，true是可以，false是不可以，默认为true
isKey     |是否是主键，默认false,不能和 isUnique 一起使用
isAutoIncrement |是否自动递增，默认false 只有主键才能使用
defaultValue    |默认值，默认为null
comment | 注释说明

#### 核心代码:

##### 类:CreateTableController
此类实现了**IApplicationContextAware**, 通过**setApplicationContext**函数自动扫描所有Table标签的entity.
    
##### 函数:handlerTable
- 反射出entity所有的列属性
- 查询数据库(表是否存在/取到所有的列属性)
- 对比列属性(entityField,tableField)
- 更新(增加,修改,删除)

#### 配置:resources/mybatis/CreateTableMapper.xml 
    mybatis建表,改表语句.

#### 关键逻辑:
- 单数据源
    DbDatabaseSupport.getInstance(dbSource).selectOne
- 分表
    如果是分表,直接在表名上加后缀_(0-9),循环执行多次,同步多张表


