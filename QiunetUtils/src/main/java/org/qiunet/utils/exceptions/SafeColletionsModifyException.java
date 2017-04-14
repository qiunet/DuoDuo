package org.qiunet.utils.exceptions;

/**
 * @author qiunet
 *         Created on 17/3/1 16:30.
 */
public class SafeColletionsModifyException extends RuntimeException {

	public SafeColletionsModifyException(){
		super();
	}

	public SafeColletionsModifyException(String str){
		super(str);
	}
}
