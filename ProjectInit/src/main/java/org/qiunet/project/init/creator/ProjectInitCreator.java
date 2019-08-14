package org.qiunet.project.init.creator;

import org.qiunet.project.init.enums.EntityType;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;
import org.xml.sax.SAXParseException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by qiunet on 4/8/17.
 */
public final class ProjectInitCreator {
	private static Logger logger = LoggerType.DUODUO.getLogger();

	private ProjectInitCreator() {
	}

	/***
	 * 给出对应classpath的xml文件夹路径即可
	 */
	public static void create(String xmlDirectory) {
		URL url = Thread.currentThread().getContextClassLoader().getResource(xmlDirectory);
		try {
			create(new File(url.toURI()));
		} catch (URISyntaxException e) {
			logger.error("xml directory ["+xmlDirectory+"] create exception:", e);
		}
	}
	/***
	 * 给出对应classpath的xml文件夹File
	 */
	public static void create(File xmlDirectory) {
		if (! xmlDirectory.isDirectory()) {
			throw new RuntimeException("["+xmlDirectory.getAbsolutePath()+"] is not a directory");
		}

		for (File file : xmlDirectory.listFiles()) {
			EntityType entityType = EntityType.parse(file);

			try {
				entityType.validate(new FileInputStream(file));
			}catch (Exception e) {
				logger.error("file ["+file.getName()+"] create exception:", e);
			}
		}
	}
}
