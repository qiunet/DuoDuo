package org.qiunet.flash.handler.context.request.tcp;

import org.qiunet.flash.handler.context.request.IRequestContext;

/**
 * Created by qiunet.
 * 17/7/21
 */
public interface ITcpRequestContext<RequestData> extends IRequestContext<RequestData>, ITcpRequest<RequestData> {
	/**
	 * 得到分配到哪个queueHandler 的一个索引,  要求是一次连接 到 断开过程中不在变化.
	 * @return
	 */
	public int getQueueIndex();
}
