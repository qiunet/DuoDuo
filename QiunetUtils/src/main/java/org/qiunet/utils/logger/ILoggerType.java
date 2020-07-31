package org.qiunet.utils.logger;

import org.slf4j.Logger;

/***
 *
 * @author qiunet
 * 2020-04-25 15:53
 **/
public interface ILoggerType {

	Logger getLogger();

	default void debug(String msg) {
		getLogger().debug(msg);
	}

	default void debug(String format, Object ... arguments) {
		getLogger().debug(format, arguments);
	}

	default void info(String msg) {
		getLogger().info(msg);
	}

	default void info(String format, Object ... arguments) {
		getLogger().info(format, arguments);
	}

	default void warn(String msg) {
		getLogger().warn(msg);
	}

	default void warn(String format, Object ... arguments) {
		getLogger().warn(format, arguments);
	}

	default void error(String msg) {
		getLogger().error(msg);
	}

	default void error(String format, Object ... arguments) {
		getLogger().error(format, arguments);
	}

	default void error(String msg, Throwable e) {
		getLogger().error(msg, e);
	}
}
