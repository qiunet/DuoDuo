package org.qiunet.flash.handler.handler.http;

import org.qiunet.flash.handler.context.request.http.IHttpRequest;

/**
 * http的处理
 * @author qiunet
 *         Created on 17/3/3 12:01.
 */
public interface ISyncHttpHandler<RequestData, ResponseData> extends IHttpHandler<RequestData> {
	@Override
	default boolean isAsync() {
		return false;
	}
	/**
	 * http返回处理后的Response
	 * @param request
	 * @return
	 */
	ResponseData handler(IHttpRequest<RequestData> request)throws Exception;
}
