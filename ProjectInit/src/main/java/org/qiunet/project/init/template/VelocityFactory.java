package org.qiunet.project.init.template;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.qiunet.project.init.define.ITemplateObjectDefine;
import org.qiunet.utils.file.FileUtil;

import java.io.File;
import java.io.StringWriter;

/**
 * 搞定vmfile 并且输出为文件.
 * @author qiunet
 *         Created on 16/11/22 16:04.
 */
public class VelocityFactory {
	private static final VelocityFactory instance = new VelocityFactory();

	private final VelocityEngine velocity;

	private VelocityFactory() {
		velocity = new VelocityEngine();
		// 默认应用 org/apache/velocity/runtime/defaults/velocity.properties
		velocity.setProperty("resource.loader.file.class", ClasspathResourceLoader.class.getName());
		velocity.init();
	}

	public static VelocityFactory getInstance() {
		return instance;
	}
	/**
	 *
	 * @param vmFilePath vm文件的路径. 相对于basePath的
	 * @param outputFileName 文件输出路径
	 */
	public void parseOutFile (String vmFilePath, String outputFileName, ITemplateObjectDefine objDefine) {
		Template template = velocity.getTemplate(vmFilePath ,Constants.CHAR_ENCODING);

		VelocityContext context = new VelocityContext();
		context.put(Constants.DEFAULT_DATA_OBJECT_NAME, objDefine);

		StringWriter writer = new StringWriter();
		template.merge(context, writer);

		File file = new File(outputFileName);
		FileUtil.createFileWithContent(file , writer.toString());
	}
}
