package org.qiunet.flash.handler.context.request.http;

import org.qiunet.flash.handler.context.request.IRequestContext;
import org.qiunet.flash.handler.handler.http.IHttpHandler;


/**
 * Created by qiunet.
 * 17/11/21
 */
public interface IHttpRequestContext<RequestData, ResponseData> extends IRequestContext<RequestData>, IHttpRequest<RequestData> {
	
	@Override
	IHttpHandler<RequestData, ResponseData> getHandler();
}
