package org.qiunet.flash.handler.common.player;

import org.qiunet.flash.handler.context.session.ISession;

/***
 *
 * @author qiunet
 * 2020/3/1 21:43
 **/
public interface IPlayerActor<Session extends ISession> {
	/***
	 * 得到playerId 一般是uid
	 * @return
	 */
	long getPlayerId();

	/**
	 * 获得session
	 * @return
	 */
	Session getSession();

	/**
	 * 是否已经登录认证.
	 * @return
	 */
	boolean isAuth();
}
