package org.qiunet.utils.classLoader;

import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.classfile.ClassFile;
import org.qiunet.agent.ClassInfos;
import org.qiunet.utils.data.IKeyValueData;
import org.qiunet.utils.encryptAndDecrypt.MD5Util;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.properties.PropertiesUtil;
import org.qiunet.utils.string.StringUtil;
import org.qiunet.utils.system.OSUtil;
import org.qiunet.utils.system.SystemPropertyUtil;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

/***
 * 对class 进行热替换
 */
public final class ClassHotSwap {
	private static Logger logger = LoggerType.DUODUO.getLogger();
	/***
	 * 传入 class 所在的绝对地址路径
	 * class 文件不需要有层级结构 直接在里面就ok
	 * @param classesParentPath
	 */
	public static void hotSwap(Path classesParentPath) {
		ClassInfos classInfos = new ClassInfos();

		File classDirectory = classesParentPath.toFile();
		if (! classDirectory.isDirectory()) {
			logger.error(" classesParentPath must be a directory!");
			return;
		}
		classInfos.setClassPath(classDirectory.getAbsolutePath());

		String classPath  = System.getProperty("java.class.path");
		String [] paths = StringUtil.split(classPath, SystemPropertyUtil.getPathSeparator());
		String JavaAgentJarPath = null;
		for (String path : paths) {
			if (!path.endsWith(".jar")) continue;

			String fileName = Paths.get(path).toFile().getName();
			if (fileName.matches("JavaAgent.*jar")) {
				JavaAgentJarPath = path;
				break;
			}
		}
		if (JavaAgentJarPath == null) {
			logger.error(" JavaAgentJarPath is null!");
			return;
		}
		/* 文件名=md5 */
		File md5File = new File(classDirectory, "HotSwapMd5.properties");
		if (! md5File.exists()) {
			logger.error("HotSwapMd5.properties is not found!");
			return;
		}

		IKeyValueData<Object, Object> md5Details = PropertiesUtil.loadProperties(md5File);
		for (Map.Entry<Object, Object> entry : md5Details.returnMap().entrySet()) {
			String fileName = entry.getKey().toString();
			String md5String = entry.getValue().toString();

			File clazzFile = new File(classDirectory, fileName);
			if (clazzFile.isDirectory()) {
				logger.error("File {} is a directory", fileName);
				return;
			}

			if (! fileName.endsWith(".class")) {
				logger.error("File {} is a class File", fileName);
				return;
			}

			try {
				if (! md5String.equals(MD5Util.encrypt(clazzFile))) {
					logger.error("File {}` md5 {} is not match specify md5 {} in HotSwapMd5.properties", fileName, md5String, MD5Util.encrypt(clazzFile));
					return;
				}

				ClassFile classFile = ClassFile.read(clazzFile);
				classInfos.addClass(classFile.getName());
			} catch (Exception e) {
				logger.error(clazzFile.getName()+"ERROR", e);
			}
		}

		VirtualMachine vm = null;
		try {
			vm = VirtualMachine.attach(String.valueOf(OSUtil.pid()));
			vm.loadAgent(JavaAgentJarPath, classInfos.toString());
		} catch (Exception e) {
			logger.error("VirtualMachine attach ERROR", e);
		}finally {
			if (vm != null) {
				try {
					vm.detach();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
