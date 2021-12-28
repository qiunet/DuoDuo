package org.qiunet.cross.node;

import org.qiunet.flash.handler.context.request.persistconn.IPersistConnRequest;
import org.qiunet.flash.handler.handler.persistconn.PersistConnPbHandler;
import org.qiunet.utils.logger.LoggerType;

/***
 * serverNode 鉴权请求
 *
 * @author qiunet
 * 2020-10-22 16:13
 */

public class ServerNodeAuthHandler extends PersistConnPbHandler<ServerNode, ServerNodeAuthRequest> {

	@Override
	public void handler(ServerNode playerActor, IPersistConnRequest<ServerNodeAuthRequest> context) throws Exception {
		ServerNodeAuthRequest requestData = context.getRequestData();
		String sign = ServerNodeAuthRequest.makeSign(requestData.getDt());
		if (! sign.equals(requestData.getSign())) {
			LoggerType.DUODUO_CROSS.error("Auth request [{}, {}, {}] sign error.", requestData.getServerId(), requestData.getDt(), requestData.getSign());
			return;
		}

		playerActor.auth(requestData.getServerId());
		playerActor.sendMessage(ServerNodeAuthRsp.valueOf(true));
	}

	@Override
	public boolean needAuth() {
		return false;
	}
}
