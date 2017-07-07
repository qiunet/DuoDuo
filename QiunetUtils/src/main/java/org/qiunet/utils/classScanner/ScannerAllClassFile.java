package org.qiunet.utils.classScanner;

import org.qiunet.utils.string.StringUtil;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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
		try {
			this.basePath = new File(basePath).toURI().toURL().getPath();
			this.listAllFiles(this.basePath);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		this.loader = Thread.currentThread().getContextClassLoader();
	}
	/***
	 * 对jar文件操作
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
	private void listAllFiles(String path) throws MalformedURLException {
		File file = new File(path);
		if(file.isFile() && file.getName().endsWith(".class")){
			String filePath = file.toURI().toURL().getPath();
			int endIndex = filePath.lastIndexOf(".class");
			allclass.add(filePath.substring(basePath.length(), endIndex).replace("/", "."));
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
		this.allclass.clear();
	}
}
