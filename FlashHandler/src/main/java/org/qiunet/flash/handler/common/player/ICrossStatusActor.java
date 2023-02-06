package org.qiunet.flash.handler.common.player;

import org.qiunet.cross.event.BaseCrossPlayerEvent;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.context.response.push.IChannelMessage;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.flash.handler.netty.server.constants.CloseCause;

import java.util.function.Consumer;

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
		return currentCrossServerId() != 0;
	}
	/**
	 * 退出所有跨服
	 * @param cause
	 */
	void quitAllCross(CloseCause cause);
	/**
	 * 当前跨服的类型
	 * @return
	 */
	int currentCrossServerId();
	/**
	 * 跨服到某个server
	 * @param serverId 服务ID
	 */
	default void crossToServer(int serverId) {
		crossToServer(serverId, null);
	}

	/**
	 * 跨服到某个server
	 * @param serverId 服务ID
	 * @param resultCallback 结果回调
	 */
	void crossToServer(int serverId, Consumer<Boolean> resultCallback);
	/**
	 * 退出当前的跨服
	 */
	void quitCurrentCross(CloseCause cause);
	/**
	 * 是否有某种类型的服务跨服
	 * @param serverId
	 * @return
	 */
	boolean isCrossStatus(int serverId);
	/**
	 * 给当前跨服的服务发送消息
	 * @return
	 */
	default void sendCrossMessage(IChannelData channelData) {
		this.sendCrossMessage(channelData.buildChannelMessage());
	}
	/**
	 * 给所有的跨服连接发送事件
	 * @param event
	 */
	<E extends BaseCrossPlayerEvent> void allCrossEvent(E event);

	void sendCrossMessage(IChannelMessage<?> channelData);
	/**
	 * 当前的跨服session
	 * @return
	 */
	ISession crossSession();
}
