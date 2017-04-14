package org.qiunet.handler.exception;

/**
 * @author qiunet
 *         Created on 17/3/15 17:45.
 */
public class DuplicateInstanceException extends RuntimeException {
	
	public DuplicateInstanceException (String msg) {
		super(msg);
	}
}
