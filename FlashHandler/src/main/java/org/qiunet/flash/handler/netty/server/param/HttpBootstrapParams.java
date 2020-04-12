package org.qiunet.flash.handler.netty.server.param;

/**
 * 使用引导类 参数.
 * 该模式通用 websocket
 * 建造者模式
 * Created by qiunet.
 * 17/7/19
 */
public final class HttpBootstrapParams extends AbstractBootstrapParam {
	/**
	 * 游戏的uriPath
	 * http://localhost:8080/f?a=b&c=d 后面的/f
	 */
	private String gameURIPath;
	/***
	 * 升级websocket的路径 一般 /ws 没有websocket 需求. 不需要设定.
	 * 根据这个参数判断是不是
	 */
	private String websocketPath;

	private HttpBootstrapParams(){}

	public String getWebsocketPath() {
		return websocketPath;
	}

	public String getGameURIPath() {
		return gameURIPath;
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
		// 默认 /f
		private String gameURIPath = "/f";
		/***
		 * 升级websocket的路径
		 */
		private String websocketPath;

		public Builder setWebsocketPath(String websocketPath) {
			this.websocketPath = websocketPath;
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
						if (websocketPath != null) {
				if (websocketPath.equals(gameURIPath)) {
					throw new IllegalArgumentException("gameUrl can equals websocketPath");
				}
				if (errorMessage == null) {
					throw new NullPointerException("IClientErrorMessage can not be Null");
				}
				if (startupContext == null) {
					throw new NullPointerException("startupContext can not be Null");
				}

				params.websocketPath = this.websocketPath;
			}

			params.gameURIPath = this.gameURIPath;
		}
	}
}
