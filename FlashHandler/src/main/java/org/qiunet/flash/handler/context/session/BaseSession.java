package org.qiunet.flash.handler.context.session;

import com.google.common.collect.Lists;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import org.qiunet.flash.handler.common.player.IMessageActor;
import org.qiunet.flash.handler.context.response.push.IChannelMessage;
import org.qiunet.flash.handler.context.sender.IChannelMessageSender;
import org.qiunet.flash.handler.context.session.future.DChannelFutureWrapper;
import org.qiunet.flash.handler.context.session.future.DMessageContentFuture;
import org.qiunet.flash.handler.context.session.future.IDSessionFuture;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.flash.handler.util.ChannelUtil;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicBoolean;

/***
 *
 * @author qiunet
 * 2022/4/26 15:13
 */
abstract class BaseSession implements ISession {

	protected static final Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();

	protected Channel channel;

	protected void setChannel(Channel channel) {
		if (channel != null) {
			// 测试可能为null
			channel.closeFuture().addListener(f -> this.close(CloseCause.CHANNEL_CLOSE));
		}
		this.channel = channel;
	}

	/**
	 * session是否是活跃的.
	 * @return
	 */
	@Override
	public boolean isActive() {
		return channel != null && channel.isActive();
	}


	@Override
	public Channel channel() {
		return channel;
	}

	@Override
	public String getIp() {
		return ChannelUtil.getIp(channel);
	}
	@Override
	public <T> T getAttachObj(AttributeKey<T> key) {
		return channel.attr(key).get();
	}

	@Override
	public <T> void attachObj(AttributeKey<T> key, T obj) {
		channel.attr(key).set(obj);
	}

	private final AtomicBoolean closed = new AtomicBoolean();
	public void close(CloseCause cause) {
		if (! closed.compareAndSet(false, true)) {
			// 避免多次调用close. 多次调用监听.
			return;
		}

		closeListeners.forEach(l -> l.close(this, cause));

		logger.info("Session [{}] close by cause [{}]", this, cause.getDesc());
		if (channel != null && (channel.isActive() || channel.isOpen())) {
			logger.info("Session [{}] closed", this);
			this.flush();
			channel.close();
		}
	}

	protected abstract void flush();

	@Override
	public IDSessionFuture sendMessage(IChannelMessage<?> message) {
		return this.sendMessage(message, true);
	}

	@Override
	public IDSessionFuture sendMessage(IChannelMessage<?> message, boolean flush) {
		return this.realSendMessage(message, flush);
	}

	/**
	 * 发送消息在这里
	 * @param message
	 * @param flush
	 * @return
	 */
	protected IDSessionFuture realSendMessage(IChannelMessage<?> message, boolean flush) {
		IMessageActor messageActor = getAttachObj(ServerConstants.MESSAGE_ACTOR_KEY);
		if (! this.channel.isOpen()) {
			String identityDesc = messageActor == null ? channel.id().asShortText() : messageActor.getIdentity();
			logger.error("[{}] discard {} message: {}", identityDesc, channel.attr(ServerConstants.HANDLER_TYPE_KEY).get(), message.toStr());
			return new DMessageContentFuture(channel, message);
		}

		if ( logger.isInfoEnabled() && message.needLogger()  && messageActor != null) {
			logger.info("[{}] {} >>> {}", messageActor.getIdentity(), channel.attr(ServerConstants.HANDLER_TYPE_KEY).get(), message.toStr());
		}

		if (flush) {
			return new DChannelFutureWrapper(this.channel.writeAndFlush(message));
		}else {
			return new DChannelFutureWrapper(this.channel.write(message));
		}
	}

	@Override
	public String toString() {
		StringJoiner sj = new StringJoiner(",", "[", "]");
		if (channel != null) {
			sj.add("Type = "+channel.attr(ServerConstants.HANDLER_TYPE_KEY).get());
			IMessageActor messageActor = getAttachObj(ServerConstants.MESSAGE_ACTOR_KEY);
			if (messageActor != null) {
				sj.add(messageActor.getIdentity());
			}
			sj.add("ID = " + channel.id().asShortText());
			sj.add("Ip = " + getIp());
		}
		return sj.toString();
	}

	@Override
	public void addCloseListener(SessionCloseListener listener) {
		this.closeListeners.add(listener);
	}

	@Override
	public void clearCloseListener(){
		this.closeListeners.clear();
	}

	private final List<SessionCloseListener> closeListeners = Lists.newCopyOnWriteArrayList();

	@Override
	public IChannelMessageSender getSender() {
		return this;
	}


	@FunctionalInterface
	public interface SessionCloseListener {
		void close(ISession session, CloseCause cause);
	}
}
