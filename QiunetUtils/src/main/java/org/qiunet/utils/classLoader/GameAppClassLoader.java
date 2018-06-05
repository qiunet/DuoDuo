package org.qiunet.utils.classLoader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 用来加载自己指定的一些类
 * @author qiunet
 *         Created on 17/1/24 09:53.
 */
public class GameAppClassLoader extends URLClassLoader {

	private String [] allowLoaderNames;
	/**
	 *
	 * @param paths
	 */
	public GameAppClassLoader(String [] paths, String [] allowLoaderNames) {
		super(returnURLs(paths).toArray(new URL[0]));
		this.allowLoaderNames = allowLoaderNames;
	}

	/**
	 * 指定该加载器的父类加载器
	 * @param paths
	 * @param parent
	 */
	public GameAppClassLoader(String [] paths, String [] allowLoaderNames, ClassLoader parent) {
		super(returnURLs(paths).toArray(new URL[0]), parent);
		this.allowLoaderNames = allowLoaderNames;
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		Class<?> clazz = findLoadedClass(name);
		if (clazz != null) return clazz;
		boolean loader = false;

		for (String allow : allowLoaderNames){
			if ( loader = name.startsWith(allow)) break;
		}
		if (loader) {
			byte [] bytes = getClassDefineBytes(name);
			return defineClass(name, bytes, 0 , bytes.length);
		}
		return super.findClass(name);
	}

	/**
	 * 返回指定name的class byte 字节
	 * @param name
	 * @return
	 */
	protected byte [] getClassDefineBytes(String name) throws ClassNotFoundException {
		String realName = name.replace('.',File.separatorChar) + ".class";
		for (URL url : getURLs()) {
			if (url.getPath().endsWith(realName)){
				try {
					return getClassDefineBytes(url);
				} catch (Exception e) {
					throw new ClassNotFoundException("["+name+"] not found");
				}
			}
		}
		return null;
	}

	/**
	 * 返回指定的url的class byte数组
	 * @param url
	 * @return
	 */
	protected byte [] getClassDefineBytes(URL url) throws Exception {
		return Files.readAllBytes(Paths.get(url.toURI()));
	}
	/**
	 * 找到路径中的jar和class文件
	 * @param paths
	 * @return
	 */
	private static List<URL> returnURLs(String [] paths){
		List<URL> urls = new ArrayList<>();
		for (String path : paths) {
			try {
				File folder = new File(path);
				File [] files = folder.listFiles();
				if (files == null) return urls;

				for (File file : files) {
					if (! file.isDirectory()){
						String fileName = file.getName();
						if (!fileName.endsWith("jar") && !fileName.endsWith("class")) continue;

						urls.add(file.toURI().toURL());
					}else{
						urls.addAll(returnURLs(new String[]{file.getPath()} ));
					}
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		return urls;
	}
}
