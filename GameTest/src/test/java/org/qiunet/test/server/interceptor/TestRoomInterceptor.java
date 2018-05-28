package org.qiunet.test.server.interceptor;

import org.qiunet.flash.handler.context.request.tcp.ITcpRequest;
import org.qiunet.flash.handler.handler.tcp.ITcpHandler;
import org.qiunet.flash.handler.netty.server.interceptor.TcpInterceptor;

/**
 * Created by qiunet.
 * 17/12/11
 */
public class TestRoomInterceptor implements TcpInterceptor {
	@Override
	public void handler(ITcpHandler handler, ITcpRequest request) {

		try {
			handler.handler(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
