package org.qiunet.flash.handler.context.session;

import io.netty.channel.ChannelHandlerContext;

/**
 * Created by qiunet.
 * 17/11/27
 */
public class DefaultPlayerSession extends DefaultSession implements IPlayerSession {
	private int uid;

	public DefaultPlayerSession(ChannelHandlerContext ctx) {
		this(ctx, 0);
	}

	public DefaultPlayerSession(ChannelHandlerContext ctx, int uid) {
		super(ctx);
		this.uid = uid;
	}

	@Override
	public int getUid() {
		return uid;
	}

	@Override
	public void setUid(int uid) {
		this.uid = uid;
	}
}
