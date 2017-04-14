package org.qiunet.handler.handler.intecepter;

import org.qiunet.handler.context.IContext;
import org.qiunet.handler.handler.IHandler;
import org.qiunet.handler.handler.IHttpHandler;
import org.qiunet.handler.handler.ITcpUdpHandler;
import org.qiunet.handler.iodata.net.AbstractRequestData;

/**
 * intercepter 可以继承这个类省掉部分方法. 否则自己实现也行
 * @author qiunet
 *         Created on 17/3/7 11:33.
 */
public abstract class AbstractIntercepter implements Intercepter {

	

	/**
	 * http handler
	 */
	protected abstract void httpHandler(IContext context);

	/**
	 * tcp udp handler
	 */
	protected abstract void tcpUdpHandler(IContext context);

	@Override
	public void preHandler(IContext context) {
		
	}

	@Override
	public void handler(IContext context) {
		IHandler handler = context.getHandler();
		switch (handler.getHandlerType()){
			case HTTP:
				this.httpHandler(context);
				break;
			case TCP_UDP:
				this.tcpUdpHandler(context);
				break;
		}
	}

	@Override
	public void postHandler(IContext context) {

	}
}
