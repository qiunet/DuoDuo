package org.qiunet.cross.transaction;

import org.qiunet.cross.node.ServerNode;
import org.qiunet.flash.handler.common.annotation.RequestHandler;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.context.request.tcp.ITcpRequest;
import org.qiunet.flash.handler.handler.tcp.TcpProtobufHandler;
import org.qiunet.utils.protobuf.ProtobufDataManager;

/***
 * 接受其它server的请求
 *
 * @author qiunet
 * 2020-10-22 12:39
 */
@RequestHandler(ID = IProtocolId.System.TRANSACTION_REQ, desc = "处理事务请求")
public class TransactionRequestHandler extends TcpProtobufHandler<ServerNode, RouteTransactionRequest> {

	@Override
	public void handler(ServerNode serverNode, ITcpRequest<RouteTransactionRequest> context) throws Exception {
		RouteTransactionRequest requestData = context.getRequestData();

		Class<? extends BaseTransactionRequest> aClass = (Class<? extends BaseTransactionRequest>) Class.forName(requestData.getReqClassName());
		Object obj = ProtobufDataManager.decode(aClass, requestData.getReqData());
		DTransaction transaction = new DTransaction(requestData.getId(), (BaseTransactionRequest) obj, serverNode);
		TransactionManager0.instance.handler(aClass, transaction);
	}
}
