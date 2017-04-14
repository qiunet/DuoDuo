package org.qiunet.handler.intercepter;

import org.qiunet.handler.context.IContext;
import org.qiunet.handler.handler.IHandler;
import org.qiunet.handler.handler.IHttpHandler;
import org.qiunet.handler.handler.ITcpUdpHandler;
import org.qiunet.handler.handler.intecepter.AbstractIntercepter;
import org.qiunet.handler.iodata.net.AbstractResponseData;

/**
 * @author qiunet
 *         Created on 17/3/15 18:03.
 */
public class HandlerIntercepter extends AbstractIntercepter {
	@Override
	public void throwCause(IContext context, Exception e) {
		e.printStackTrace();
	}

	@Override
	protected void httpHandler(IContext context) {
		IHttpHandler handler = (IHttpHandler) context.getHandler();
		AbstractResponseData responseData = handler.handler(context.getRequestData());
		context.response(responseData);
	}

	@Override
	protected void tcpUdpHandler(IContext context) {
		ITcpUdpHandler handler = (ITcpUdpHandler) context.getHandler();
		handler.handler(context);
	}
}
