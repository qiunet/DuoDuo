package org.qiunet.cross.actor.data;

import org.qiunet.cross.transaction.DTransaction;
import org.qiunet.cross.transaction.ITransactionHandler;
import org.qiunet.flash.handler.common.player.PlayerActor;
import org.qiunet.flash.handler.common.player.UserOnlineManager;
import org.qiunet.utils.json.JsonUtil;
import org.qiunet.utils.listener.event.EventListener;

/***
 * 获取跨服数据的事务处理
 *
 * @author qiunet
 * 2020-10-28 12:37
 */
public class CrossDataTransactionHandler implements ITransactionHandler<CrossDataTransactionReq, CrossDataTransactionRsp> {

	@Override
	public void handler(DTransaction<CrossDataTransactionReq, CrossDataTransactionRsp> transaction) {
		CrossDataTransactionReq request = transaction.getReqData();
		PlayerActor playerActor = UserOnlineManager.instance.getPlayerActor(request.getPlayerId());
		playerActor.addMessage(p -> transaction.handler(req -> {
			CrossData<?> crossData = (CrossData<?>) CrossData.valueOf(CrossData.class, req.getKey());
			IUserTransferData data = crossData.get(p);
			return CrossDataTransactionRsp.valueOf(data);
		}));
	}

	@EventListener
	private void crossDataUpdateEvent(_CrossDataNeedUpdateEvent event) {
		CrossData crossData = (CrossData) CrossData.valueOf(CrossData.class, event.getKey());
		Object data = JsonUtil.getGeneralObj(event.getJson(), crossData.getDataClz());
		crossData.update(event.getPlayer(), (IUserTransferData) data);
	}
}
