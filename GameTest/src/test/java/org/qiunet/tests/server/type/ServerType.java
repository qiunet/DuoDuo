package org.qiunet.tests.server.type;

import org.qiunet.flash.handler.netty.client.param.HttpClientParams;
import org.qiunet.flash.handler.netty.client.param.IClientConfig;
import org.qiunet.flash.handler.netty.client.param.TcpClientParams;
import org.qiunet.flash.handler.netty.client.param.WebSocketClientParams;
import org.qiunet.test.server.IServer;

/**
 * Created by qiunet.
 * 17/12/9
 */
public enum ServerType implements IServer {
	/**http 逻辑服*/
	HTTP_LOGIC(HttpClientParams.custom().setAddress("localhost", 8080).setUriPath("/f").build()),
	/**长链接 在线服*/
	LC_ONLINE(WebSocketClientParams.custom().setAddress("localhost", 8080).setUriPath("/ws").build()),
	/**长链接 房间服*/
	LC_ROOM(TcpClientParams.DEFAULT_PARAMS) {
		@Override
		public String host() {
			return "localhost";
		}

		@Override
		public int port() {
			return 8081;
		}
	},
	;
	private IClientConfig config;
	ServerType(IClientConfig config) {
		this.config = config;
	}

	@Override
	public String getName() {
		return name();
	}

	@Override
	public IClientConfig getClientConfig() {
		return config;
	}
}
