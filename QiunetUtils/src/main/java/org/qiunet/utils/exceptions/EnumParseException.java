package org.qiunet.utils.exceptions;

import java.io.Serial;

/**
 * enum parse exception
 * @author xiangyang
 *
 * 2013-7-31 下午3:41:51
 *
 */
public class EnumParseException extends RuntimeException {
	@Serial
	private static final long serialVersionUID = -6264105421183868936L;

	public EnumParseException(String val){
		super("no enum for value ["+val+"]");
	}

	public EnumParseException(int val){
		super("no enum for value ["+val+"]");
	}
}
