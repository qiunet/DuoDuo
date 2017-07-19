package org.qiunet.flash.handler.handler;

import org.qiunet.flash.handler.context.IContext;

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
	public void handler(IContext<RequestData> context);
}
