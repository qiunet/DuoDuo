package org.qiunet.flash.handler.handler.response;

/**
 * @author qiunet
 *         Created on 17/3/14 20:10.
 */
public interface IResponse<ResponseData> {
	/***
	 * 对客户端响应
	 * @param responseData
	 */
	public void response(ResponseData responseData);
}
