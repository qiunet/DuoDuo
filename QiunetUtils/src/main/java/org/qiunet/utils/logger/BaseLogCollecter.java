package org.qiunet.utils.logger;

import org.qiunet.utils.string.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志的收集
 * 子类一般单例即可 , 以后转远程日志. 也不需要费劲
 * Created by qiunet.
 * 18/2/4
 */
public abstract class BaseLogCollecter {
//
//	public static final String DEFAULT_DATE_PATTERN = "'.'yy-MM-dd";
//	public static final String DEFAULT_LOG_PATTERN = "[%d{yyyy-MM-dd HH:mm:ss}] %m%n";
//
//	private String logPath;
	/**日志的分割符号*/
	protected String separator;
//	/**日期格式*/
//	private String dataPattern;
//	/**日志的记录格式*/
//	private String logPattern;
//
//	private BaseLogCollecter(String logPath, String dataPattern, String logPattern, String separator) {
//		this.logPath = logPath;
//		this.separator = separator;
//		this.dataPattern = dataPattern;
//		this.logPattern = logPattern;
//	}
//
//	private BaseLogCollecter(String logPath, String separator) {
//		this(logPath,DEFAULT_DATE_PATTERN,  DEFAULT_LOG_PATTERN ,separator);
//	}
//
//	protected BaseLogCollecter(String logPath) {
//		this(logPath, "|");
//	}

	protected BaseLogCollecter( String separator) {
		this.separator = separator;
	}

	/***
	 * 记录日志
	 * @param fileName 日志的文件名
	 * @param args 自定义的参数. 最后按照拼串的方式记录
	 */
	protected void log(String fileName, Object... args) {
		String logMsg = StringUtil.arraysToString(args, separator);
		Logger logger = LoggerFactory.getLogger(fileName);
		logger.info(logMsg);
	}

//	/**
//	 * 创建一个合适的logger
//	 * @param fileName 加入的文件名
//	 * @return
//	 */
//	private synchronized Logger createLogger(String fileName) {
//		Logger logger;
//		if ((logger = LogManager.exists(fileName)) != null) return logger;
//
//		try {
//			String filePath = logPath+ File.separator+fileName;
//			logger = Logger.getLogger(fileName);
//			logger.setAdditivity(false);
//			logger.setLevel(Level.INFO);
//
//			PatternLayout layout = new PatternLayout();
//			layout.setConversionPattern(logPattern);
//
//			FileAppender appender = new DailyRollingFileAppender(layout, filePath, dataPattern);
//			appender.setName("defaultAppender");
//			appender.setEncoding("UTF-8");
//			appender.activateOptions();
//			logger.addAppender(appender);
//
//			loggerMap.putIfAbsent(fileName, logger);
//			logger = loggerMap.get(fileName);
//		} catch (IOException e) {
//			LoggerFactory.getLogger(LoggerType.QIUNET_UTILS).info("Exception", e);
//		}
//		return logger;
//	}
//
//	/***
//	 * 重新加载日志的格式数据
//	 * @param dataPattern 滚动日期格式
//	 * @param logPattern 日志的格式
//	 */
//	public void reload(String dataPattern, String logPattern) {
//		for (Logger logger : loggerMap.values()) {
//			Enumeration<Appender> it = logger.getAllAppenders();
//			while (it.hasMoreElements()) {
//				Appender appender = it.nextElement();
//				((PatternLayout) (appender).getLayout()).setConversionPattern(logPattern);
//				((DailyRollingFileAppender) appender).setDatePattern(dataPattern);
//			}
//		}
//	}
}
