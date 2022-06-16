package org.qiunet.flash.handler.common.player;

import org.qiunet.data.util.ServerType;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.context.response.push.IChannelMessage;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;

/***
 * 跨服状态的actor
 *
 * @author qiunet
 * 2020-10-26 14:54
 */
public interface ICrossStatusActor {
	/**
	 * 是否跨服状态
	 * @return
	 */
	default boolean isCrossStatus() {
		return currentCrossType() != null;
	}
	/**
	 * 退出所有跨服
	 * @param cause
	 */
	void quitAllCross(CloseCause cause);
	/**
	 * 退出指定类型的跨服
	 * @param serverType 指定类型
	 * @param cause
	 */
	void quitCross(ServerType serverType, CloseCause cause);

	/**
	 * 当前跨服的类型
	 * @return
	 */
	ServerType currentCrossType();
	/**
	 * 跨服到某个server
	 * @param serverId
	 */
	void crossToServer(int serverId);
	/**
	 * 退出当前的跨服
	 */
	default void quitCurrentCross(CloseCause cause) {
		if (! isCrossStatus()) {
			return;
		}
		this.quitCross(this.currentCrossType(), cause);
	}
	/**
	 * 切换跨服
	 * @param serverType
	 */
	void switchCross(ServerType serverType);
	/**
	 * 是否有某种类型的服务跨服
	 * @param serverType
	 * @return
	 */
	boolean isCrossStatus(ServerType serverType);
	/**
	 * 给当前跨服的服务发送消息
	 * @return
	 */
	default void sendCrossMessage(IChannelData channelData) {
		this.sendCrossMessage(channelData.buildChannelMessage());
	}

	void sendCrossMessage(IChannelMessage<?> channelData);
	/**
	 * 当前的跨服session
	 * @return
	 */
	ISession crossSession();
}
