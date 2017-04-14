package org.qiunet.utils.exceptions;

public class HttpMethodNotSupportException extends RuntimeException {
	/**
	 *
	 */
	private static final long serialVersionUID = -6650239602491790873L;

	public HttpMethodNotSupportException(){
		super();
	}

	public HttpMethodNotSupportException(String desc){
		super(desc);
	}
}
