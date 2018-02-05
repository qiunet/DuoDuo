package org.qiunet.utils.logger;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Layout;

/**
 * 日志记录 不打印回车 换行
 * @author qiunet
 *         Created on 17/1/6 11:30.
 */
public class GameConsoleAppender extends ConsoleAppender {
	/**
	 * Constructs an unconfigured appender.
	 */
	public GameConsoleAppender() {
		super();
	}

	/**
	 * Creates a configured appender.
	 *
	 * @param layout layout, may not be null.
	 */
	public GameConsoleAppender(Layout layout)
	{
		super(layout);
	}

	/**
	 *   Creates a configured appender.
	 * @param layout layout, may not be null.
	 * @param target target, either "System.err" or "System.out".
	 */
	public GameConsoleAppender(Layout layout, String target) {
		super(layout, target);
	}
}
