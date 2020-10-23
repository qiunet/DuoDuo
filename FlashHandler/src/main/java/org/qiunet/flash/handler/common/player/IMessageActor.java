package org.qiunet.flash.handler.common.player;

import org.qiunet.flash.handler.common.IMessageHandler;
import org.qiunet.flash.handler.context.session.DSession;

/***
 *
 * @author qiunet
 * 2020/3/1 21:43
 **/
public interface IMessageActor<P extends IMessageActor> extends IMessageHandler<P> {
	/***
	 * 得到 Id playerActor一般是playerId
	 * crossActor 是 serverId
	 * @return
	 */
	long getId();

	/**
	 * 获得session
	 * @return
	 */
	DSession getSession();
	/**
	 * 是否已经鉴权认证.
	 * 一般初始化是只有DSession,
	 * 只有有id了. 才是鉴权成功了.
	 * @return
	 */
	default boolean isAuth() {
		return getId() > 0;
	}
	/**
	 * 鉴权后赋值
	 * @param id
	 */
	void auth(long id);
}
