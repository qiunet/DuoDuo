package org.qiunet.handler.context;

/**
 * @author qiunet
 *         Created on 17/3/15 17:49.
 */
public interface ITcpUdpContext extends IContext {
	/**
	 * 得到分配到哪个queueHandler 的一个索引,  要求是一次连接 到 断开过程中不在变化.
	 * @return
	 */
	public int getQueueHandlerIndex();
}
