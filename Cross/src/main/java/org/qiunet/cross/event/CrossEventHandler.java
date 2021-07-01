package org.qiunet.cross.event;

import org.qiunet.flash.handler.common.player.AbstractMessageActor;
import org.qiunet.flash.handler.common.player.AbstractUserActor;
import org.qiunet.flash.handler.common.player.event.BaseUserEventData;
import org.qiunet.flash.handler.context.request.persistconn.IPersistConnRequest;
import org.qiunet.flash.handler.handler.persistconn.PersistConnPbHandler;
import org.qiunet.listener.event.EventManager;
import org.qiunet.listener.event.IEventData;

/***
 * 跨服事件请求
 *
 * @author qiunet
 * 2020-10-15 16:56
 */
public class CrossEventHandler extends PersistConnPbHandler<AbstractMessageActor, CrossEventRequest> {

	@Override
	public void handler(AbstractMessageActor actor, IPersistConnRequest<CrossEventRequest> context) throws Exception {
		CrossEventRequest requestData = context.getRequestData();
		IEventData obj = requestData.getData();
		if (obj instanceof BaseUserEventData) {
			((BaseUserEventData) obj).setPlayer((AbstractUserActor) actor);
		}
		// 可能是PlayerActor CrossPlayerActor . 也可能是ServerNode
		EventManager.fireEventHandler(obj);
	}
}
