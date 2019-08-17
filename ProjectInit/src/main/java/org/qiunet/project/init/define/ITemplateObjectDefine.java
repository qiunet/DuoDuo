package org.qiunet.project.init.define;

import java.nio.file.Path;

/***
 *
 * 往模板输出的对象
 *
 * qiunet
 * 2019-08-17 11:20
 **/
public interface ITemplateObjectDefine {

	/***
	 * 输出路径 仅到文件entity文件夹父类文件夹
	 *
	 * 使用Paths 组装
	 * @return
	 */
	Path outputPath();
}
