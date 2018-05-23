package org.qiunet.utils.classScanner;

import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.string.StringUtil;
import org.qiunet.utils.system.SystemPropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author qiunet
 *         Created on 17/1/23 18:22.
 */
public class ScannerAllClassFile {
	private Logger logger = LoggerFactory.getLogger(LoggerType.DUODUO);
	private List<String> allclass = new LinkedList<>();
	/**扫描匹配项列表*/
	private List<IScannerHandler> scannerHanderList = new ArrayList<>();

	private ClassLoader loader;

	/***
	 * 使用默认加载器
	 */
	public ScannerAllClassFile(){
		this.loader = Thread.currentThread().getContextClassLoader();
		String classPath  = System.getProperty("java.class.path");
		String [] paths = StringUtil.split(classPath, SystemPropertyUtil.getPathSeparator());
		Set<String> pathSet = new HashSet();
		for (String path : paths) {
			if (path.endsWith(".jar")) continue;

			pathSet.add(formatPath(path));
		}
		String classesPath = ScannerAllClassFile.class.getResource("/").getPath();
		pathSet.add(formatPath(classesPath));

		String userdir = System.getProperty("user.dir");
		pathSet.remove(formatPath(userdir));

		for (String path : pathSet) {
			this.scannerFilePath(path);
		}
	}

	/***
	 * 格式化路径
	 * @param path
	 * @return
	 */
	private String formatPath(String path) {
		try {
			return new File(path).toURI().toURL().getPath();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}
	/***
	 * 添加路径的文件
	 * @param basePath
	 */
	public void scannerFilePath(String basePath) {
		try {
			String basepath = formatPath(basePath);
			this.listAllFiles(basepath, basepath);
		} catch (MalformedURLException e) {
			logger.error("["+getClass().getSimpleName()+"] Exception: ", e);
		}
	}
	/***
	 * 对jar文件操作
	 * 如果 Managers.class 在jar包里面的话.使用下面的语句得到url
	 * scanner.scannerJarFile(Managers.class.getResource("").toURI().toURL())
	 * @param url
	 */
	public void scannerJarFile(URL url){
		if (!"jar".equals(url.getProtocol())) return;
		try {
			JarURLConnection conn = (JarURLConnection) url.openConnection();
			JarFile jarFile = conn.getJarFile();
			Enumeration<JarEntry> entries = jarFile.entries();
			while (entries.hasMoreElements()){
				JarEntry entry = entries.nextElement();
				if (entry.getName().endsWith(".class")) {
					int endIndex = entry.getName().lastIndexOf(".class");
					allclass.add(entry.getName().substring(0, endIndex).replace("/", "."));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 找到当前basePath的所有的class
	 * @param path
	 */
	private void listAllFiles(String basepath, String path) throws MalformedURLException {
		File file = new File(path);
		if(file.isFile() && file.getName().endsWith(".class")){
			String filePath = file.toURI().toURL().getPath();
			int endIndex = filePath.lastIndexOf(".class");
			allclass.add(filePath.substring(basepath.length(), endIndex).replace("/", "."));
		}else if(file.isDirectory()){
			File[] files = file.listFiles();
			for (File f : files) {
				this.listAllFiles(basepath, f.getPath());
			}
		}
	}

	/**
	 * 添加扫描处理
	 * @param handler
	 */
	public void addScannerHandler(IScannerHandler handler) {
		this.scannerHanderList.add(handler);
	}
	/***
	 * 执行, 并且回调handler
	 */
	public void scanner() throws ClassNotFoundException {
		if (scannerHanderList.isEmpty()) return;

		for (String clazz : allclass) {
			Class c = loader.loadClass(clazz);
			for (IScannerHandler handler : scannerHanderList) {
				if (handler.matchClazz(c))		handler.handler(c);
			}
		}
		this.allclass = null;

	}
}
