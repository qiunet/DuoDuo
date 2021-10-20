# Excel2Cfgs
	自己写的一个Excel转换工具. 可以将Excel转换为字节流格式的文件.
	Idea 先要设置 Editor -> GUI Designer 
	选项： "Generate GUI into:" 从 "Binary class files" 变更为 "Java source code" 	
	否则会抛异常。

	优点:
		1. 可以加密, 防止简单破解.
		2. 读取时候组织形式可以多样.
		
	缺点:
		1. 需要按照列来读取, 如果表格多变, 调整次数会增加.


### 文件名规则
	中文名字_英文名.xlsx
	中文名给策划看的 英文名程序生成文件.

### 生成规则
	第一行 中文名描述
	第二行  英文名 项目里面使用的
	第三行  ALL  SERVER  CLIENT 区别谁需要. 还是都要.
	
	sheet名如果开头有 s. 表示仅服务端生成
	sheet名如果开头有 c. 表示仅客户端生成

### 身份问题
	服务端和客户端会生成对应的配置文件到项目目录.
	策划会将对应的生成到根目录的.xd.config .json.config .xml.config 文件夹
	文件夹里面区分 server  client目录.


