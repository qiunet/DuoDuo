package org.qiunet.utils.exceptions;

import org.slf4j.helpers.MessageFormatter;

/***
 *
 *
 * @author qiunet
 * 2020-09-27 17:19
 */
public class CustomException extends RuntimeException {

	private String message;

	private Object [] params;

	public CustomException(String message, Object... params) {
		super(MessageFormatter.arrayFormat(message, params).getMessage());
		this.message = message;
		this.params = params;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public Object[] getParams() {
		return params;
	}
}
