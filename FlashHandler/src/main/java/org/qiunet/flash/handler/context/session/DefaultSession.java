package org.qiunet.flash.handler.context.session;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.qiunet.flash.handler.common.message.MessageContent;

/**
 * Created by qiunet.
 * 17/11/26
 */
public class DefaultSession implements ISession {
	protected Channel channel;
	protected int queueIndex;
	protected String key;
	protected long dt;
	public  DefaultSession(Channel channel) {
		this.channel = channel;
		this.dt = System.currentTimeMillis();
		this.key = channel.id().asLongText();
		this.resetQueueIndex();
	}
	@Override
	public int getQueueIndex() {
		return queueIndex;
	}

	@Override
	public void resetQueueIndex() {
		this.queueIndex = this.key.hashCode();
	}

	@Override
	public void setQueueIndex(int queueIndex) {
		this.queueIndex = queueIndex;
	}

	@Override
	public Channel getChannel() {
		return channel;
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public long lastPackageTimeStamp() {
		return dt;
	}

	@Override
	public void setLastPackageTimeStamp() {
		this.dt = System.currentTimeMillis();
	}
}
