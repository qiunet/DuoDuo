package org.qiunet.flash.handler.interceptor;

import org.qiunet.flash.handler.context.request.tcp.ITcpRequest;
import org.qiunet.flash.handler.handler.tcp.ITcpHandler;
import org.qiunet.flash.handler.netty.server.interceptor.TcpInterceptor;

/**
 * Created by qiunet.
 * 17/11/24
 */
public class DefaultTcpStringInterceptor implements TcpInterceptor {
	@Override
	public void preHandler(ITcpHandler handler, ITcpRequest request) {

	}
}
