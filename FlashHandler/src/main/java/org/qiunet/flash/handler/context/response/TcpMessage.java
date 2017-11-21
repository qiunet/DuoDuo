package org.qiunet.flash.handler.context.response;

import io.netty.channel.ChannelHandlerContext;
import org.qiunet.utils.nonSyncQuene.QueueElement;
import org.qiunet.utils.nonSyncQuene.mutiThread.MultiNonSyncQueueHandler;

/**
 * Created by qiunet.
 * 17/11/21
 */
public class TcpMessage implements QueueElement{
	private ChannelHandlerContext ctx;
	private int protocolId;
	private byte [] bytes;

	public TcpMessage(int protocolId, byte [] bytes, ChannelHandlerContext ctx) {
		this.protocolId = protocolId;
		this.bytes = bytes;
		this.ctx = ctx;
	}
	@Override
	public boolean handler() {
		return false;
	}

	@Override
	public String toStr() {
		return null;
	}
}
