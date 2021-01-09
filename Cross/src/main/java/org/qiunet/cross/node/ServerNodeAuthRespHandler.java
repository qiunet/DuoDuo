package org.qiunet.cross.node;

import org.qiunet.flash.handler.common.annotation.RequestHandler;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.context.request.persistconn.IPersistConnRequest;
import org.qiunet.flash.handler.handler.persistconn.PersistConnPbHandler;

/***
 * serverNode 鉴权请求的响应
 *
 * @author qiunet
 * 2020-10-22 16:13
 */
@RequestHandler(ID = IProtocolId.System.SERVER_NODE_AUTH_RESP, desc = "serverNode 鉴权请求响应")
public class ServerNodeAuthRespHandler extends PersistConnPbHandler<ServerNode, ServerNodeAuthResponse> {

	@Override
	public void handler(ServerNode serverNode, IPersistConnRequest<ServerNodeAuthResponse> context) throws Exception {
		ServerNodeManager0.instance.authResponse(serverNode, context.getRequestData());
	}
}
