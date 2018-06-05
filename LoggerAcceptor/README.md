# LoggerAcceptor
	如果是集群的情况. 日志需要往一台机器打印. 该模块为接收日志内容的模块 

### 打包
> 使用 `mvn clean && mvn package` 可以把Acceptor打包.
`首次打包, 需要去掉排除 *.propertie的注释.`

### shell
> 项目包含启动脚本. 打包时候会自动附带进去. 
项目的启动端口可以在脚本里面配置.

### 其它配置
> 需要配置一个 `config.properties` , 里面指定日志打印路径. <br />
> 以及其它参数. 每个游戏需要指定一个secret. 用来校验日志正确性.
