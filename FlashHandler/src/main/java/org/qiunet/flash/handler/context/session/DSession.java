package org.qiunet.flash.handler.context.session;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.util.AttributeKey;
import org.qiunet.flash.handler.common.annotation.SkipDebugOut;
import org.qiunet.flash.handler.common.player.IMessageActor;
import org.qiunet.flash.handler.context.request.data.pb.IpbChannelData;
import org.qiunet.flash.handler.context.response.push.IChannelMessage;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.flash.handler.util.ChannelUtil;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Session 的 父类
 * Created by qiunet.
 * 17/11/26
 */
public final class DSession {
	private Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();
	/**
	 * 默认flush延时毫秒时间
	 */
	private int flush_delay_ms = 50;
	/**
	 * 是否默认flush
	 */
	private boolean default_flush;

	protected Channel channel;
	/**
	 * 判断是否已经在计时flush
	 */
	private AtomicBoolean flushScheduling = new AtomicBoolean();

	public DSession(Channel channel) {
		this(50, false, channel);
	}

	public DSession(int flush_delay_ms, boolean default_flush, Channel channel) {
		Preconditions.checkState(flush_delay_ms >= 5 && flush_delay_ms < 1000);
		this.flush_delay_ms = flush_delay_ms;
		this.default_flush = default_flush;
		this.channel = channel;

		channel.closeFuture().addListener(f -> this.close(CloseCause.CHANNEL_CLOSE));
	}

	/**
	 * session是否是活跃的.
	 * @return
	 */
	public boolean isActive() {
		return channel.isActive();
	}

	/**
	 * 得到channel
	 * @return
	 */
	public Channel channel() {
		return channel;
	}

	/**
	 * 获得ip
	 * @return
	 */
	public String getIp() {
		return ChannelUtil.getIp(channel);
	}

	/**
	 * 写入消息
	 * @param message
	 * @return
	 */
	public ChannelFuture writeMessage(IpbChannelData message) {
		return this.writeMessage(message.buildResponseMessage());
	}
	/**
	 * 写入消息
	 * @param message
	 * @return
	 */

	public ChannelFuture writeMessage(IChannelMessage message) {
		return this.writeMessage(message, default_flush);
	}

	/**
	 * 写入消息. 自己定义是否需要立即刷新
	 * @param message
	 * @param flush
	 * @return
	 */
	public ChannelFuture writeMessage(IChannelMessage message, boolean flush) {
		if ( logger.isInfoEnabled()
			&& ! message.getContent().getClass().isAnnotationPresent(SkipDebugOut.class)) {
			IMessageActor messageActor = getAttachObj(ServerConstants.MESSAGE_ACTOR_KEY);
			if (messageActor != null) {
				logger.info("[{}] >>> {}", messageActor.getIdent(), message.toStr());
			}
		}
		if (flush) {
			return channel.writeAndFlush(message.encode());
		}
		ChannelFuture future = channel.write(message.encode());
		if (flushScheduling.compareAndSet(false, true)) {
			channel.eventLoop().schedule(() -> {
				flushScheduling.set(false);
				channel.flush();
			}, flush_delay_ms, TimeUnit.MILLISECONDS);
		}
		return future;
	}
	/**
	 * 获得channel里面的对象.
	 * @param key
	 * @param <T>
	 * @return
	 */
	public <T> T getAttachObj(AttributeKey<T> key) {
		return channel.attr(key).get();
	}

	/**
	 * 往channel上面挂载数据.
	 * @param key
	 * @param obj
	 * @param <T>
	 */
	public <T> void attachObj(AttributeKey<T> key, T obj) {
		channel.attr(key).set(obj);
	}

	private AtomicBoolean closed = new AtomicBoolean();
	public void close(CloseCause cause) {
		if (! closed.compareAndSet(false, true)) {
			// 避免多次调用close. 多次调用监听.
			return;
		}
		logger.info("Session [{}] closed by cause [{}]", this, cause.getDesc());
		closeListeners.forEach(l -> l.close(cause));
		if (channel.isActive() || channel.isOpen()) {
			channel.close();
		}
	}

	@Override
	public String toString() {
		StringJoiner sj = new StringJoiner(",", "[", "]");
		IMessageActor messageActor = getAttachObj(ServerConstants.MESSAGE_ACTOR_KEY);
		if (messageActor != null) {
			sj.add(messageActor.getIdent());
		}
		sj.add("ID = " + channel.id().asShortText());
		sj.add("Ip = " + getIp());
		return sj.toString();
	}

	/**
	 * 添加一个close监听.
	 * @param listener
	 */
	public void addCloseListener(SessionCloseListener listener) {
		this.closeListeners.add(listener);
	}

	private List<SessionCloseListener> closeListeners = Lists.newCopyOnWriteArrayList();
	@FunctionalInterface
	public interface SessionCloseListener {
		void close(CloseCause cause);
	}
}
