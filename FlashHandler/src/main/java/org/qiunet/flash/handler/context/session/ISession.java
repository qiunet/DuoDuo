package org.qiunet.flash.handler.context.session;

import io.netty.channel.ChannelFuture;
import io.netty.util.AttributeKey;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.context.response.push.IChannelMessage;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;

/***
 *
 * @author qiunet
 * 2022/4/26 15:18
 */
public interface ISession {

	/**
	 * 是否活跃
	 * @return
	 */
	boolean isActive();

	/**
	 * flush
	 */
	void flush();
	/**
	 * 添加一个close 监听
	 * @param listener
	 */
	void addCloseListener(String name, BaseSession.SessionCloseListener listener);

	/**
	 * 清理Close listener
	 *
	 */
	void clearCloseListener();

	/**
	 * 关闭连接. 只能一次
	 * @param cause
	 */
	void close(CloseCause cause);
	/**
	 * 获得ip
	 * @return
	 */
	String getIp();

	/**
	 * 获得channel里面的对象.
	 * @param key
	 * @param <T>
	 * @return
	 */
	<T> T getAttachObj(AttributeKey<T> key);

	/**
	 * 设置对象到channel
	 * @param key
	 * @param obj
	 * @param <T>
	 */
	<T> void attachObj(AttributeKey<T> key, T obj);
	/**
	 * 发送消息
	 * @param message
	 * @return
	 */
	default ChannelFuture sendMessage(IChannelMessage<?> message) {
		return this.sendMessage(message, false);
	}
	/**
	 * 发送消息
	 * @param message
	 */
	default ChannelFuture sendMessage(IChannelData message) {
		return this.sendMessage(message.buildChannelMessage());
	}
	/**
	 * 发送消息
	 * @param message
	 */
	default ChannelFuture sendMessage(IChannelData message, boolean flush) {
		return this.sendMessage(message.buildChannelMessage(), flush);
	}
	/**
	 * 发送消息
	 * @param message
	 */
	ChannelFuture sendMessage(IChannelMessage<?> message, boolean flush);

	@FunctionalInterface
	interface SessionCloseListener {
		void close(ISession session, CloseCause cause);
	}
}
