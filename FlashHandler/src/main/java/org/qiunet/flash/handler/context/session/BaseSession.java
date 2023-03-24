package org.qiunet.flash.handler.context.session;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPromise;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.qiunet.flash.handler.common.MessageHandler;
import org.qiunet.flash.handler.common.player.IMessageActor;
import org.qiunet.flash.handler.context.response.push.BaseByteBufMessage;
import org.qiunet.flash.handler.context.response.push.IChannelMessage;
import org.qiunet.flash.handler.context.session.config.DSessionConfig;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.utils.exceptions.CustomException;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.nio.channels.ClosedChannelException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/***
 *
 * @author qiunet
 * 2022/4/26 15:13
 */
abstract class BaseSession implements ISession {

	protected static final Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();
	/**
	 * 配置
	 */
	protected DSessionConfig sessionConfig = DSessionConfig.DEFAULT_CONFIG;
	/**
	 * 设置 session 的参数
	 */
	public ISession sessionConfig(DSessionConfig config) {
		Preconditions.checkState(config.isDefault_flush() || (config.getFlush_delay_ms() >= 5 && config.getFlush_delay_ms() < 3000));
		this.sessionConfig = config;
		return this;
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

	private static final GenericFutureListener<? extends Future<? super Void>> listener = f -> {
		// ClosedChannelException 不打印了
		if (! f.isSuccess() && ! (f.cause() instanceof ClosedChannelException)) {
			logger.error("channel send message error:", f.cause());
		}
	};

	/**
	 * 发送消息在这里
	 *
	 * @param message
	 * @param flush
	 * @return
	 */
	private ChannelFuture realSendMessage(Channel channel, IChannelMessage<?> message, boolean flush) {
		ChannelPromise promise = channel.newPromise();
		channel.eventLoop().execute(() -> {
			this.realSendMessage0(promise, channel, message, flush);
		});
		return promise;
	}
	private ChannelFuture realSendMessage0(ChannelPromise promise, Channel channel, IChannelMessage<?> message, boolean flush) {
		IMessageActor messageActor = getAttachObj(ServerConstants.MESSAGE_ACTOR_KEY);
		if (! channel.isOpen()) {
			if (logger.isDebugEnabled() && message.debugOut()) {
				String identityDesc = messageActor == null ? channel.id().asShortText() : messageActor.getIdentity();
				logger.debug("[{}] discard [{}({})] message: {}", identityDesc, getAttachObj(ServerConstants.HANDLER_TYPE_KEY), channel.id().asShortText(), message._toString());
			}

			if (message instanceof BaseByteBufMessage && ((BaseByteBufMessage<?>) message).isByteBufPrepare()) {
					((BaseByteBufMessage<?>) message).getByteBuf().release();
			}
			message.recycle();
			return channel.newPromise();
		}

		if ( logger.isInfoEnabled() && messageActor != null && message.debugOut()) {
			logger.info("[{}] [{}({})] >>> {}", messageActor.getIdentity(), getAttachObj(ServerConstants.HANDLER_TYPE_KEY), channel.id().asShortText(), message._toString());
		}

		if (flush) {
			channel.writeAndFlush(message, promise);
		}else {
			channel.write(message, promise);
		}
		promise.addListener(listener);
		return promise;
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
}
