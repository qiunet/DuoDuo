package org.qiunet.flash.handler.context.session;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by qiunet.
 * 17/11/27
 */
public class DefaultPlayerSession extends DefaultSession implements IPlayerSession {
	private int uid;

	public DefaultPlayerSession(Channel channel) {
		this(channel, 0);
	}

	public DefaultPlayerSession(Channel channel, int uid) {
		super(channel);
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
