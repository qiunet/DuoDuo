package org.qiunet.utils.logger;

import org.slf4j.Logger;

import java.util.function.Supplier;

/***
 *
 * @author qiunet
 * 2020-04-25 15:53
 **/
public interface ILoggerType {

	Logger getLogger();
 	default boolean isDebugEnabled(){
 		return getLogger().isDebugEnabled();
	}

	default boolean isInfoEnabled(){
 		return getLogger().isInfoEnabled();
	}

	default boolean isErrorEnabled(){
		return getLogger().isErrorEnabled();
	}

	/**
	 * 如果 debug能打印. 才真正去计算string.
	 * @param msg
	 */
	default void debug(Supplier<String> msg) {
 		if (this.isDebugEnabled()) {
 			this.debug(msg.get());
		}
	}

	default void info(Supplier<String> msg) {
		if (this.isInfoEnabled()) {
			this.info(msg.get());
		}
	}

	default void error(Supplier<String> msg) {
		if (this.isErrorEnabled()) {
			this.error(msg.get());
		}
	}

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
