package org.qiunet.test.cross.common.event;

import org.qiunet.cross.transaction.TransactionFuture;
import org.qiunet.cross.transaction.TransactionManager;
import org.qiunet.test.cross.common.Constants;
import org.qiunet.test.cross.common.transaction.TestTransactionReq;
import org.qiunet.test.cross.common.transaction.TestTransactionRsp;
import org.qiunet.utils.listener.event.EventListener;
import org.qiunet.utils.logger.LoggerType;

/***
 *
 *
 * @author qiunet
 * 2020-10-22 22:26
 */
public enum EventAcceptService {
	instance;

	@EventListener
	public void login(PlayerLoginEventData eventData) {
		LoggerType.DUODUO_CROSS.info("PlayerId: {},登录事件", eventData.getPlayer().getId());
	}

	@EventListener
	public void crossLogin(CrossPlayerLoginEventData eventData) {
		LoggerType.DUODUO_CROSS.info("PlayerId: {},跨服登录事件", eventData.getPlayer().getId());

		try {
			TransactionFuture<TestTransactionRsp> transactionFuture = TransactionManager.instance.beginTransaction(Constants.LOGIC_SERVER_ID, TestTransactionReq.valueOf(eventData.getPlayer().getPlayerId()));
			TestTransactionRsp response = transactionFuture.get();
			LoggerType.DUODUO_CROSS.info("PlayerId: {},跨服事务", response.getPlayerId());
		} catch (Exception e) {
			LoggerType.DUODUO_CROSS.error("出现异常:{}", e);
		}
	}
}
