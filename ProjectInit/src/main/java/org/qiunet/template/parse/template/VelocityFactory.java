package org.qiunet.template.parse.template;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.qiunet.project.init.ProjectInitData;
import org.qiunet.template.config.Constants;
import org.qiunet.template.parse.xml.SubVmElement;
import org.qiunet.utils.file.FileUtil;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

/**
 * 搞定vmfile 并且输出为文件.
 * @author qiunet
 *         Created on 16/11/22 16:04.
 */
public class VelocityFactory {

	private static VelocityFactory instance;

	private VelocityEngine velocity;

	private VelocityFactory() {
		velocity = new VelocityEngine();

		Properties properties = new Properties();
		// 设置从classpath下查找
		properties.setProperty("file.resource.loader.class","org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
//		properties.setProperty(VelocityEngine.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.AvalonLogChute,org.apache.velocity.runtime.log.Log4JLogChute,org.apache.velocity.runtime.log.CommonsLogLogChute,org.apache.velocity.runtime.log.ServletLogChute,org.apache.velocity.runtime.log.JdkLogChute");
		properties.setProperty(VelocityEngine.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.NullLogChute");

		velocity.init(properties);
	}

	public static VelocityFactory getInstance() {
		if (instance == null) {
			synchronized (VelocityFactory.class) {
				if (instance == null)
				{
					instance = new VelocityFactory();
				}
			}
		}
		return instance;
	}
	/**
	 *
	 * @param vmFilePath vm文件的路径. 相对于basePath的
	 * @param outputFileName 文件输出路径
	 */
	public void parseOutFile (String vmFilePath, String outputFileName, ProjectInitData initData) {
		Template template = velocity.getTemplate(vmFilePath ,Constants.CHAR_ENCODING);

		VelocityContext context = new VelocityContext();
		context.put(Constants.DEFAULT_DATA_OBJECT_NAME, initData);

		StringWriter writer = new StringWriter();
		template.merge(context, writer);

		File file = new File(outputFileName);
		FileUtil.writeStringToFile(file , writer.toString() ,Constants.CHAR_ENCODING, false);
	}
}
