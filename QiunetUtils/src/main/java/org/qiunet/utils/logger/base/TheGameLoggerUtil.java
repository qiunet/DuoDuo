package org.qiunet.utils.logger.base;

import org.apache.log4j.Appender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.qiunet.utils.threadLocal.ThreadContextData;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author qiunet
 *         Created on 17/1/9 08:21.
 */
public class TheGameLoggerUtil {
	/**存入 ThreadContextData 后, 日志打印往list. 异常后 调用TheGameLoggerUtil.printAllLoggerNormal() 输出日志 */
	private static final String APPENDER_SWITCH_TO_LIST  = "APPENDER_SWITCH_TO_LIST";
	/**日志的list key*/
	private static final String APPENDER_LIST_KEY = "QIUNET_UTILS_APPENDER_LIST";
	/***
	 * 过滤一组byte. 替换\n 为空格
	 * @param b 数组
	 * @param off 起始位置
	 * @param len 长度
	 * @return 
	 */
	public static byte [] trimLineBreak(byte [] b, int off , int len){
		// 异常打印一行. 没法看
		if (len > 300) return b;
		
		for (int i = off+1; i < len-1; i++) if (b[i] == 10) b[i] = 16;
		return b;
	}
	
	/***
	 * 当前是否是写到list
	 * @return
	 */
	public static boolean isAppenderToList(){
		return ThreadContextData.get(TheGameLoggerUtil.APPENDER_SWITCH_TO_LIST) != null;
	}
	/***
	 * 设置 所有日志写到list, 出错后自行处理
	 * error 级别的日志不受影响, 会直接打印
	 */
	public static void startAppenderLoggerToList(){
		ThreadContextData.put(APPENDER_SWITCH_TO_LIST, "");
	}
	/**
	 * 记录日志到线程变量的list
	 * error 级别的日志不受影响, 会直接打印
	 * @param event
	 * @return appender to super
	 */
	public static boolean AppenderLoggerToThreadLocal(String name ,LoggingEvent event) {
		if (! isAppenderToList()) return true;
		
		Map<String , LinkedList<LoggingEvent>> logs = ThreadContextData.get(APPENDER_LIST_KEY);
		if (logs == null) {
			logs = new HashMap<>(4);
			ThreadContextData.put(APPENDER_LIST_KEY , logs);
		}
		LinkedList<LoggingEvent> list = logs.get(name);
		if (list == null) {
			list = new LinkedList<>();
			logs.put(name, list);
		}
		list.add(event);
		return  event.getLevel().toInt() >= Level.ERROR_INT;
	}
	
	/***
	 * 打印所有线程里的日志. 使用原有的级别.
	 */
	public static void printAllLoggerNormal(){
		ThreadContextData.removeKey(APPENDER_SWITCH_TO_LIST);
		
		Map<String , LinkedList<LoggingEvent>> logs = ThreadContextData.get(APPENDER_LIST_KEY);
		if (logs != null) {
			Logger logger = Logger.getRootLogger();
			for (Map.Entry<String , LinkedList<LoggingEvent>> en : logs.entrySet()){
				Appender appender = logger.getAppender(en.getKey());
				
				for (LoggingEvent event : en.getValue()) appender.doAppend(event);
			}
		}
	}
}
