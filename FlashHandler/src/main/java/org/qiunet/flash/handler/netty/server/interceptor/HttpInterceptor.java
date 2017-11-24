package org.qiunet.flash.handler.netty.server.interceptor;

import org.qiunet.flash.handler.context.request.http.IHttpRequest;
import org.qiunet.flash.handler.handler.http.IHttpHandler;

/**
 * Created by qiunet.
 * 17/11/23
 */
public interface HttpInterceptor {
	/***
	 * 自行处理
	 * @param handler
	 * @param request
	 */
	Object handler(IHttpHandler handler, IHttpRequest request);

}
