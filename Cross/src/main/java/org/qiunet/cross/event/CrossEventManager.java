package org.qiunet.cross.event;

import org.qiunet.flash.handler.common.player.event.BasePlayerEventData;
import org.qiunet.flash.handler.context.session.DSession;
import org.qiunet.utils.protobuf.ProtobufDataManager;

/***
 * 跨服事件处理
 *
 * @author qiunet
 * 2020-10-14 17:28
 */
public enum CrossEventManager {
	instance;

	/**
	 * 跨服的事件.
	 * 功能服 -> 跨服
	 * 跨服 -> 功能服
	 * @param playerId
	 * @param crossSession
	 * @param eventData
	 */
	public <T extends BasePlayerEventData> void fireCrossEvnet(long playerId, DSession crossSession, T eventData) {
		byte[] bytes = ProtobufDataManager.encode((Class<T>)eventData.getClass(), eventData);
		CrossEventRequest request = CrossEventRequest.valueOf(playerId, eventData.getClass().getName(), bytes);
		crossSession.writeMessage(request.buildResponseMessage());
	}
}
