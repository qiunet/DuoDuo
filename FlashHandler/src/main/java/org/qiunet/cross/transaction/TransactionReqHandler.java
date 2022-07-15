package org.qiunet.cross.transaction;

import org.qiunet.cross.node.ServerNode;
import org.qiunet.flash.handler.context.request.persistconn.IPersistConnRequest;
import org.qiunet.flash.handler.handler.persistconn.PersistConnPbHandler;

/***
 * 接受其它server的请求
 *
 * @author qiunet
 * 2020-10-22 12:39
 */
public class TransactionReqHandler extends PersistConnPbHandler<ServerNode, RouteTransactionReq> {

	@Override
	public void handler(ServerNode serverNode, IPersistConnRequest<RouteTransactionReq> context) throws Exception {
		RouteTransactionReq requestData = context.getRequestData();

		ITransactionReq transactionRequestData = requestData.getData();
		DTransaction transaction = new DTransaction(requestData.getId(), transactionRequestData, serverNode);
		TransactionManager.instance.handler(transactionRequestData, transaction);
	}
}
