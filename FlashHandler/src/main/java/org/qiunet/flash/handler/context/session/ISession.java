package org.qiunet.flash.handler.context.session;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import org.qiunet.flash.handler.context.sender.IChannelMessageSender;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;

/***
 *
 * @author qiunet
 * 2022/4/26 15:18
 */
public interface ISession extends IChannelMessageSender {
	/**
	 * 绑定kcp session
	 * 仅 tcp 和 ws可以.
	 * @param kcpSession
	 */
	void bindKcpSession(KcpSession kcpSession);

	/**
	 * 获得kcp session
	 * @return
	 */
	 KcpSession getKcpSession();
	/**
	 * 是否活跃
	 * @return
	 */
	boolean isActive();

	/**
	 * 得到channel
	 * @return
	 */
	Channel channel();

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
}
