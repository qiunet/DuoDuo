package org.qiunet.flash.handler.context.session;

import com.google.common.collect.Maps;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.util.AttributeKey;
import org.qiunet.flash.handler.common.MessageHandler;
import org.qiunet.flash.handler.common.player.IMessageActor;
import org.qiunet.flash.handler.common.player.IRobot;
import org.qiunet.flash.handler.context.response.push.DefaultByteBufMessage;
import org.qiunet.flash.handler.context.response.push.IChannelMessage;
import org.qiunet.flash.handler.context.sender.IChannelMessageSender;
import org.qiunet.flash.handler.context.session.future.DMessageContentFuture;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.flash.handler.util.ChannelUtil;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.util.Map;
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

		IMessageActor attachObj = getAttachObj(ServerConstants.MESSAGE_ACTOR_KEY);
		if (attachObj == null || attachObj.msgExecuteIndex() == null) {
			if (attachObj != null) {
				attachObj.destroy();
			}
			this.closeChannel(cause);
			return;
		}

		if (cause == CloseCause.SERVER_SHUTDOWN
		|| ((MessageHandler) attachObj).isDestroyed()
		|| ((MessageHandler<?>) attachObj).inSelfThread()) {
			// 直接执行.
			this.closeSession(cause);
		}else {
			attachObj.addMessage(p -> {
				this.closeSession(cause);
			});
		}
	}

	/**
	 * 关闭session
	 * @param cause
	 */
	private void closeSession(CloseCause cause) {
		IMessageActor attachObj = getAttachObj(ServerConstants.MESSAGE_ACTOR_KEY);
		closeListeners.forEach((name, cl) -> {
			cl.close(this, cause);
		});

		// 没有loginSuccess的那种
		if (closeListeners.isEmpty()) {
			attachObj.destroy();
		}
		this.closeChannel(cause);
	}
	/**
	 * 关闭channel
	 * @param cause
	 */
	private void closeChannel(CloseCause cause) {
		if (channel == null) {
			return;
		}
		logger.info("Session [{}] close by cause [{}]", this, cause.getDesc());
		if ((channel.isActive() || channel.isOpen())) {
			logger.info("Session [{}] closed", this);
			this.flush();
		}
		channel.close();
	}

	@Override
	public ChannelFuture sendMessage(IChannelMessage<?> message) {
		return this.sendMessage(message, true);
	}

	@Override
	public ChannelFuture sendMessage(IChannelMessage<?> message, boolean flush) {
		return this.realSendMessage(message, flush);
	}

	/**
	 * 发送消息在这里
	 * @param message
	 * @param flush
	 * @return
	 */
	protected ChannelFuture realSendMessage(IChannelMessage<?> message, boolean flush) {
		IMessageActor messageActor = getAttachObj(ServerConstants.MESSAGE_ACTOR_KEY);
		if (! this.channel.isOpen()) {
			String identityDesc = messageActor == null ? channel.id().asShortText() : messageActor.getIdentity();
			logger.error("[{}] discard [{}({})] message: {}", identityDesc, channel.attr(ServerConstants.HANDLER_TYPE_KEY).get(), channel.id().asShortText(), message.toStr());
			if (message instanceof DefaultByteBufMessage) {
				((DefaultByteBufMessage) message).getContent().release();
				message.recycle();
			}
			return new DMessageContentFuture(channel, message);
		}

		if ( logger.isInfoEnabled() && messageActor != null && ( message.needLogger() || messageActor instanceof IRobot)) {
			logger.info("[{}] [{}({})] >>> {}", messageActor.getIdentity(), channel.attr(ServerConstants.HANDLER_TYPE_KEY).get(), channel.id().asShortText(), message.toStr());
		}

		if (flush) {
			return this.channel.writeAndFlush(message);
		}else {
			return this.channel.write(message);
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
	public void addCloseListener(String name, SessionCloseListener listener) {
		if (this.closeListeners.containsKey(name)) {
			throw new CustomException("close listener {} repeated!", name);
		}
		this.closeListeners.put(name, listener);
	}

	@Override
	public void clearCloseListener(){
		this.closeListeners.clear();
	}

	protected final Map<String, SessionCloseListener> closeListeners = Maps.newConcurrentMap();

	@Override
	public IChannelMessageSender getSender() {
		return this;
	}
}
