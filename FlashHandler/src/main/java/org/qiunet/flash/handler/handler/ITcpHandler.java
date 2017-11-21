package org.qiunet.flash.handler.handler;

import org.qiunet.flash.handler.context.request.tcp.ITcpRequest;

/**
 * @author qiunet
 *         Created on 17/3/3 12:01.
 */
public interface ITcpHandler<RequestData> extends IHandler<RequestData> {
	/**
	 * tcp udp 处理. 下行一般在逻辑里面处理了
	 * @param context
	 * @return
	 */
	void handler(ITcpRequest<RequestData> context);
}
