package org.qiunet.flash.handler.context.session;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import org.qiunet.flash.handler.common.listener.SessionCloseEventData;
import org.qiunet.flash.handler.context.response.push.IResponseMessage;

/**
 * Session 的 父类
 * Created by qiunet.
 * 17/11/26
 */
public abstract class AbstractSession implements ISession {
	protected Channel channel;
	protected long uid;

	public AbstractSession(long uid, Channel channel) {
		this.uid = uid;
		this.channel = channel;
	}

	@Override
	public boolean isAuth() {
		return this.uid > 0;
	}

	@Override
	public boolean isActive() {
		return channel != null && channel.isActive();
	}

	@Override
	public Channel getChannel() {
		return channel;
	}

	@Override
	public long getUid() {
		return this.uid;
	}

	@Override
	public ChannelFuture writeMessage(IResponseMessage message) {
		return channel.writeAndFlush(message.encode());
	}

	@Override
	public void close() {
		new SessionCloseEventData(this).fireEventHandler();
	}
}
