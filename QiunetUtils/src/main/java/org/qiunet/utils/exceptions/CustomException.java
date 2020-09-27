package org.qiunet.utils.exceptions;

import org.slf4j.Logger;
import org.slf4j.helpers.MessageFormatter;

/***
 *
 *
 * @author qiunet
 * 2020-09-27 17:19
 */
public class CustomException extends RuntimeException {
	private Throwable ex;

	public CustomException(String message, Object... params) {
		this(null, message, params);
	}

	public CustomException(Throwable ex, String message, Object... params) {
		super(MessageFormatter.arrayFormat(message, params).getMessage());
		this.ex = ex;
	}

	public void logger(Logger logger) {
		if (ex != null) {
			logger.error(getMessage(), ex);
		}else {
			logger.error(getMessage());
		}
	}
}
