package org.qiunet.test.cross.common.transaction;

import org.qiunet.cross.transaction.DTransaction;
import org.qiunet.cross.transaction.ITransactionHandler;

/***
 *
 * @author qiunet
 * 2020-10-23 20:56
 **/
public class ITestTransactionHandler implements ITransactionHandler<TestTransactionReq, TestTransactionRsp> {

	@Override
	public void handler(DTransaction<TestTransactionReq, TestTransactionRsp> transaction) {
		transaction.handler(req -> TestTransactionRsp.valueOf(req.getPlayerId()));
	}
}
