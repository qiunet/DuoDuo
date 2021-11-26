package org.qiunet.cross.actor.data;

import org.qiunet.cross.transaction.DTransaction;
import org.qiunet.cross.transaction.ITransactionHandler;
import org.qiunet.flash.handler.common.player.AbstractUserActor;
import org.qiunet.flash.handler.common.player.PlayerActor;
import org.qiunet.flash.handler.common.player.UserOnlineManager;

/***
 * 获取跨服数据的事务处理
 *
 * @author qiunet
 * 2020-10-28 12:37
 */
public class CrossDataTransactionHandler implements ITransactionHandler<CrossDataTransactionRequest, CrossDataTransactionResponse> {

	@Override
	public void handler(DTransaction<CrossDataTransactionRequest, CrossDataTransactionResponse> transaction) {
		CrossDataTransactionRequest request = transaction.getReqData();
		AbstractUserActor playerActor = UserOnlineManager.getPlayerActor(request.getPlayerId());
		playerActor.addMessage(p -> transaction.handler(req -> {
			CrossData crossData = CrossData.get(req.getKey());
			BaseCrossTransferData data = crossData.create((PlayerActor) p);
			return CrossDataTransactionResponse.valueOf(data);
		}));
	}
}
