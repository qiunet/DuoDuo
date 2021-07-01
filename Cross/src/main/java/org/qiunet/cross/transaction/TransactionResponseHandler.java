package org.qiunet.cross.transaction;

import org.qiunet.cross.node.ServerNode;
import org.qiunet.flash.handler.context.request.persistconn.IPersistConnRequest;
import org.qiunet.flash.handler.handler.persistconn.PersistConnPbHandler;

/***
 * 接受其它server处理后的响应.
 *
 * @author qiunet
 * 2020-10-22 12:39
 */
public class TransactionResponseHandler extends PersistConnPbHandler<ServerNode, RouteTransactionResponse> {

	@Override
	public void handler(ServerNode playerActor, IPersistConnRequest<RouteTransactionResponse> context) throws Exception {
		TransactionManager.instance.completeTransaction(context.getRequestData());
	}
}
