package org.qiunet.flash.handler.context.session;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import org.qiunet.flash.handler.common.player.IPlayerActor;
import org.qiunet.flash.handler.context.response.push.IResponseMessage;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;

/**
 * session 的接口,
 * Created by qiunet.
 * 17/10/23
 */
public interface ISession<P extends IPlayerActor> {


	/***
	 * 得到存放map的唯一key
	 * @return
	 */
	long getUid();

	/**
	 * openid
	 * 或者
	 * 账号 account
	 * @return
	 */
	String getOpenId();
	/***
	 *
	 * @return
	 */
	Channel getChannel();

	/**
	 * 得到玩家相关的对象.
	 * @return
	 */
	default P getPlayerActor(){
		return (P) getChannel().attr(ServerConstants.PLAYER_ACTOR_KEY).get();
	}
	/**
	 * 是否已经认证登录
	 * @return
	 */
	boolean isAuth();

	/**
	 * 是否活跃的session
	 * @return
	 */
	boolean isActive();

	/**
	 * 得到ip
	 * @return
	 */
	String getIp();

	/***
	 * 对外写消息
	 * @param message
	 */
	ChannelFuture writeMessage(IResponseMessage message);

	/**
	 * 清理 session
	 */
	void close(CloseCause cause);
}
