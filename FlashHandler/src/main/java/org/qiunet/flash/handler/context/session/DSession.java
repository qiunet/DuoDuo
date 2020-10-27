package org.qiunet.flash.handler.context.session;

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
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Session 的 父类
 * Created by qiunet.
 * 17/11/26
 */
public final class DSession {
	private Logger logger = LoggerType.DUODUO_FLASH_HANDLER.getLogger();
	protected Channel channel;

	public DSession(Channel channel) {
		channel.closeFuture().addListener(f -> this.close(CloseCause.CHANNEL_CLOSE));
		this.channel = channel;
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
		String ip = "";
		if (channel.remoteAddress() instanceof InetSocketAddress) {
			ip = ((InetSocketAddress) channel.remoteAddress()).getAddress().getHostAddress();
		}
		return ip;
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
		if ( logger.isInfoEnabled()
			&& ! message.getContent().getClass().isAnnotationPresent(SkipDebugOut.class)) {
			IMessageActor messageActor = getAttachObj(ServerConstants.MESSAGE_ACTOR_KEY);
			if (messageActor != null) {
				logger.info("[{}] >>> {}", messageActor.getIdent(), message.toStr());
			}
		}
		return channel.writeAndFlush(message.encode());
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
