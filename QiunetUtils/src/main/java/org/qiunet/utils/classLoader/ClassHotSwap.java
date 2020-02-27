package org.qiunet.utils.classLoader;

import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.classfile.ClassFile;
import org.qiunet.agent.ClassInfos;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.string.StringUtil;
import org.qiunet.utils.system.OSUtil;
import org.qiunet.utils.system.SystemPropertyUtil;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

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
		File[] listFiles = classDirectory.listFiles();
		if (listFiles == null) {
			throw new NullPointerException("No class file in class hot swap dir ["+classesParentPath.toString()+"]");
		}
		for (File clazzFile : listFiles) {
			if (clazzFile.isDirectory() || !clazzFile.getName().endsWith(".class")) continue;

			try {
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
