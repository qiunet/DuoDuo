package org.qiunet.handler.exception;

/**
 * @author qiunet
 *         Created on 17/3/10 17:19.
 */
public class CrcErrorException extends RuntimeException {
	
	public CrcErrorException (String msg){
		super(msg);
	}
}
