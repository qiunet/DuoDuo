package org.qiunet.cross.test.event;

import org.qiunet.cross.test.common.Constants;
import org.qiunet.cross.test.proto.resp.CrossLoginResponse;
import org.qiunet.cross.test.transaction.TestTransactionRequest;
import org.qiunet.cross.test.transaction.TestTransactionResponse;
import org.qiunet.cross.transaction.TransactionFuture;
import org.qiunet.cross.transaction.TransactionManager;
import org.qiunet.listener.event.EventListener;
import org.qiunet.utils.logger.LoggerType;

import java.util.concurrent.ExecutionException;

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

		eventData.getPlayer().sendMessage(new CrossLoginResponse());

		TransactionFuture<TestTransactionResponse> transactionFuture = TransactionManager.instance.beginTransaction(Constants.LOGIC_SERVER_ID, TestTransactionRequest.valueOf(eventData.getPlayer().getPlayerId()));
		try {
			TestTransactionResponse response = transactionFuture.get();
			LoggerType.DUODUO_CROSS.info("PlayerId: {},跨服事务", response.getPlayerId());
		} catch (ExecutionException | InterruptedException e) {
			LoggerType.DUODUO_CROSS.error("出现异常:{}", e.getMessage());
		}
	}
}
