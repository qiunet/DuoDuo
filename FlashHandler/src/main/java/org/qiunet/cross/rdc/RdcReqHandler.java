package org.qiunet.cross.rdc;

import org.qiunet.cross.node.ServerNode;
import org.qiunet.flash.handler.context.request.persistconn.IPersistConnRequest;
import org.qiunet.flash.handler.handler.persistconn.PersistConnPbHandler;

/***
 * 接受其它server的请求
 *
 * @author qiunet
 * 2020-10-22 12:39
 */
public class RdcReqHandler extends PersistConnPbHandler<ServerNode, RouteRdcReq> {

	@Override
	public void handler(ServerNode serverNode, IPersistConnRequest<RouteRdcReq> context) throws Exception {
		RouteRdcReq requestData = context.getRequestData();

		IRdcRequest rdcRequestData = requestData.getData();
		DRdc drdc = new DRdc(requestData.getId(), rdcRequestData, serverNode);
		RdcManager.instance.handler(rdcRequestData, drdc);
	}
}
