package org.qiunet.utils.exceptions;

/**
 * 池导致的异常
 * @author qiunet
 *         Created on 16/12/21 16:55.
 */
public class PoolException extends RuntimeException {
	public PoolException(){}

	public PoolException(String str){
		super(str);
	}
}
