package org.qiunet.acceptor.log;

import org.apache.log4j.*;
import org.apache.log4j.spi.LoggingEvent;
import org.qiunet.acceptor.cfg.ConfigManager;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by qiunet.
 * 17/9/24
 */
public class LoggerUtil {
	private static final Logger outLogger = Logger.getLogger("catalina.out");
	static {
		Logger root = Logger.getRootLogger();

		outLogger.setAdditivity(false);
		outLogger.setLevel(Level.INFO);
		PatternLayout layout = new PatternLayout();
		layout.setConversionPattern("[%d{yyyy/MM/dd HH:mm:ss}] %m%n");
		String osName = System.getProperty("os.name");
		Appender appender = null;
		if (osName.contains("Mac") || osName.contains("Windows")) {
			appender = new ConsoleAppender(layout);
			((ConsoleAppender) appender).activateOptions();
		}else {
			try {
				appender = new DailyRollingFileAppender(layout, ConfigManager.getInstance().getLogPath()+"catalina.out", "_yyyy-MM-dd");
				((DailyRollingFileAppender) appender).activateOptions();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		root.addAppender(appender);
		outLogger.addAppender(appender);
	}

	/***
	 * 得到输出打印的日志
	 * @return
	 */
	public static Logger getOutLogger() {
		return outLogger;
	}

	/**       存储logger */
	private static final ConcurrentHashMap<String , Logger> loggerMap = new ConcurrentHashMap<>();
	/***
	 * 记录日志
	 * @param gameId
	 * @param dtSeconds
	 * @param msg
	 */
	public static void logAppend(short gameId, final int dtSeconds, String msg) {
		int index = msg.indexOf('|');
		if (index < 0) {
			outLogger.error("msg: ["+msg+"] is error");
			return;
		}

		Logger logger = getLogger(gameId, msg.substring(0, index));
		logger.callAppenders(new LoggingEvent(Category.class.getName(), logger,dtSeconds*1000L, logger.getLevel(), msg.substring(index+1), null ));
	}
	/***
	 * 根据 gameid 和 prefix 输出日志
	 * @param gameId
	 * @param prefix
	 * @return
	 */
	private static Logger getLogger(short gameId, String prefix) {
		String loggerName = String.valueOf(gameId) + "_" + prefix;
		Logger logger = loggerMap.get(loggerName);
		if (logger != null) return logger;

		String filePath = ConfigManager.getInstance().getLogPath()+gameId+ File.separator+prefix;
		synchronized (LoggerUtil.class) {
			if ((logger = LogManager.exists(loggerName)) != null) return logger;
			try {
				logger = Logger.getLogger(loggerName);
				logger.setAdditivity(false);
				logger.setLevel(Level.INFO);

				PatternLayout layout = new PatternLayout();
				layout.setConversionPattern(ConfigManager.getInstance().getlogConversionPattern());
				FileAppender appender = new DailyRollingFileAppender(layout, filePath, ConfigManager.getInstance().getDatePattern());
				appender.setName("defaultAppender");
				appender.setEncoding("UTF-8");
				appender.activateOptions();
				logger.addAppender(appender);

				loggerMap.putIfAbsent(loggerName, logger);
				logger = loggerMap.get(loggerName);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return logger;
	}

	/***
	 * 如果log日志变动了. 就清理该map 重新生成
	 */
	public static void cleanLogger(){
		loggerMap.clear();
	}
}
