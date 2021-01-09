package org.qiunet.cross.transaction;

import org.qiunet.cross.node.ServerNode;
import org.qiunet.flash.handler.common.annotation.RequestHandler;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.context.request.persistconn.IPersistConnRequest;
import org.qiunet.flash.handler.handler.persistconn.PersistConnPbHandler;

/***
 * 接受其它server的请求
 *
 * @author qiunet
 * 2020-10-22 12:39
 */
@RequestHandler(ID = IProtocolId.System.TRANSACTION_REQ, desc = "处理事务请求")
public class TransactionRequestHandler extends PersistConnPbHandler<ServerNode, RouteTransactionRequest> {

	@Override
	public void handler(ServerNode serverNode, IPersistConnRequest<RouteTransactionRequest> context) throws Exception {
		RouteTransactionRequest requestData = context.getRequestData();

		BaseTransactionRequest transactionRequestData = requestData.getData();
		DTransaction transaction = new DTransaction(requestData.getId(), transactionRequestData, serverNode);
		TransactionManager0.instance.handler(transactionRequestData.getClass(), transaction);
	}
}
