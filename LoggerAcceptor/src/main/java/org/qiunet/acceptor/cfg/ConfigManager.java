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
				Map oldMap = (Map) field.get(this);
				reload();
				Map newMap = (Map) field.get(this);

				if (first){
					first = false;
					continue;
				}

				for (Object nkey : newMap.keySet()) {
					if (! oldMap.containsKey(nkey)) {
						logger.info("Config add entry key ["+nkey+"] value ["+newMap.get(nkey)+"]");
						continue;
					}

					if (! oldMap.get(nkey).equals(newMap.get(nkey))) {
						logger.info("Config modify entry key ["+nkey+"] value ["+newMap.get(nkey)+"]");
						if (nkey.toString().startsWith("log_")) LoggerUtil.cleanLogger();
						continue;
					}
				}

				for (Object okey : oldMap.keySet()) {
					if (! newMap.containsKey(okey)) {
						logger.info("Config remove entry key ["+okey+"] value ["+oldMap.get(okey)+"]");
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

}
