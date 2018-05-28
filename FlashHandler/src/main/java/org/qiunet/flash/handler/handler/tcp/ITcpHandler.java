package org.qiunet.flash.handler.handler.tcp;

import org.qiunet.flash.handler.context.request.tcp.ITcpRequest;
import org.qiunet.flash.handler.handler.IHandler;

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
	void handler(ITcpRequest<RequestData> context)throws Exception;
}
