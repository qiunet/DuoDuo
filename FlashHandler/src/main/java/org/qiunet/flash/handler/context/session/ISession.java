package org.qiunet.flash.handler.context.session;

import io.netty.util.AttributeKey;
import io.netty.util.AttributeMap;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;

/***
 *
 * @author qiunet
 * 2022/4/26 15:18
 */
public interface ISession extends AttributeMap, ISender {

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
	 * @param key key
	 * @param <T> 类型
	 * @return null 或者 里面的对象
	 */
	default <T> T getAttachObj(AttributeKey<T> key) {
		if (!hasAttr(key)) {
			return null;
		}
		return attr(key).get();
	}
	/**
	 * 设置对象到channel
	 * @param key key
	 * @param obj 新的数据
	 * @param <T> 类型
	 * @return 老的数据
	 */
	default <T> T attachObj(AttributeKey<T> key, T obj) {
		return attr(key).getAndSet(obj);
	}

	/**
	 * short id
	 * @return id
	 */
    String aliasId();

    @FunctionalInterface
	interface SessionCloseListener {
		void close(ISession session, CloseCause cause);
	}
}
