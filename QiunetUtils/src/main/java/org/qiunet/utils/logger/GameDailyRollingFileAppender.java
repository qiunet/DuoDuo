package org.qiunet.utils.logger;

import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Layout;

import java.io.*;

/**
 * @author qiunet
 *         Created on 17/1/9 08:30.
 */
public class GameDailyRollingFileAppender extends DailyRollingFileAppender {
	/**
	 The default constructor does nothing. */
	public GameDailyRollingFileAppender() {
		super();
	}

	/**
	 Instantiate a <code>DailyRollingFileAppender</code> and open the
	 file designated by <code>filename</code>. The opened filename will
	 become the ouput destination for this appender.

	 */
	public GameDailyRollingFileAppender (Layout layout, String filename, String datePattern) throws IOException {
		super(layout, filename, datePattern);
	}
}
