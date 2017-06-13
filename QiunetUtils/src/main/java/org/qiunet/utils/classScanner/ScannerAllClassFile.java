package org.qiunet.utils.classScanner;

import org.qiunet.utils.string.StringUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author qiunet
 *         Created on 17/1/23 18:22.
 */
public class ScannerAllClassFile {
	/**默认路径*/
	private String basePath;

	private List<String> allclass = new LinkedList<>();
	/**扫描匹配项列表*/
	private List<IScannerHandler> scannerHanderList = new ArrayList<>();

	private ClassLoader loader;

	/***
	 * 使用默认加载器
	 */
	public ScannerAllClassFile(){
		this.scannerFilePath(ScannerAllClassFile.class.getResource("/").getPath());
	}
	/***
	 * 添加路径的文件
	 * @param basePath
	 */
	public void scannerFilePath(String basePath) {
		this.basePath = basePath;
		if (";".equals(File.pathSeparator) && basePath.startsWith("/")) {
			this.basePath = basePath.substring(1, basePath.length());
		}
		this.listAllFiles(this.basePath);
		this.loader = Thread.currentThread().getContextClassLoader();
	}

	/**
	 * 找到当前basePath的所有的class
	 * @param path
	 */
	private void listAllFiles(String path){
		File file = new File(path);
		if(file.isFile() && file.getName().endsWith(".class")){
			String filePath = file.getPath();
			int endIndex = filePath.lastIndexOf(".class");
			allclass.add(filePath.substring(basePath.length(), endIndex).replace(File.separator, "."));
		}else if(file.isDirectory()){
			File[] files = file.listFiles();
			for (File f : files) {
				this.listAllFiles(f.getPath());
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
	}
}
