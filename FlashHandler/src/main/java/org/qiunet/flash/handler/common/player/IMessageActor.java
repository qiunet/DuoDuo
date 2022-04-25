package org.qiunet.flash.handler.common.player;

import org.qiunet.flash.handler.common.IMessageHandler;
import org.qiunet.flash.handler.context.sender.IChannelMessageSender;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.utils.string.StringUtil;

/***
 *
 * @author qiunet
 * 2020/3/1 21:43
 **/
public interface IMessageActor<P extends IMessageActor<P>> extends IMessageHandler<P>, IChannelMessageSender {
	/***
	 * 得到 Id playerActor一般是playerId
	 * crossActor 是 serverId
	 * @return
	 */
	long getId();
	/**
	 * 一个标识.
	 * 最好能明确的找到问题的id. 比如玩家id什么的.
	 * @return
	 */
	default String getIdentity(){
		return StringUtil.format("({0}:{1})", getClass().getSimpleName(), getId());
	}
	/**
	 * 获得session
	 * @return
	 */
	ISession getSender();
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
