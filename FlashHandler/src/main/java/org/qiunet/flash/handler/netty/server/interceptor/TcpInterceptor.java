package org.qiunet.flash.handler.netty.server.interceptor;

import org.qiunet.flash.handler.context.request.tcp.ITcpRequest;
import org.qiunet.flash.handler.handler.tcp.ITcpHandler;

/**
 * Created by qiunet.
 * 17/11/23
 */
public interface TcpInterceptor {
	/***
	 * 自行处理
 	 * @param handler
	 * @param request
	 */
	void handler(ITcpHandler handler, ITcpRequest request);
}
