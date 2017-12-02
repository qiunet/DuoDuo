package org.qiunet.flash.handler.context.request.websocket;

import org.qiunet.flash.handler.context.request.IRequestContext;
import org.qiunet.flash.handler.context.request.tcp.ITcpRequestContext;

/**
 * Created by qiunet.
 * 17/12/2
 */
public interface IWebSocketRequestContext<RequestData> extends IRequestContext<RequestData> {
	/**
	 * 得到分配到哪个queueHandler 的一个索引,  要求是一次连接 到 断开过程中不在变化.
	 * @return
	 */
	public int getQueueHandlerIndex();
}
