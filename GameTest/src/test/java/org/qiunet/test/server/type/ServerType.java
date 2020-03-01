package org.qiunet.test.server.type;

import org.qiunet.flash.handler.common.enums.HandlerType;
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
	LC_ONLINE(WebSocketClientParams.custom().setAddress("localhost", 8080).setUriIPath("/ws").build()),
	/**长链接 房间服*/
	LC_ROOM(TcpClientParams.custom().setAddress("localhost", 8081).build()),
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
	public String host() {
		return config.getAddress().getHostName();
	}

	@Override
	public int port() {
		return config.getAddress().getPort();
	}

	@Override
	public HandlerType getType() {
		return config.getHandlerType();
	}

	@Override
	public IClientConfig getClientConfig() {
		return config;
	}
}
