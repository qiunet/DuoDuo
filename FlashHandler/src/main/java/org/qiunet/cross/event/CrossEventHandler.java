package org.qiunet.cross.event;

import org.qiunet.flash.handler.common.player.AbstractMessageActor;
import org.qiunet.flash.handler.common.player.AbstractUserActor;
import org.qiunet.flash.handler.common.player.UserOnlineManager;
import org.qiunet.flash.handler.common.player.event.BasePlayerEventData;
import org.qiunet.flash.handler.common.player.event.BaseUserEventData;
import org.qiunet.flash.handler.common.player.offline.OfflinePlayerActor;
import org.qiunet.flash.handler.common.player.offline.UserOfflineManager;
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
		if (obj instanceof BaseUserEventData) {
			BaseUserEventData eventData = (BaseUserEventData) obj;

			if (actor instanceof AbstractUserActor) {
				((AbstractUserActor) actor).fireEvent(eventData);
			}else if (requestData.getPlayerId() != 0) {
				AbstractUserActor playerActor = UserOnlineManager.getPlayerActor(requestData.getPlayerId());
				if (playerActor != null) {
					// 在线的情况
					playerActor.fireEvent(eventData);
				}else {
					// 不在线的情况
					OfflinePlayerActor offlinePlayerActor = UserOfflineManager.instance.getOrCreate(requestData.getPlayerId());
					offlinePlayerActor.fireEvent((BasePlayerEventData) eventData);
				}
			}
			return;
		}


		// 可能是PlayerActor CrossPlayerActor . 也可能是ServerNode
		EventManager.fireEventHandler(obj);
	}
}
