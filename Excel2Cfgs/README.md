# ExcelToStream
	自己写的一个Excel转换工具. 可以将Excel转换为字节流格式的文件.
	
	优点:
		1. 可以加密, 防止简单破解.
		2. 读取时候组织形式可以多样.
		
	缺点:
		1. 需要按照列来读取, 如果表格多变, 调整次数会增加.
### 文件名规则
	中文名字_英文名.xlsx
	中文名给策划看的 英文名程序生成文件.
			
### 生成规则
	第一行 中文名描述 如果有 _ 表示需要生成json对象字段.
	第二行  数据类型
		支持: int  long  string  double
	第三行  英文名 项目里面使用的
	第四行  ALL  SERVER  CLIENT 区别谁需要. 还是都要.
	
	sheet名如果开头有 s. 表示仅服务端生成
	sheet名如果开头有 c. 表示仅客户端生成
	
### 身份问题
	服务端和客户端会生成对应的配置文件到项目目录.
	策划会将对应的生成到根目录的.xd.config .json.config .xml.config 文件夹
	文件夹里面区分 server  client目录.
				
	