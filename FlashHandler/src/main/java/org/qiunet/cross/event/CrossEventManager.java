package org.qiunet.cross.event;

import org.qiunet.cross.node.ServerNodeManager;
import org.qiunet.data.conf.ServerConfig;
import org.qiunet.flash.handler.common.CommMessageHandler;
import org.qiunet.flash.handler.common.player.AbstractUserActor;
import org.qiunet.flash.handler.common.player.UserOnlineManager;
import org.qiunet.flash.handler.common.player.event.UserEvent;
import org.qiunet.utils.listener.event.EventManager;
import org.qiunet.utils.listener.event.ICrossListenerEvent;

/***
 * 跨服事件处理
 *
 * @author qiunet
 * 2020-10-14 17:28
 */
public final class CrossEventManager {
	/**
	 * 服务与服务之间的事件触发 .走cross通道.
	 * @param serverId 对方serverId
	 * @param eventData 事件
	 * @param <T>
	 */
	public static <T extends ICrossListenerEvent> void fireCrossEvent(int serverId, T eventData) {
		if (serverId == ServerConfig.getServerId()) {
			EventManager.fireEventHandler(eventData);
			return;
		}

		ServerNodeManager.getNode(serverId, node -> node.fireCrossEvent(eventData));
	}

	/**
	 * 触发ServerId服务器事件. 触发指定的玩家事件.
	 * @param serverId 对方serverId
	 * @param event 事件
	 * @param <T>
	 */
	public static <T extends UserEvent & ICrossListenerEvent> void fireCrossUserEvent(int serverId, T event, long playerId) {
		if (serverId == ServerConfig.getServerId()) {
			CommMessageHandler.DEFAULT.runMessageWithMsgExecuteIndex(h -> {
				AbstractUserActor playerActor = UserOnlineManager.instance.returnActor(playerId);
				playerActor.addMessage(p -> {
					EventManager.post(event.setPlayer((AbstractUserActor) p));
				});
			}, String.valueOf(playerId));
			return;
		}
		ServerNodeManager.getNode(serverId, node ->node.fireUserCrossEvent(event, playerId));
	}
}
