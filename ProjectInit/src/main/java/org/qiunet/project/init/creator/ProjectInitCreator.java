package org.qiunet.project.init.creator;

import org.apache.commons.digester.Digester;
import org.qiunet.project.init.define.mybatis.MybatisConfigDefine;
import org.qiunet.project.init.define.mybatis.MybatisExtraDefine;
import org.qiunet.project.init.enums.EntityType;
import org.qiunet.project.init.template.VelocityFactory;
import org.qiunet.project.init.util.DigesterUtil;
import org.qiunet.project.init.util.InitProjectUtil;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.system.SystemPropertyUtil;
import org.slf4j.Logger;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by qiunet on 4/8/17.
 */
public final class ProjectInitCreator {
	/**
	 * 默认的mybatis config 文件名
	 */
	private static final String DEFAULT_MYBATIS_CONFIG_FILE_NAME = "MybatisConfig.xml";
	private static final Logger logger = LoggerType.DUODUO_CREATOR.getLogger();
	private String mybatisConfigFileName;
	private final File xmlDirectory;

	private ProjectInitCreator(String mybatisConfigFileName, File xmlDirectory, File outputModuleDir) {
		if (outputModuleDir == null) {
			outputModuleDir = InitProjectUtil.getRealUserDir();
		}
		this.mybatisConfigFileName = mybatisConfigFileName;
		InitProjectUtil.setRealUseDir(outputModuleDir);
		this.xmlDirectory = xmlDirectory;

		logger.info("xml directory is [{}]", xmlDirectory.getAbsolutePath());
		logger.info("Current user.dir is [{}]", SystemPropertyUtil.getUserDir());
		logger.info("mybatis config file name is [{}]", mybatisConfigFileName);
		logger.info("output module base directory is [{}]", outputModuleDir.getAbsolutePath());
	}

	/***
	 * 给出对应classpath的xml文件夹路径即可
	 * 使用默认的 MybatisConfig 文件名
	 */
	public static void create(String xmlDirectory) {
		create(DEFAULT_MYBATIS_CONFIG_FILE_NAME, xmlDirectory, null);
	}

	/**
	 * 输出到指定的module文件夹.
	 * @param xmlDirectory
	 * @param outputModuleDir 给出module的文件夹即可. 不需要包含: src
	 */
	public static void create(String xmlDirectory, File outputModuleDir) {
		create(DEFAULT_MYBATIS_CONFIG_FILE_NAME, xmlDirectory, outputModuleDir);
	}
		/***
         * 使用指定的MybatisConfig.xml 配置文件名 和 指定的xml路径
         * @param mybatisConfigFileName
         * @param xmlDirectory
         */
	public static void create(String mybatisConfigFileName, String xmlDirectory, File outputModuleDir) {
		URL url = Thread.currentThread().getContextClassLoader().getResource(xmlDirectory);
		try {
			assert url != null;
			create(mybatisConfigFileName, new File(url.toURI()), outputModuleDir);
		} catch (URISyntaxException e) {
			logger.error("xml directory ["+xmlDirectory+"] create exception:", e);
		}
	}
	/***
	 * 给出对应classpath的xml文件夹File
	 */
	public static void create(String mybatisConfigFileName, File xmlDirectory, File outputModuleDir) {
		if (! xmlDirectory.isDirectory()) {
			throw new CustomException("["+xmlDirectory.getAbsolutePath()+"] is not a directory");
		}
		if (outputModuleDir!= null && ! outputModuleDir.isDirectory()) {
			throw new CustomException("["+outputModuleDir.getAbsolutePath()+"] is not a directory");
		}

		try {
			new ProjectInitCreator(mybatisConfigFileName, xmlDirectory, outputModuleDir).process();
		}catch (Exception e) {
			logger.error("Create exception:", e);
		}finally {
			logger.info("Work Finished!");
		}
	}

	private void process(){
		this.processMybatisConfig();
		File[] listFiles = xmlDirectory.listFiles();
		if (listFiles == null) {
			throw new NullPointerException("No file in xmlDirectory ["+xmlDirectory+"]");
		}
		List<File> files = Arrays.stream(listFiles).sorted().collect(Collectors.toList());
		for (File file : files) {
			if (mybatisConfigFileName.equals(file.getName())) {
				continue;
			}

			EntityType entityType = EntityType.parse(file);
			EntityCreator entityCreator = new EntityCreator(entityType, file, Paths.get(mybatisConfig.getBaseDir(), mybatisConfig.getConfigDir()).toString());
			entityCreator.parse();

			mybatisConfig.addExtraFile(entityCreator.getEntityDefine().getNameSpace()+".xml");
			mybatisConfig.addAliasPackage(entityCreator.getEntityDefine().getPackageName() + ".entity");
		}

		// mybatis-config.xml 的输出
		String configPath = "";
		try {
			configPath = Paths.get(mybatisConfig.outputPath().toString(), mybatisConfig.getFileName()).toString();
			VelocityFactory.getInstance().parseOutFile("vm/mybatis_config_create.vm", configPath, mybatisConfig);
		}finally {
			logger.info("Create Mybatis Config [{}] Success!", configPath);
		}
	}


	private MybatisConfigDefine mybatisConfig;
	private void processMybatisConfig(){
		File mybatisConfigFile = new File(xmlDirectory.getPath(), mybatisConfigFileName);
		if (! mybatisConfigFile.exists() || ! mybatisConfigFile.isFile()) {
			logger.warn("mybatis config xml ["+mybatisConfigFileName+"] is not exist! use default config!");
			this.setMybatisConfig(new MybatisConfigDefine());
			return;
		}
		this.mybatisConfigFileName = mybatisConfigFile.getName();

		Digester digester = new Digester();
		digester.push(this);
		DigesterUtil.addObjectCreate(digester,"mybatis_config", MybatisConfigDefine.class, "setMybatisConfig");
		DigesterUtil.addObjectCreate(digester,"mybatis_config/extraConfigs/extra", MybatisExtraDefine.class, "addExtraFile");
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
