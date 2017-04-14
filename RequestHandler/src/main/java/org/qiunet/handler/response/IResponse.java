package org.qiunet.handler.response;

import org.qiunet.handler.iodata.net.AbstractResponseData;

/**
 * @author qiunet
 *         Created on 17/3/14 20:10.
 */
public interface IResponse {
	/***
	 * 对客户端响应
	 * @param responseData
	 */
	public void response(AbstractResponseData responseData);
}
