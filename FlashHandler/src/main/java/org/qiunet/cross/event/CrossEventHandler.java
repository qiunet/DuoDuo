package org.qiunet.cross.event;

import org.qiunet.cross.actor.CrossPlayerActor;
import org.qiunet.flash.handler.common.player.*;
import org.qiunet.flash.handler.common.player.event.PlayerEvent;
import org.qiunet.flash.handler.common.player.event.UserEvent;
import org.qiunet.flash.handler.context.request.persistconn.IPersistConnRequest;
import org.qiunet.flash.handler.handler.persistconn.PersistConnPbHandler;
import org.qiunet.utils.listener.event.EventManager;
import org.qiunet.utils.listener.event.IListenerEvent;

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
		IListenerEvent obj = requestData.getData();
		if (obj instanceof UserEvent event) {
			if (actor instanceof AbstractUserActor) {
				((IPlayerFireEvent) actor).fireAsyncEvent(event);
			}else if (requestData.getPlayerId() != 0) {
				actor.runMessageWithMsgExecuteIndex(a -> {
					AbstractUserActor actor0 = UserOnlineManager.instance.returnActor(requestData.getPlayerId());
					if (actor0.isPlayerActor()) {
						((PlayerActor) actor0).fireAsyncEvent(((PlayerEvent) event));
					}else {
						((CrossPlayerActor) actor0).fireAsyncEvent((CrossPlayerEvent) event);
					}
				}, String.valueOf(requestData.getPlayerId()));
			}
			return;
		}


		// 可能是PlayerActor CrossPlayerActor . 也可能是ServerNode
		EventManager.fireEventHandler(obj);
	}
}
