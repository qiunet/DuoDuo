package org.qiunet.flash.handler.netty.client.param;

import java.net.InetSocketAddress;
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
	/***
	 * 接收端口
	 */
	protected InetSocketAddress address;
	/**
	 * 是否使用ssl
	 */
	private boolean ssl;

	private WebSocketClientParams(){}

	public InetSocketAddress getAddress() {
		return address;
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

		private boolean ssl = false;
		protected InetSocketAddress address;

		public WebSocketClientParams.Builder setAddress(InetSocketAddress address) {
			this.address = address;
			return this;
		}
		public WebSocketClientParams.Builder setAddress(String host, int port) {
			return setAddress(new InetSocketAddress(host, port));
		}

		public Builder setSsl(boolean ssl) {
			this.ssl = ssl;
			return this;
		}

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
			if (address == null) throw new NullPointerException("Must set port for Http Listener! ");

			params.address = address;
			params.uriIPath = uriIPath;
		}
	}
}
