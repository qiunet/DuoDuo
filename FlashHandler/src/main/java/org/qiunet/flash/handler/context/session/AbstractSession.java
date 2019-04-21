package org.qiunet.flash.handler.context.session;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import org.qiunet.flash.handler.acceptor.ProcessAcceptor;
import org.qiunet.flash.handler.context.response.push.IMessage;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.utils.timer.DelayTask;
import org.qiunet.utils.timer.TimerManager;

import java.util.concurrent.TimeUnit;

/**
 * Created by qiunet.
 * 17/11/26
 */
public abstract class AbstractSession implements ISession {
	protected Channel channel;
	protected int queueIndex;
	protected long uid;

	public AbstractSession(long uid, Channel channel) {
		this.uid = uid;
		this.channel = channel;
		this.resetQueueIndex();
	}
	@Override
	public int getQueueIndex() {
		return queueIndex;
	}

	@Override
	public void resetQueueIndex() {
		this.queueIndex = channel.hashCode();
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
	public long getUid() {
		return this.uid;
	}

	@Override
	public ChannelFuture writeMessage(IMessage message) {
		return channel.writeAndFlush(message.encode());
	}

	@Override
	public void addProcessMessage(IProcessMessage msg) {
		ProcessAcceptor.getInstance().process(this.getQueueIndex(), msg);
	}

	@Override
	public void addProcessMessage(IProcessMessage msg, long delay, TimeUnit unit) {
		TimerManager.getInstance().scheduleWithDeley(() -> {
			addProcessMessage(msg);
			return null;
			}, delay, unit);
	}

	@Override
	public void addProcessMessage(IProcessMessage msg, long timeMillis) {
		TimerManager.getInstance().scheduleWithTimeMillis(() -> {
			addProcessMessage(msg);
			return null;
		}, timeMillis);
	}
}
