package org.qiunet.fx.common;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.qiunet.fx.bean.ConfigData;
import org.qiunet.utils.FileUtil;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.string.StringUtil;
import org.slf4j.Logger;

import java.io.*;
import java.util.*;

public class ConfigManager {
	private volatile static ConfigManager instance;
	public static final String def_regex = ";";
	private Map<Object, Object> map;

	private static final Logger logger = LoggerType.DUODUO.getLogger();

	public static final String excel_path_array_key = "excel_path_array";
	public static final String last_check_excel_key = "last_check_excel";
	public static final String out_path_key = "out_path";
	public static final String config_data_key = "config_data";

	private Set<String> excel_path_array;
	private Map<String, String> out_path;

	private List<ConfigData> configDataList;

	public String getLast_check_excel() {
		return (String) map.getOrDefault(last_check_excel_key, "");
	}

	public Set<String> getExcel_path_array() {
		return excel_path_array;
	}

	public boolean isHasPath(String path) {
		return getData(path)!=null;
	}

	public void loadOutPath() {
		String str = (String) map.get(out_path_key);
		if (StringUtil.isEmpty(str)) {
			out_path = new HashMap<>();
		} else {
			out_path = JsonUtil.getGeneralObject(str, Map.class);
		}

	}


	public void loadExcel_path_array() {
		String str = (String) map.getOrDefault(excel_path_array_key, "");
		if (!StringUtil.isEmpty(str)) {
			excel_path_array = new HashSet<>(Arrays.asList(str.split(def_regex)));
		} else {
			excel_path_array = new HashSet<>();
		}
	}

	private ConfigManager() {
		File file = FileUtil.returnWorkFile();
		if (file != null && file.exists()) {

			Properties properties = FileUtil.loadProperties(file);
			map = properties;
		} else {
			map = new HashMap<>();
		}
		instance = this;
		loadExcel_path_array();
		loadOutPath();

		String str = (String) map.get(config_data_key);
		if (StringUtil.isEmpty(str)) {
			configDataList = new ArrayList<>(4);
		} else {
			configDataList = JsonUtil.getGeneralList(str, ConfigData.class);
		}
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


	public boolean writeOutPath(String excelPath, String outPath) {
		if (out_path.containsKey(excelPath) && out_path.get(excelPath).equals(outPath))
			return true;
		out_path.put(excelPath, outPath);
		return write(out_path_key, JsonUtil.toJsonString(out_path), false);
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
			logger.error("写入数据出错!", ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (out != null) out.close();
			} catch (Exception e) {
				logger.error("关闭资源出错!", ExceptionUtils.getStackTrace(e));
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

	public Map<String, String> getOut_path() {
		return out_path;
	}

	public void addConfigData(String excelPath, boolean xml, boolean xd, boolean json, String outPath) {
		ConfigData data = getData(excelPath);
		if (data == null) data = new ConfigData(excelPath);
		data.setJson(json);
		data.setXd(xd);
		data.setXml(xml);
		data.addOutPath(outPath);
		if (configDataList == null) configDataList = new ArrayList<>(4);
		if (configDataList.contains(data)) configDataList.remove(data);
		configDataList.add(0,data);
		String jsonStr = JsonUtil.toJsonString(configDataList);
		write(config_data_key, jsonStr, false);
	}

	public ConfigData getData(String excelPath) {
		if (StringUtil.isEmpty(excelPath)) return null;
		if (configDataList == null || configDataList.isEmpty()) return null;
		for (ConfigData data : configDataList) {
			if (excelPath.equals(data.getExcelPath())) return data;
		}
		return null;
	}

	public List<ConfigData> getConfigDataList() {
		return configDataList;
	}
}
