package org.qiunet.acceptor.cfg;

import org.apache.log4j.Logger;
import org.qiunet.acceptor.log.LoggerUtil;
import org.qiunet.utils.properties.LoaderProperties;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by qiunet.
 * 17/9/14
 */
public class ConfigManager extends LoaderProperties implements Runnable {
	private Logger logger = LoggerUtil.getOutLogger();
	private volatile static ConfigManager instance;
	private Thread thread;
	private ConfigManager() {
		super("config.properties");
		if (instance != null) throw new RuntimeException("Instance Duplication!");
		thread = new Thread(this, "ConfigManager");
		thread.setDaemon(true);
		thread.start();
		instance = this;
	}

	public static ConfigManager getInstance() {
		if (instance == null) {
			synchronized (ConfigManager.class) {
				if (instance == null)
				{
					new ConfigManager();
				}
			}
		}
		return instance;
	}
	/**
	 * 得到日志的输出路径
	 * @return
	 */
	public String getLogPath(){
		String path = getString("log_data_path");
		if (! path.endsWith(File.separator)) {
			path += File.separator;
		}
		return path;
	}
	/***
	 * 得到日志的时间格式
	 * @return
	 */
	public String getDatePattern(){
		return getString("log_date_pattern");
	}
	/***
	 * 得到secret
	 * @param gameid
	 * @return
	 */
	public String getSecret(short gameid) {
		return getString(String.valueOf(gameid));
	}
	/***
	 * 得到日志的打印数据
	 * @return
	 */
	public String getlogConversionPattern(){
		return getString("log_conversionPattern");
	}
	@Override
	public void run() {
		boolean first = true;
		for (;;) {
			try {
				Thread.sleep(10000);
				Field field = this.getClass().getSuperclass().getSuperclass().getDeclaredField("map");
				field.setAccessible(true);
				Map<Object, Object> oldMap = (Map<Object, Object>) field.get(this);
				reload();
				Map<Object, Object> newMap = (Map<Object, Object>) field.get(this);

				if (first){
					first = false;
					continue;
				}

				for (Map.Entry entry : newMap.entrySet()) {
					if (! oldMap.containsKey(entry.getKey())) {
						logger.info("Config add entry key ["+entry.getKey()+"] value ["+entry.getValue()+"]");
						continue;
					}

					if (! oldMap.get(entry.getKey()).equals(entry.getValue())) {
						logger.info("Config modify entry key ["+entry.getKey()+"] value ["+entry.getValue()+"]");
						if (entry.getKey().toString().startsWith("log_")) LoggerUtil.cleanLogger();
						continue;
					}
				}

				for (Map.Entry entry : oldMap.entrySet()) {
					if (! newMap.containsKey(entry.getKey())) {
						logger.info("Config remove entry key ["+entry.getKey()+"] value ["+entry.getValue()+"]");
					}
				}
			} catch (InterruptedException | NoSuchFieldException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

}
