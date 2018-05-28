package org.qiunet.flash.handler.handler.tcp;

import org.qiunet.flash.handler.common.annotation.RequestHandler;
import org.qiunet.flash.handler.context.request.tcp.ITcpRequest;

/**
 * Created by qiunet.
 * 17/11/21
 */
@RequestHandler(ID = 1003, desc = "测试")
public class LoginHandler extends TcpStringHandler{
	@Override
	public void handler(ITcpRequest<String> context)throws Exception {
		context.response(2000, context.getRequestData());
	}
}
