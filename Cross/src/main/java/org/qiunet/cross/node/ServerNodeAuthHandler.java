package org.qiunet.cross.node;

import org.qiunet.flash.handler.common.annotation.RequestHandler;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.context.request.tcp.ITcpRequest;
import org.qiunet.flash.handler.handler.tcp.TcpProtobufHandler;

/***
 * serverNode 鉴权请求
 *
 * @author qiunet
 * 2020-10-22 16:13
 */
@RequestHandler(ID = IProtocolId.System.SERVER_NODE_AUTH, desc = "serverNode 鉴权请求")
public class ServerNodeAuthHandler extends TcpProtobufHandler<ServerNode, ServerNodeAuthRequest> {

	@Override
	public void handler(ServerNode playerActor, ITcpRequest<ServerNodeAuthRequest> context) throws Exception {
		playerActor.auth(context.getRequestData().getServerId());
	}

	@Override
	public boolean needAuth() {
		return false;
	}
}
