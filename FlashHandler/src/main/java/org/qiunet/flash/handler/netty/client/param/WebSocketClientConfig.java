package org.qiunet.flash.handler.netty.client.param;

import org.qiunet.flash.handler.common.enums.ServerConnType;

import java.net.URI;

/**
 * 使用引导类 参数.
 * 该模式通用 websocket
 * 建造者模式
 * Created by qiunet.
 * 17/7/19
 */
public final class WebSocketClientConfig extends AbstractClientConfig {
	/**
	 * 游戏的uriPath
	 * ws://localhost:8080/ws 后面的/ws
	 */
	private String uriPath = "/ws";

	/**
	 * 是否使用ssl
	 */
	private boolean ssl;

	private WebSocketClientConfig(){}

	@Override
	public ServerConnType getConnType() {
		return ServerConnType.WS;
	}

	public boolean isSsl() {
		return ssl;
	}

	public String getUriPath() {
		return uriPath;
	}

	public URI getURI(){
		StringBuilder sb = new StringBuilder("ws");
		if (ssl) sb.append("s");
		sb.append("://").append(address.getHostString());
		if ((ssl && address.getPort() != 443)
			|| (!ssl && address.getPort() != 80)) {
			sb.append(":").append(address.getPort());
		}
		sb.append(uriPath);
		return URI.create(sb.toString());
	}
	/***
	 * 得到
	 * @return
	 */
	public static Builder custom(){
		WebSocketClientConfig params = new WebSocketClientConfig();
		return params.new Builder();
	}

	/***
	 * 使用build模式 set和 get 分离. 以后有有顺序的构造时候也可以不动
	 * */
	public class Builder extends SuperBuilder<WebSocketClientConfig, Builder> {
		private Builder(){}

		public Builder setUriPath(String uriPath) {
			WebSocketClientConfig.this.uriPath = uriPath;
			return this;
		}

		@Override
		protected WebSocketClientConfig newConfig() {
			return WebSocketClientConfig.this;
		}
	}
}
