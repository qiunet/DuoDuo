package org.qiunet.cross.transaction;

import org.qiunet.cross.node.ServerNode;
import org.qiunet.flash.handler.common.annotation.RequestHandler;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.context.request.tcp.ITcpRequest;
import org.qiunet.flash.handler.handler.tcp.TcpProtobufHandler;

/***
 * 接受其它server处理后的响应.
 *
 * @author qiunet
 * 2020-10-22 12:39
 */
@RequestHandler(ID = IProtocolId.System.TRANSACTION_RESP, desc = "处理事务请求")
public class TransactionResponseHandler extends TcpProtobufHandler<ServerNode, RouteTransactionResponse> {

	@Override
	public void handler(ServerNode playerActor, ITcpRequest<RouteTransactionResponse> context) throws Exception {
		TransactionManager.instance.completeTransaction(context.getRequestData());
	}
}
