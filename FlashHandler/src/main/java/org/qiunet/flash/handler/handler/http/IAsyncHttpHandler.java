package org.qiunet.flash.handler.handler.http;

import org.qiunet.flash.handler.context.request.http.IAsyncHttpRequest;

/**
 * http的异步处理
 * @author qiunet
 *         Created on 17/3/3 12:01.
 */
public interface IAsyncHttpHandler<RequestData, ResponseData> extends IHttpHandler<RequestData> {

	@Override
	default boolean isAsync() {
		return true;
	}
	/**
	 * http返回处理后的Response
	 * @param request
	 * @return
	 */
	void handler(IAsyncHttpRequest<RequestData, ResponseData> request)throws Exception;
}
