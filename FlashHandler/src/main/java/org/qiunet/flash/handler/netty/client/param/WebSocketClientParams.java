package org.qiunet.flash.handler.netty.client.param;

import org.qiunet.flash.handler.common.enums.HandlerType;

import java.net.URI;

/**
 * 使用引导类 参数.
 * 该模式通用 websocket
 * 建造者模式
 * Created by qiunet.
 * 17/7/19
 */
public final class WebSocketClientParams extends AbstractClientParam {
	/**
	 * 游戏的uriPath
	 * ws://localhost:8080/ws 后面的/ws
	 */
	private String uriIPath;

	/**
	 * 是否使用ssl
	 */
	private boolean ssl;

	private WebSocketClientParams(){}

	@Override
	public HandlerType getHandlerType() {
		return HandlerType.WEB_SOCKET;
	}

	public boolean isSsl() {
		return ssl;
	}

	public String getUriIPath() {
		return uriIPath;
	}

	public URI getURI(){
		StringBuilder sb = new StringBuilder("ws");
		if (ssl) sb.append("s");
		sb.append("://").append(address.getHostString());
		if ((ssl && address.getPort() != 443)
			|| (!ssl && address.getPort() != 80)) {
			sb.append(":").append(address.getPort());
		}
		sb.append(uriIPath);
		return URI.create(sb.toString());
	}
	/***
	 * 得到
	 * @return
	 */
	public static Builder custom(){
		return new Builder();
	}

	/***
	 * 使用build模式 set和 get 分离. 以后有有顺序的构造时候也可以不动
	 * */
	public static class Builder extends SuperBuilder<WebSocketClientParams, Builder> {
		private Builder(){}
		// 默认 /f
		private String uriIPath = "/ws";

		public Builder setUriIPath(String uriIPath) {
			this.uriIPath = uriIPath;
			return this;
		}

		@Override
		protected WebSocketClientParams newParams() {
			return new WebSocketClientParams();
		}

		@Override
		protected void buildInner(WebSocketClientParams params) {
			params.uriIPath = uriIPath;
		}
	}
}
