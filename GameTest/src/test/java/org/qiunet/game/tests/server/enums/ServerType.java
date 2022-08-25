package org.qiunet.game.tests.server.enums;

import org.qiunet.flash.handler.common.enums.ServerConnType;
import org.qiunet.flash.handler.netty.client.param.HttpClientParams;
import org.qiunet.flash.handler.netty.client.param.IClientConfig;
import org.qiunet.flash.handler.netty.client.param.TcpClientParams;
import org.qiunet.flash.handler.netty.client.param.WebSocketClientParams;

/**
 * Created by qiunet.
 * 17/12/9
 */
public enum ServerType  {
	/**http 逻辑服*/
	HTTP_LOGIC(HttpClientParams.custom().setAddress("localhost", 8080).setUriPath("/f").build()),
	/**长链接 在线服*/
	LC_ONLINE(WebSocketClientParams.custom().setAddress("localhost", 8080).setUriPath("/ws").build()),
	/**长链接 房间服*/
	LC_ROOM(TcpClientParams.custom().setAddress("localhost", 8080).build()),
	;
	private final IClientConfig config;
	ServerType(IClientConfig config) {
		this.config = config;
	}

	public String getName() {
		return name();
	}

	public IClientConfig getClientConfig() {
		return config;
	}

	/**
	 * 得到端口
	 * @return
	 */
	public int port(){
		return getClientConfig().getAddress().getPort();
	}
	/**
	 * 得到地址
	 * @return
	 */
	public String host(){
		return getClientConfig().getAddress().getHostString();
	}
	/***
	 * 得到类型 处理
	 * @return
	 */
	public ServerConnType getType() {
		return getClientConfig().getConnType();
	}
}
