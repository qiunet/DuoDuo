package org.qiunet.flash.handler.handler.http.async;

import org.qiunet.flash.handler.context.request.http.IHttpRequest;
import org.qiunet.flash.handler.handler.http.IHttpHandler;

/**
 * http的异步处理
 * 一般业务自己实现在basic 基类里面
 *
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
	HttpAsyncTask<ResponseData> handler(IHttpRequest<RequestData> request)throws Exception;
}
