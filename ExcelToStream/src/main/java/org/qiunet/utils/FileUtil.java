package org.qiunet.utils;

import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.regex.Pattern;

public class FileUtil {
	private static final String loadWorkHomeName = ".xdProject";
	private static final String loadWorkFileName = ".xd.project";

	private static File returnWorkFile(){
		Path path = Paths.get(System.getProperty("user.home") , loadWorkHomeName, loadWorkFileName);
		File file = path.toFile();
		if (! file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		return file;
	}
	/**
	 *
	 * @param dir
	 */
	public static void writeToProjectFile(File dir) {
		File workFile = returnWorkFile();

		System.out.println("WriteTo " + workFile.getAbsolutePath());
		PrintWriter print = null;
		try {
			String path = dir.getAbsolutePath().replaceAll("\\\\", "/");
			print = new PrintWriter(workFile, "UTF-8");
			print.println(returnProjectKey()+"="+path);
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		} finally {
			if (print != null) print.close();
		}
	}

	public static String returnPathFromProjectFile(){
		File file = returnWorkFile();
		if (! file.exists()) return null;

		Properties properties = loadProperties(file);
		return properties.getProperty(returnProjectKey());
	}

	private static final Pattern patter = Pattern.compile("[^a-zA-Z]");
	private static String returnProjectKey(){
		String userDir = System.getProperty("user.dir");
		return patter.matcher(userDir).replaceAll("");
	}
	/***
	 * 加载一个properties
	 * @return
	 */
	public static Properties loadProperties(File file) {
		Properties tempProperties = new Properties();
		InputStreamReader isr = null ;
		InputStream fis = null;
		try {
			fis = new FileInputStream(file);
			isr = new InputStreamReader(fis , "UTF-8");
			tempProperties.load(isr);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (isr != null) isr.close();
				if (fis != null) fis.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return tempProperties;
	}
}
