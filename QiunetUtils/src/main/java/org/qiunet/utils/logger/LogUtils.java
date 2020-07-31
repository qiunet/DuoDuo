package org.qiunet.utils.logger;

import java.util.StringJoiner;

/***
 *
 *
 * @author qiunet
 * 2020-04-24 18:16
 ***/
public class LogUtils {


	public static void printStackTrace(String message) {
		LoggerType.DUODUO.error(generatePrintStackTrace(message));
	}

	public static String generatePrintStackTrace(String message) {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		StringJoiner joiner = new StringJoiner("\r\n");
		joiner.add("["+message+"] StackTrace");
		for (int i = 2; i < stackTrace.length; i++) {
			joiner.add(stackTrace[i].toString());
		}
		return joiner.toString();
	}
}
