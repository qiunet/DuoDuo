package org.qiunet.flash.handler.context.response;

import io.netty.channel.ChannelHandlerContext;
import org.qiunet.utils.nonSyncQuene.IndexNonSyncQueueHandler;

/**
 * Created by qiunet.
 * 17/11/21
 */
public class TcpResponseMsgQueue {
	/**可以根据index 自动分组的异步队列 */
	private IndexNonSyncQueueHandler<TcpMessage> queueHandler  = new IndexNonSyncQueueHandler<>(true);

	private volatile static TcpResponseMsgQueue instance;

	private TcpResponseMsgQueue() {
		if (instance != null) throw new RuntimeException("Instance Duplication!");
		instance = this;
	}

	public static TcpResponseMsgQueue getInstance() {
		if (instance == null) {
			synchronized (TcpResponseMsgQueue.class) {
				if (instance == null)
				{
					new TcpResponseMsgQueue();
				}
			}
		}
		return instance;
	}

	public void addMessage(ChannelHandlerContext ctx, int protocolId, byte[] bytes) {

	}
}
