package org.qiunet.flash.handler.common.player;

import org.qiunet.flash.handler.common.IMessageHandler;
import org.qiunet.flash.handler.context.session.DSession;

/***
 *
 * @author qiunet
 * 2020/3/1 21:43
 **/
public interface IPlayerActor<P extends IPlayerActor> extends IMessageHandler<P> {
	/***
	 * 得到playerId 一般是uid
	 * @return
	 */
	long getPlayerId();

	/**
	 * 获得session
	 * @return
	 */
	DSession getSession();

	/**
	 * openId 或者账号account
	 * @return
	 */
	String getOpenId();

	/**
	 * 是否已经登录认证.
	 * @return
	 */
	boolean isAuth();
}
