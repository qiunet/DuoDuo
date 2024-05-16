package org.qiunet.flash.handler.context.session;

import com.google.common.collect.Maps;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPromise;
import io.netty.channel.DefaultChannelPromise;
import io.netty.util.concurrent.Future;
import org.qiunet.flash.handler.common.MessageHandler;
import org.qiunet.flash.handler.common.player.IMessageActor;
import org.qiunet.flash.handler.context.response.push.BaseByteBufMessage;
import org.qiunet.flash.handler.context.response.push.IChannelMessage;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/***
 *
 * @author qiunet
 * 2022/4/26 15:13
 */
abstract class BaseSession implements ISession {
	protected final Map<String, SessionCloseListener> closeListeners = Maps.newConcurrentMap();

	protected static final Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();

	protected final AtomicBoolean closed = new AtomicBoolean();
	public void close(CloseCause cause) {
		if (! closed.compareAndSet(false, true)) {
			// 避免多次调用close. 多次调用监听.
			return;
		}
		logger.info("Session [{}] close by cause [{}]", this, cause.getDesc());
		IMessageActor attachObj = getAttachObj(ServerConstants.MESSAGE_ACTOR_KEY);
		if (attachObj == null) {
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

	@Override
	public boolean isClose() {
		return closed.get();
	}

	/**
	 * 关闭session
	 * @param cause
	 */
	private void closeSession(CloseCause cause) {
		try {
			IMessageActor attachObj = getAttachObj(ServerConstants.MESSAGE_ACTOR_KEY);
			closeListeners.forEach((name, cl) -> {
				try {
					cl.close(this, cause);
				}catch (Exception e) {
					logger.error("close session exception: ", e);
				}
			});

			// 没有loginSuccess的那种
			if (closeListeners.isEmpty()) {
				attachObj.destroy();
			}
		}finally {
			this.closeChannel(cause);
		}
	}

	/**
	 * 关闭channel
	 * @param cause 原因
	 */
	protected abstract void closeChannel(CloseCause cause);
	/**
	 * 发送message
	 * @param message
	 * @param flush
	 * @return
	 */
	protected ChannelFuture doSendMessage(Channel channel, IChannelMessage<?> message, boolean flush) {
		return this.realSendMessage(channel, message, flush);
	}

	/**
	 * 发送消息在这里
	 *
	 * @param message
	 * @param flush
	 * @return
	 */
	private ChannelPromise realSendMessage(Channel channel, IChannelMessage<?> message, boolean flush) {
		ChannelPromise promise = new DChannelPromise(this, channel, message);
		channel.eventLoop().execute(() -> this.realSendMessage0(promise, channel, message, flush));
		return promise;
	}
	private void realSendMessage0(ChannelPromise promise, Channel channel, IChannelMessage<?> message, boolean flush) {
		IMessageActor messageActor = getAttachObj(ServerConstants.MESSAGE_ACTOR_KEY);
		if (! channel.isOpen()) {
			if (logger.isDebugEnabled() && message.debugOut()) {
				String identityDesc = messageActor == null ? this.aliasId() : messageActor.getIdentity();
				logger.debug("[{}] discard [{}({})] message: {}", identityDesc, getAttachObj(ServerConstants.HANDLER_TYPE_KEY), this.aliasId(), message._toString());
			}

			if (message instanceof BaseByteBufMessage && ((BaseByteBufMessage<?>) message).isByteBufPrepare()) {
					((BaseByteBufMessage<?>) message).getByteBuf().release();
			}
			message.recycle();
			return;
		}

		if ( LoggerType.DUODUO_PROTOCOL.isInfoEnabled() &&  message.debugOut()) {
			String identityDesc = messageActor == null ? this.aliasId() : messageActor.getIdentity();
			LoggerType.DUODUO_PROTOCOL.info("[{}] [{}({})] >>> {}", identityDesc, getAttachObj(ServerConstants.HANDLER_TYPE_KEY), this.aliasId(), message._toString());
		}

		if (flush) {
			channel.writeAndFlush(message.needFlush(), promise);
		}else {
			channel.write(message, promise);
		}
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
	private static class DChannelPromise extends DefaultChannelPromise {

		private final BaseSession session;

		private final int protocolId;

		public DChannelPromise(BaseSession session, Channel channel, IChannelMessage<?> message) {
			super(channel);
			this.protocolId = message.getProtocolID();
			this.addListener(this::listener);
			this.session = session;
		}

		private void listener(Future<? super Void> f) {
			// 成功发送消息时，直接返回不做处理
			if (f.isSuccess()) {
				return;
			}

			// 当失败原因为ClosedChannelException时，也不进行处理
			// 可以等等再放开注释
			//if (f.cause() instanceof ClosedChannelException) {
			//	return;
			//}

			// 记录除ClosedChannelException外的其他发送错误
			logger.error("channel send message protocolID [{}] error:", this.protocolId, f.cause());
		}
	}
}
