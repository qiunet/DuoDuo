package org.qiunet.flash.handler.context.response;

/**
 * @author qiunet
 *         Created on 17/3/14 20:10.
 */
public interface IResponse<ResponseData> {
	/***
	 * 对客户端响应
	 * @param responseData
	 */
	void response(int protocolId, ResponseData responseData);
}
