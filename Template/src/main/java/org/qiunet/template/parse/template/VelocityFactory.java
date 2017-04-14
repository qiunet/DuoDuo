package org.qiunet.template.parse.template;

import org.apache.commons.io.FileUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.qiunet.template.config.Constants;
import org.qiunet.template.parse.xml.SubVmElement;
import org.qiunet.utils.threadLocal.ThreadContextData;

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
	
	private String dataObjName;
	
	private Map<String , Object> params;
	/***是否已经初始化过了*/
	private boolean init;

	/**
	 * 初始化velocity的引擎
	 * @param dataObjName
	 * @param params
	 */
	public void initVelocityEngine( String dataObjName, Map<String, Object> params){
		Properties properties = new Properties();
		// 设置从classpath下查找
		properties.setProperty("file.resource.loader.class","org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		properties.setProperty(VelocityEngine.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.SystemLogChute");
		
		this.initVelocityEngine(dataObjName, params, properties);
	}

	/***
	 * 使用自定义的properties 初始化
	 * @param dataObjName
	 * @param params 
	 * @param properties 自定义的properties velocity.properties 内容
	 */
	public void initVelocityEngine( String dataObjName, Map<String, Object> params, Properties properties){
		if(init) return;

		this.init = true;
		velocity.init(properties);
		this.params = params;
		this.dataObjName = dataObjName;
	}
	
	private VelocityFactory() {
		velocity = new VelocityEngine();
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
	public void parseOutFile (String vmFilePath, String outputFileName, SubVmElement vmBaseElement) {
		Template t = velocity.getTemplate(vmFilePath ,Constants.CHAR_ENCODING);

		VelocityContext context = new VelocityContext();
		context.put(dataObjName, vmBaseElement);
		
		if (params != null) {
			for (Map.Entry<String, Object > en : params.entrySet()) {
				context.put(en.getKey(), en.getValue());
			}
		}
		
		StringWriter writer = new StringWriter();
		t.merge(context, writer);
		try {
			File file = new File(outputFileName);
			if (! file.canWrite()) file.setWritable(true);
			FileUtils.writeStringToFile(file , writer.toString() ,Constants.CHAR_ENCODING);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
