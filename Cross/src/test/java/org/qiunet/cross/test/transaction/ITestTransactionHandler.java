package org.qiunet.cross.test.transaction;

import org.qiunet.cross.transaction.DTransaction;
import org.qiunet.cross.transaction.ITransactionHandler;

/***
 *
 * @author qiunet
 * 2020-10-23 20:56
 **/
public class ITestTransactionHandler implements ITransactionHandler<TestTransactionRequest, TestTransactionResponse> {

	@Override
	public void handler(DTransaction<TestTransactionRequest, TestTransactionResponse> transaction) {
		transaction.handler(req -> TestTransactionResponse.valueOf(req.getPlayerId()));
	}
}
