package org.qiunet.cross.event;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.google.common.base.Preconditions;
import org.qiunet.cross.node.ServerNodeManager;
import org.qiunet.data.util.ServerConfig;
import org.qiunet.flash.handler.common.player.AbstractUserActor;
import org.qiunet.flash.handler.common.player.UserOnlineManager;
import org.qiunet.flash.handler.common.player.event.BaseUserEventData;
import org.qiunet.flash.handler.context.session.DSession;
import org.qiunet.listener.event.EventManager;
import org.qiunet.listener.event.IEventData;

/***
 * 跨服事件处理
 *
 * @author qiunet
 * 2020-10-14 17:28
 */
public class CrossEventManager {
	/**
	 * 跨服的事件.
	 * 功能服 -> 跨服
	 * 跨服 -> 功能服
	 * @param playerId
	 * @param crossSession
	 * @param eventData
	 */
	public static  <T extends BaseUserEventData> void fireCrossEvent(long playerId, DSession crossSession, T eventData) {
		// 当前服的playerActor
		AbstractUserActor playerActor = UserOnlineManager.getPlayerActor(playerId);

		Preconditions.checkNotNull(playerActor, "player actor null");
		Preconditions.checkState(playerActor.isCrossStatus(), "player actor must be cross status");
		Preconditions.checkState(eventData.getClass().isAnnotationPresent(ProtobufClass.class), "Class [%s] need specify annotation @ProtobufClass", eventData.getClass().getName());

		CrossEventRequest request = CrossEventRequest.valueOf(eventData);
		crossSession.writeMessage(request.buildResponseMessage());
	}

	/**
	 * 服务与服务之间的事件触发 .走cross通道.
	 * @param serverId 对方serverId
	 * @param eventData 事件
	 * @param <T>
	 */
	public static <T extends IEventData> void fireCrossEvent(int serverId, T eventData) {
		if (serverId == ServerConfig.getServerId()) {
			EventManager.fireEventHandler(eventData);
			return;
		}

		Preconditions.checkState(eventData.getClass().isAnnotationPresent(ProtobufClass.class), "Class [%s] need specify annotation @ProtobufClass", eventData.getClass().getName());
		CrossEventRequest request = CrossEventRequest.valueOf(eventData);
		ServerNodeManager.getNode(serverId).send(request.buildResponseMessage());
	}
}
