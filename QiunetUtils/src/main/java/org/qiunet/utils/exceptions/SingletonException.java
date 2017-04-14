package org.qiunet.utils.exceptions;

/**
 * @author qiunet
 *         Created on 17/3/3 16:58.
 */
public class SingletonException extends RuntimeException {

	public SingletonException(){
		super();
	}

	public SingletonException(String str){
		super(str);
	}
}
