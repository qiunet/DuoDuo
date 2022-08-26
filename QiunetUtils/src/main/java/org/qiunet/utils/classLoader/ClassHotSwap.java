package org.qiunet.utils.classLoader;

import com.google.common.collect.Lists;
import com.sun.tools.attach.VirtualMachine;
import org.qiunet.utils.file.FileUtil;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.string.StringUtil;
import org.qiunet.utils.system.OSUtil;
import org.qiunet.utils.system.SystemPropertyUtil;
import org.qiunet.utils.timer.TimerManager;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;

/***
 * 对class 进行热替换
 */
public final class ClassHotSwap {
	private static final Logger logger = LoggerType.DUODUO.getLogger();
	/***
	 * 传入 class 所在的绝对地址路径
	 * class 文件不需要有层级结构 直接在里面就ok
	 * @param classesParentPath
	 */
	public static void hotSwap(Path classesParentPath) {

		File classDirectory = classesParentPath.toFile();
		if (! classDirectory.isDirectory()) {
			logger.error(" classesParentPath must be a directory!");
			return;
		}

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
		List<File> fileList = Lists.newArrayList();
		FileUtil.listFile(classesParentPath, fileList, file -> file.getName().endsWith(".class"));
		if (fileList.isEmpty()) {
			logger.error("Class list is empty!");
			return;
		}

		VirtualMachine vm = null;
		try {
			vm = VirtualMachine.attach(String.valueOf(OSUtil.pid()));
			vm.loadAgent(JavaAgentJarPath, classDirectory.getAbsolutePath());
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
			TimerManager.executor.scheduleWithDelay(() -> {
				try {
					FileUtil.cleanDirectory(classDirectory);
					logger.error("清空热更文件夹成功!");
					return true;
				}catch (Exception e) {
					logger.error("清空热更文件夹失败!", e);
					return false;
				}
			}, 30, TimeUnit.SECONDS);
		}
	}
}
