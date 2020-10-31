package org.qiunet.excel2cfgs.appender;

import java.io.File;

/***
 * 中间base层, 处理
 * Appender 统一处理往服务端  客户端复制文件
 *
 * qiunet
 * 2020-02-02 13:08
 **/
abstract class BaseAppender implements IAppender {
	/***
	 * 输出目录到文件的中间相对路径.
	 */
	protected String outputRelativePath;
	/***
	 * 例如一个文件名为: 模板_template.xlsx
	 * filePrefix 就是template
	 */
	protected String filePrefix;

	/**
	 * 源文件
	 */
	protected File sourceFile;
	protected BaseAppender(File sourceFile, String outputRelativePath, String filePrefix) {
		this.outputRelativePath = outputRelativePath;
		this.filePrefix = filePrefix;
		this.sourceFile = sourceFile;
	}

	/**
	 * copy 到 配置路径. 如果有的话
	 * @param file
	 */
	protected void copyToProject(File file) {
		// 目前是服务端 客户端直接生成到项目配置路径. 不需要额外copy
		// 策划负责转化和提交. 服务端 客户端只需要生成.
	}
}
