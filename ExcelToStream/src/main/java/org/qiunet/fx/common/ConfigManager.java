package org.qiunet.fx.common;

import org.qiunet.utils.properties.PropertiesUtil;
import org.qiunet.utils.string.StringUtil;

import java.io.*;
import java.net.URL;
import java.util.*;

public class ConfigManager {
	private volatile static ConfigManager instance;
	public static final String fileName = "config.properties";
	public static final String def_regex = ";";
	private Map<Object, Object> map;

	public static final String excel_path_array_key = "excel_path_array";
	public static final String last_check_excel_key = "last_check_excel";
	private Set<String> excel_path_array;


	public String getLast_check_excel() {
		return (String) map.getOrDefault(last_check_excel_key, "");
	}

	public Set<String> getExcel_path_array() {

		return excel_path_array;
	}

	public void loadExcel_path_array() {
		String str = (String) map.getOrDefault(excel_path_array_key, "");
		if (!StringUtil.isEmpty(str)) {
			excel_path_array = new HashSet<>(Arrays.asList(str.split(def_regex)));
		}
	}

	private ConfigManager() {
		map = PropertiesUtil.loadProperties(fileName).returnMap();
		loadExcel_path_array();
		instance = this;
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
			URL url = Thread.currentThread().getContextClassLoader().getResource(fileName);
			if (url.getPath().contains(".jar!")) {
				out = new FileOutputStream(fileName);
			} else {
				out = new FileOutputStream(url.getPath());
			}
			map.put(key, val);
			tempProperties.putAll(map);
			tempProperties.store(out, null);
			if (excel_path_array_key.equals(key))
				loadExcel_path_array();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) out.close();
			} catch (Exception e) {
				e.printStackTrace();
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

	public File newFile(String fileName) {
		try {
			File file = new File(fileName);
			if (!file.exists())
				file.createNewFile();
			return file;
		} catch (IOException e) {
			System.out.println(e);
		}
		return null;
	}

}
