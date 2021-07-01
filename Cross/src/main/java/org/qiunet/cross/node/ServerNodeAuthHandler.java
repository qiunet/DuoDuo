package org.qiunet.cross.node;

import org.qiunet.flash.handler.context.request.persistconn.IPersistConnRequest;
import org.qiunet.flash.handler.handler.persistconn.PersistConnPbHandler;

/***
 * serverNode 鉴权请求
 *
 * @author qiunet
 * 2020-10-22 16:13
 */

public class ServerNodeAuthHandler extends PersistConnPbHandler<ServerNode, ServerNodeAuthRequest> {

	@Override
	public void handler(ServerNode playerActor, IPersistConnRequest<ServerNodeAuthRequest> context) throws Exception {
		playerActor.auth(context.getRequestData().getServerId());
	}

	@Override
	public boolean needAuth() {
		return false;
	}
}
