package org.qiunet.cross.actor.data;

import org.qiunet.flash.handler.common.player.PlayerActor;
import org.qiunet.flash.handler.common.player.UserOnlineManager;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.listener.event.EventListener;

/***
 * 获取跨服数据的rpc处理
 *
 * @author qiunet
 * 2020-10-28 12:37
 */
public enum CrossDataHandler {
	instance;

	public CrossDataRpcRsp handler(CrossDataRpcReq request) {
		CrossData<?> crossData = (CrossData<?>) CrossData.valueOf(CrossData.class, request.getKey());
		PlayerActor playerActor = UserOnlineManager.instance.getPlayerActor(request.getPlayerId());
		IUserTransferData data = crossData.get(playerActor);
		return CrossDataRpcRsp.valueOf(data);
	}


	@EventListener
	private void crossDataUpdateEvent(_CrossDataNeedUpdateEvent event) {
		CrossData crossData = CrossData.valueOf(CrossData.class, event.getKey());
		Object data = JsonUtil.getGeneralObj(event.getJson(), crossData.getDataClz());
		crossData.update(event.getPlayer(), (IUserTransferData) data);
	}
}
