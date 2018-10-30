package org.qiunet.flash.handler.handler.udp;

import org.qiunet.flash.handler.common.annotation.RequestHandler;
import org.qiunet.flash.handler.context.request.tcp.ITcpRequest;
import org.qiunet.flash.handler.context.request.udp.IUdpRequest;
import org.qiunet.flash.handler.handler.tcp.TcpStringHandler;

/**
 * Created by qiunet.
 * 17/11/21
 */
@RequestHandler(ID = 1008, desc = "测试")
public class LoginHandler extends UdpStringHandler {
	@Override
	public void handler(IUdpRequest<String> context) throws Exception {
		context.udpResponse(2000, context.getRequestData());
	}
}
