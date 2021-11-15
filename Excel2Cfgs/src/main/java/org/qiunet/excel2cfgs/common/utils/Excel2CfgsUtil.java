package org.qiunet.excel2cfgs.common.utils;

import org.qiunet.excel2cfgs.setting.Setting;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;

/***
 *
 * @author qiunet
 */
public class Excel2CfgsUtil {
	/***配置文件的文件夹名 相对于 user.home */
	private static final String SETTING_DIR_NAME = ".excelToCfg";
	/***配置文件的文件名**/
	private static final String SETTING_FILE_NAME = ".setting";
	/**excel 的后缀**/
	private static final String[] EXCEL_POSTFIXS = new String[]{"xlsx", "xls"};

	public static File returnWorkFile() {
		Path path = Paths.get(System.getProperty("user.home"), SETTING_DIR_NAME, SETTING_FILE_NAME);
		File file = path.toFile();
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		return file;
	}

	/***
	 * 写入setting 到 文件
	 * @param setting
	 */
	public static void writeToProjectFile(Setting setting) {
		File workFile = returnWorkFile();

		FileUtil.createFileWithContent(workFile, JsonUtil.toJsonString(setting));
	}

	/***
	 * 从配置文件读取配置内容
	 * @return
	 */
	public static String returnPathFromProjectFile() {
		File file = returnWorkFile();
		if (!file.exists()) {
			return null;
		}

		try {
			return FileUtil.getFileContent(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static final boolean filePostfixCheck(File file) {
		String fileName = file.getName();

		if (fileName.startsWith("~") || fileName.startsWith(".")) {
			return false;
		}

		if (file.isFile()) {
			if (file.getName().contains(".")) {
				String postfix = file.getName().substring(file.getName().indexOf(".") + 1);
				return existInList(postfix, EXCEL_POSTFIXS);
			}
		}
		return true;
	}

	/**
	 * 是否是windows
	 * @return
	 */
	public static boolean isWindows() {
		String os = System.getProperty("os.name");
		return os.startsWith("Windows");
	}

	public static void reverse(byte [] array, int step) {
		if (array == null || array.length <= 1) {
			return;
		}
		byte temp;
		int len = array.length;
		int loopNum = len / 2;
		for (int i = 0; i < loopNum; i+=step) {
			temp = array[i];
			int last = len - 1 - i;
			array[i] = array[last];
			array[last] = temp;
		}
	}

	/**
	 * 检查一个元素是否在数组中
	 * @param <T>
	 * @param arrays
	 * @return
	 */
	public static <T> boolean existInList(T element,T ... arrays)
	{
		if (arrays == null || element == null) {
			return false;
		}
		return Arrays.asList(arrays).contains(element);
	}

	/**
	 * 检查一个元素是否在集合中
	 * @param <T>
	 * @param element
	 * @param list
	 * @return
	 */
	public static <T> boolean existInList(T element, Collection<T> list)
	{
		if(list.isEmpty() || element == null) {
			return false;
		}
		return list.stream().anyMatch(ele -> ele.equals(element));
	}
}
