package org.qiunet.cross.node;

import org.qiunet.cross.common.exception.AuthFailException;
import org.qiunet.flash.handler.common.annotation.RequestHandler;
import org.qiunet.flash.handler.common.id.IProtocolId;
import org.qiunet.flash.handler.context.request.tcp.ITcpRequest;
import org.qiunet.flash.handler.handler.tcp.TcpProtobufHandler;

/***
 * serverNode 鉴权请求的响应
 *
 * @author qiunet
 * 2020-10-22 16:13
 */
@RequestHandler(ID = IProtocolId.System.SERVER_NODE_AUTH_RESP, desc = "serverNode 鉴权请求响应")
public class ServerNodeAuthRespHandler extends TcpProtobufHandler<ServerNode, ServerNodeAuthResponse> {

	@Override
	public void handler(ServerNode serverNode, ITcpRequest<ServerNodeAuthResponse> context) throws Exception {
		ServerNodeAuthResponse requestData = context.getRequestData();
		if(requestData.isResult()) {
			ServerNodeManager0.instance.addNode(serverNode);
			serverNode.authPromise.trySuccess(serverNode);
		}else {
			serverNode.authPromise.tryFailure(new AuthFailException());
		}
	}
}
