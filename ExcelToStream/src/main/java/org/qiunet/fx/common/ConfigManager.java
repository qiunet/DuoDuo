package org.qiunet.fx.common;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.qiunet.utils.FileUtil;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.string.StringUtil;
import org.slf4j.Logger;

import java.io.*;
import java.util.*;

public class ConfigManager {
	private volatile static ConfigManager instance;
	public static final String def_regex = ";";
	private Map<Object, Object> map;

	private static  final Logger logger=LoggerType.DUODUO.getLogger();

	public static final String excel_path_array_key = "excel_path_array";
	public static final String last_check_excel_key = "last_check_excel";

	private Set<String> excel_path_array;


	public String getLast_check_excel() {
		return (String) map.getOrDefault(last_check_excel_key, "");
	}

	public Set<String> getExcel_path_array() {
		return excel_path_array;
	}

	public  boolean isHasPath(String path){
		if(excel_path_array==null) return  false;
		return  excel_path_array.contains(path);
	}

	public void loadExcel_path_array() {
		String str = (String) map.getOrDefault(excel_path_array_key, "");
		if (!StringUtil.isEmpty(str)) {
			excel_path_array = new HashSet<>(Arrays.asList(str.split(def_regex)));
		}else {
			excel_path_array=new HashSet<>();
		}
	}

	private ConfigManager() {
		Properties properties = FileUtil.loadProperties(FileUtil.returnWorkFile());
		map = properties;
		instance = this;
		loadExcel_path_array();
	}

	public static ConfigManager getInstance() {
		if (instance == null) {
			synchronized (ConfigManager.class) {
				if (instance == null) {
					new ConfigManager();
				}
			}
		}
		return instance;
	}

	public boolean write(String key, String val, boolean append) {
		if (append) {
			val = appendVal((String) map.getOrDefault(key, ""), val, def_regex);
		}
		if (!isWrite(key, val))
			return true;

		Properties tempProperties = new Properties();
		OutputStream out = null;
		try {
			out = new FileOutputStream(FileUtil.returnWorkFile());
			map.put(key, val);
			tempProperties.putAll(map);
			tempProperties.store(out, null);
			if (excel_path_array_key.equals(key))
				loadExcel_path_array();
			return true;
		} catch (Exception e) {
			logger.error("写入数据出错!",ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (out != null) out.close();
			} catch (Exception e) {
				logger.error("关闭资源出错!",ExceptionUtils.getStackTrace(e));
			}
		}
		return false;

	}

	public boolean isWrite(String key, String newVal) {
		if (!map.containsKey(key)) return true;
		return !map.get(key).equals(newVal);
	}

	public String appendVal(String old, String appendVal, String regex) {
		if (StringUtil.isEmpty(appendVal)) return old;
		StringBuffer sb = new StringBuffer();
		if (StringUtil.isEmpty(old)) {
			sb.append(appendVal);
		} else {
			String[] strs = StringUtil.split(old, regex);
			Set<String> set = new HashSet<>(Arrays.asList(strs));
			if (!set.contains(appendVal)) sb.append(old).append(regex).append(appendVal);
		}
		return sb.toString();
	}
}
