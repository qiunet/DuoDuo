package org.qiunet.flash.handler.netty.server.param;
import org.qiunet.flash.handler.netty.server.interceptor.HttpInterceptor;
import org.qiunet.flash.handler.netty.server.interceptor.WebSocketInterceptor;

/**
 * 使用引导类 参数.
 * 该模式通用 websocket
 * 建造者模式
 * Created by qiunet.
 * 17/7/19
 */
public final class HttpBootstrapParams extends AbstractBootstrapParam {
	/**
	 * 使用ssl 模式
	 */
	private boolean ssl;
	/**
	 * 游戏的uriPath
	 * http://localhost:8080/f?a=b&c=d 后面的/f
	 */
	private String gameURIPath;
	/***
	 * 升级websocket的路径
	 */
	private String websocketPath;

	private HttpInterceptor httpInterceptor;

	private WebSocketInterceptor webSocketInterceptor;


	private HttpBootstrapParams(){}

	public WebSocketInterceptor getWebSocketInterceptor() {
		return webSocketInterceptor;
	}

	public String getWebsocketPath() {
		return websocketPath;
	}

	public boolean isSsl() {
		return ssl;
	}

	public String getGameURIPath() {
		return gameURIPath;
	}

	public HttpInterceptor getHttpInterceptor() {
		return httpInterceptor;
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
	public static class Builder extends AbstractBootstrapParam.SuperBuilder<HttpBootstrapParams, Builder> {
		private Builder(){}
		private boolean ssl;
		// 默认 /f
		private String gameURIPath = "/f";
		/***
		 * 升级websocket的路径
		 */
		private String websocketPath;

		private HttpInterceptor httpInterceptor;

		private WebSocketInterceptor webSocketInterceptor;

		public Builder setWebSocketInterceptor(WebSocketInterceptor webSocketInterceptor) {
			this.webSocketInterceptor = webSocketInterceptor;
			return this;
		}

		public Builder setWebsocketPath(String websocketPath) {
			this.websocketPath = websocketPath;
			return this;
		}

		public Builder setSsl(boolean ssl) {
			this.ssl = ssl;
			return this;
		}

		public Builder setHttpInterceptor(HttpInterceptor httpInterceptor) {
			this.httpInterceptor = httpInterceptor;
			return this;
		}

		public Builder setGameURIPath(String gameURIPath) {
			this.gameURIPath = gameURIPath;
			return this;
		}

		@Override
		protected HttpBootstrapParams newParams() {
			return new HttpBootstrapParams();
		}

		@Override
		protected void buildInner(HttpBootstrapParams params) {
			if (httpInterceptor == null) throw new NullPointerException("httpInterceptor can not be Null");
			if (websocketPath != null) {
				if (websocketPath.equals(gameURIPath)) throw new IllegalArgumentException("gameUrl can equals websocketPath");
				if (webSocketInterceptor == null) throw new NullPointerException("webSocketInterceptor can not be Null");
				if (errorMessage == null) throw new NullPointerException("IClientErrorMessage can not be Null");
				params.webSocketInterceptor = this.webSocketInterceptor;
				params.websocketPath = this.websocketPath;
			}

			params.gameURIPath = this.gameURIPath;
			params.httpInterceptor = this.httpInterceptor;
			params.ssl = this.ssl;
		}
	}
}
