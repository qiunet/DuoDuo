package org.qiunet.flash.handler.interceptor;

import org.qiunet.flash.handler.context.request.tcp.ITcpRequest;
import org.qiunet.flash.handler.handler.tcp.ITcpHandler;
import org.qiunet.flash.handler.netty.server.interceptor.TcpInterceptor;

/**
 * Created by qiunet.
 * 17/11/24
 */
public class DefaultTcpInterceptor implements TcpInterceptor {
	@Override
	public void handler(ITcpHandler handler, ITcpRequest request) {
		System.out.println("Protocol Id ["+handler.getProtocolID()+"] received message: "+request.getRequestData());
	}
}
