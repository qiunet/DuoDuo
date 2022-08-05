package org.qiunet.cross.event;

import org.qiunet.flash.handler.common.player.AbstractMessageActor;
import org.qiunet.flash.handler.common.player.AbstractUserActor;
import org.qiunet.flash.handler.common.player.UserOnlineManager;
import org.qiunet.flash.handler.common.player.event.OfflineUserRequestEvent;
import org.qiunet.flash.handler.common.player.event.UserEventData;
import org.qiunet.flash.handler.context.request.persistconn.IPersistConnRequest;
import org.qiunet.flash.handler.handler.persistconn.PersistConnPbHandler;
import org.qiunet.utils.listener.event.EventManager;
import org.qiunet.utils.listener.event.IEventData;

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
		if (obj instanceof UserEventData) {
			UserEventData eventData = (UserEventData) obj;

			if (actor instanceof AbstractUserActor) {
				((AbstractUserActor) actor).fireAsyncEvent(eventData);
			}else if (requestData.getPlayerId() != 0) {
				AbstractUserActor playerActor = UserOnlineManager.instance.getActor(requestData.getPlayerId());
				if (playerActor != null) {
					// 在线的情况
					playerActor.fireAsyncEvent(eventData);
				}else {
					OfflineUserRequestEvent.valueOf(eventData, requestData.getPlayerId()).fireEventHandler();
				}
			}
			return;
		}


		// 可能是PlayerActor CrossPlayerActor . 也可能是ServerNode
		EventManager.fireEventHandler(obj);
	}
}
