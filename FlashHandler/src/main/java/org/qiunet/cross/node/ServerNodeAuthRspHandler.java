package org.qiunet.cross.node;

import org.qiunet.flash.handler.context.request.persistconn.IPersistConnRequest;
import org.qiunet.flash.handler.handler.persistconn.PersistConnPbHandler;

/***
 * serverNode 鉴权请求的响应
 *
 * @author qiunet
 * 2020-10-22 16:13
 */

public class ServerNodeAuthRspHandler extends PersistConnPbHandler<ServerNode, ServerNodeAuthRsp> {

	@Override
	public void handler(ServerNode serverNode, IPersistConnRequest<ServerNodeAuthRsp> context) throws Exception {
		serverNode.complete();
	}
}
