package org.qiunet.cross.rpc;

import org.qiunet.cross.node.ServerNode;
import org.qiunet.flash.handler.context.request.persistconn.IPersistConnRequest;
import org.qiunet.flash.handler.handler.persistconn.PersistConnPbHandler;

/***
 * 处理rpc的响应
 *
 * @author qiunet
 * 2023/5/5 17:30
 */
public class RpcRspHandler extends PersistConnPbHandler<ServerNode, RouteRpcRsp> {

	@Override
	public void handler(ServerNode playerActor, IPersistConnRequest<RouteRpcRsp> context) throws Exception {
		RouteRpcRsp requestData = context.getRequestData();
		RpcManager.complete(requestData.getReqId(), requestData.getJsonData().getData());
	}
}
