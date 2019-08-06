package org.qiunet.utils;

import org.qiunet.utils.string.StringUtil;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

public class FileUtil {
	private static final String loadWorkHomeName = ".xdProject";
	private static final String loadWorkFileName = ".xd.project";


	public static File returnWorkFile() {
		Path path = Paths.get(System.getProperty("user.home"), loadWorkHomeName, loadWorkFileName);
		File file = path.toFile();
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		return file;
	}

	/**
	 * @param dir
	 */
	public static void writeToProjectFile(File dir) {
		File workFile = returnWorkFile();

		System.out.println("WriteTo " + workFile.getAbsolutePath());
		PrintWriter print = null;
		try {
			String path = dir.getAbsolutePath().replaceAll("\\\\", "/");
			print = new PrintWriter(workFile, "UTF-8");
			print.println(returnProjectKey() + "=" + path);
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		} finally {
			if (print != null) print.close();
		}
	}

	public static String returnPathFromProjectFile() {
		File file = returnWorkFile();
		if (!file.exists()) return null;

		Properties properties = loadProperties(file);
		return properties.getProperty(returnProjectKey());
	}

	private static final Pattern patter = Pattern.compile("[^a-zA-Z]");

	private static String returnProjectKey() {
		String userDir = System.getProperty("user.dir");
		return patter.matcher(userDir).replaceAll("");
	}

	/***
	 * 加载一个properties
	 * @return
	 */
	public static Properties loadProperties(File file) {
		Properties tempProperties = new Properties();
		InputStreamReader isr = null;
		InputStream fis = null;
		try {
			fis = new FileInputStream(file);
			isr = new InputStreamReader(fis, "UTF-8");
			tempProperties.load(isr);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (isr != null) isr.close();
				if (fis != null) fis.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return tempProperties;
	}

	private static final FileFilter fileFilter = new FileFilter() {
		@Override
		public boolean accept(File file) {
			if (file.isHidden()) {
				return false;
			}
			if (file.isDirectory()) {
				return true;
			}
			if (isExcel(file.getPath())) {
				return true;
			}
			return false;
		}
	};

	public static List<File> getExcelArrays(String rootPath) {
		if (StringUtil.isEmpty(rootPath))
			return Collections.emptyList();
		File rootFile = new File(rootPath);
		if (!rootFile.exists())
			return Collections.emptyList();
		List<File> list = new ArrayList<>();
		File[] subFolders = rootFile.listFiles(fileFilter);

		if (subFolders != null && subFolders.length > 0) {
			for (File file : subFolders) {
				if (file.isFile()) {
					list.add(file);
				}
			}
		}
		return list;
	}

	public static boolean isExcel2003(String filePath) {
		return filePath.matches("^.+\\.(?i)(xls)$");
	}

	public static boolean isExcel2007(String filePath) {
		return filePath.matches("^.+\\.(?i)(xlsx)$");
	}

	public static boolean isExcel(String filePath) {
		return isExcel2003(filePath) || isExcel2007(filePath);
	}

}
