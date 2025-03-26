package org.qiunet.project.init.creator;

import org.apache.commons.digester.Digester;
import org.qiunet.project.init.define.mybatis.MybatisConfigDefine;
import org.qiunet.project.init.define.mybatis.MybatisExtraDefine;
import org.qiunet.project.init.define.mybatis.MybatisExtraPkgDefine;
import org.qiunet.project.init.enums.EntityType;
import org.qiunet.project.init.template.VelocityFactory;
import org.qiunet.project.init.util.DigesterUtil;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * Created by qiunet on 4/8/17.
 */
public final class ProjectInitCreator {
	/**
	 * 默认的mybatis config 文件名
	 */
	private static final String DEFAULT_MYBATIS_CONFIG_FILE_NAME = "MybatisConfig.xml";
	private static Logger logger = LoggerType.DUODUO.getLogger();
	private String mybatisConfigFileName;
	private File xmlDirectory;

	public ProjectInitCreator(String mybatisConfigFileName, File xmlDirectory) {
		this.mybatisConfigFileName = mybatisConfigFileName;
		this.xmlDirectory = xmlDirectory;
	}

	/***
	 * 给出对应classpath的xml文件夹路径即可
	 * 使用默认的 MybatisConfig 文件名
	 */
	public static void create(String xmlDirectory) {
		create(DEFAULT_MYBATIS_CONFIG_FILE_NAME, xmlDirectory);
	}
	/***
	 * 使用指定的MybatisConfig.xml 配置文件名 和 指定的xml路径
	 * @param mybatisConfigFileName
	 * @param xmlDirectory
	 */
	public static void create(String mybatisConfigFileName, String xmlDirectory) {
		URL url = Thread.currentThread().getContextClassLoader().getResource(xmlDirectory);
		try {
			assert url != null;
			create(mybatisConfigFileName, new File(url.toURI()));
		} catch (URISyntaxException e) {
			logger.error("xml directory ["+xmlDirectory+"] create exception:", e);
		}
	}
	/***
	 * 给出对应classpath的xml文件夹File
	 */
	public static void create(String mybatisConfigFileName, File xmlDirectory) {
		if (! xmlDirectory.isDirectory()) {
			throw new RuntimeException("["+xmlDirectory.getAbsolutePath()+"] is not a directory");
		}
		new ProjectInitCreator(mybatisConfigFileName, xmlDirectory).process();
	}

	private void process(){
		this.processMybatisConfig();

		for (File file : Objects.requireNonNull(xmlDirectory.listFiles())) {
			if (mybatisConfigFileName.equals(file.getName())) continue;

			EntityType entityType = EntityType.parse(file);
			EntityCreator entityCreator = new EntityCreator(entityType, file, Paths.get(mybatisConfig.getBaseDir(), mybatisConfig.getConfigDir()).toString());
			entityCreator.parse();

			mybatisConfig.addExtraFile(entityCreator.getEntityDefine().getNameSpace()+".xml");
			mybatisConfig.addAliasPackage(entityCreator.getEntityDefine().getPackageName());
		}

		// mybatis-config.xml 的输出
		String configPath = Paths.get(mybatisConfig.outputPath().toString(), mybatisConfig.getFileName()).toString();
		VelocityFactory.getInstance().parseOutFile("vm/mybatis_config_create.vm", configPath, mybatisConfig);
	}


	private MybatisConfigDefine mybatisConfig;
	private void processMybatisConfig(){
		File mybatisConfigFile = new File(xmlDirectory.getPath(), mybatisConfigFileName);
		if (! mybatisConfigFile.exists() || ! mybatisConfigFile.isFile()) {
			throw new IllegalArgumentException("mybatis config xml ["+mybatisConfigFileName+"] is not valid");
		}
		this.mybatisConfigFileName = mybatisConfigFile.getName();

		Digester digester = new Digester();
		digester.push(this);
		DigesterUtil.addObjectCreate(digester,"mybatis_config", MybatisConfigDefine.class, "setMybatisConfig");
		DigesterUtil.addObjectCreate(digester,"mybatis_config/extraConfigs/extra", MybatisExtraDefine.class, "addExtraFile");
		DigesterUtil.addObjectCreate(digester,"mybatis_config/extraTypeAliases/extra", MybatisExtraPkgDefine.class, "addAliasPackage");
		try {
			digester.parse(mybatisConfigFile);
		} catch (IOException | SAXException e) {
			logger.error("processMybatisConfig ["+mybatisConfigFile.getName()+"] create exception:", e);
		}
	}

	public void setMybatisConfig(MybatisConfigDefine mybatisConfig) {
		this.mybatisConfig = mybatisConfig;
	}
}
