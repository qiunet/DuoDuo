package org.qiunet.flash.handler.context.request.http;

import org.qiunet.flash.handler.context.request.IRequestContext;
import org.qiunet.flash.handler.context.response.IHttpResponse;
import org.qiunet.flash.handler.handler.http.IHttpHandler;


/**
 * Created by qiunet.
 * 17/11/21
 */
public interface IHttpRequestContext<RequestData, ResponseData> extends IRequestContext<RequestData>, IHttpRequest<RequestData>, IHttpResponse<ResponseData> {

	@Override
	IHttpHandler<RequestData> getHandler();
}
