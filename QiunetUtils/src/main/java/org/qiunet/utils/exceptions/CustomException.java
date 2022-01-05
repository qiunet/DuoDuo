package org.qiunet.utils.exceptions;

import org.slf4j.Logger;
import org.slf4j.helpers.MessageFormatter;

/***
 * 自定义的一个运行异常. 主要拼消息会方便很多.
 *
 * @author qiunet
 * 2020-09-27 17:19
 */
public class CustomException extends RuntimeException {

	public CustomException(String message, Object... params) {
		this(null, message, params);
	}

	public CustomException(Throwable cause, String message, Object... params) {
		super(MessageFormatter.arrayFormat(message, params).getMessage(), cause);
	}

	public static CustomException wrapper(Throwable cause) {
		if (cause.getClass() == CustomException.class) {
			return ((CustomException) cause);
		}
		return new CustomException(cause, cause.getMessage());
	}

	public void logger(Logger logger) {
		if (getCause() != null) {
			logger.error(getMessage(), getCause());
		}else {
			logger.error(getMessage());
		}
	}
}
