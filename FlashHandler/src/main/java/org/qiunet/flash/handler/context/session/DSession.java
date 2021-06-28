package org.qiunet.flash.handler.context.session;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.ScheduledFuture;
import org.qiunet.flash.handler.common.annotation.SkipDebugOut;
import org.qiunet.flash.handler.common.player.IMessageActor;
import org.qiunet.flash.handler.context.response.push.IChannelMessage;
import org.qiunet.flash.handler.context.sender.IChannelMessageSender;
import org.qiunet.flash.handler.context.session.future.DChannelFutureWrapper;
import org.qiunet.flash.handler.context.session.future.IDSessionFuture;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
import org.qiunet.flash.handler.util.ChannelUtil;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Session 的 父类
 * Created by qiunet.
 * 17/11/26
 */
public final class DSession implements IChannelMessageSender {
	private final Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();
	/**
	 * 写次数计数
	 */
	private final AtomicInteger counter = new AtomicInteger();
	/**
	 * 是否默认flush
	 */
	private boolean default_flush;
	/**
	 * 默认flush延时毫秒时间
	 */
	private int flush_delay_ms = 50;


	protected Channel channel;
	/**
	 * 判断是否已经在计时flush
	 */
	private final AtomicBoolean flushScheduling = new AtomicBoolean();

	public DSession(Channel channel) {
		this.channel = channel;
		if (channel != null) {
			// 除了测试. 这里不会为null
			channel.closeFuture().addListener(f -> this.close(CloseCause.CHANNEL_CLOSE));
		}
	}
	/**
	 * 设置flush的参数
	 * @param default_flush 是否默认flush. 是. 每个message都flush, 否 则需要设置下面的参数
	 * @param flush_delay_ms flush延迟毫秒数.
	 */
	public DSession flushConfig(boolean default_flush, int flush_delay_ms) {
		Preconditions.checkState(default_flush || (flush_delay_ms >= 5 && flush_delay_ms < 1000));
		this.flush_delay_ms = flush_delay_ms;
		this.default_flush = default_flush;
		return this;
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

	private ScheduledFuture<?> flushSchedule;
	/**
	 * flush
	 */
	private void flush0(){
		flushScheduling.set(false);
		this.flushSchedule = null;
		counter.set(0);
		channel.flush();
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

	private final AtomicBoolean closed = new AtomicBoolean();
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
			sj.add(messageActor.getIdentity());
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

	private final List<SessionCloseListener> closeListeners = Lists.newCopyOnWriteArrayList();

	@Override
	public DSession getSession() {
		return this;
	}

	@Override
	public IDSessionFuture sendMessage(IChannelMessage<?> message) {
		return this.sendMessage(message, default_flush);
	}

	@Override
	public IDSessionFuture sendMessage(IChannelMessage<?> message, boolean flush) {
		if ( logger.isInfoEnabled()
				&& ! message.getContent().getClass().isAnnotationPresent(SkipDebugOut.class)) {
			IMessageActor messageActor = getAttachObj(ServerConstants.MESSAGE_ACTOR_KEY);
			if (messageActor != null) {
				logger.info("[{}] >>> {}", messageActor.getIdentity(), message.toStr());
			}
		}

		if (flush) {
			return new DChannelFutureWrapper(channel.writeAndFlush(message.encode()));
		}

		IDSessionFuture future = new DChannelFutureWrapper(channel.write(message.encode()));
		if (counter.incrementAndGet() >= 10) {
			// 次数够也flush
			if (this.flushSchedule != null && ! this.flushSchedule.isDone()) {
				this.flushSchedule.cancel(false);
			}
			this.flush0();
			return future;
		}

		if (flushScheduling.compareAndSet(false, true)) {
			this.flushSchedule = channel.eventLoop().schedule(this::flush0, flush_delay_ms, TimeUnit.MILLISECONDS);
		}
		return future;
	}

	@FunctionalInterface
	public interface SessionCloseListener {
		void close(CloseCause cause);
	}
}
