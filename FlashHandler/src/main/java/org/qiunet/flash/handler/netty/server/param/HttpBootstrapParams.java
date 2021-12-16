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
	private String gameURIPath = "/f";
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
		HttpBootstrapParams params = new HttpBootstrapParams();
		return params.new Builder();
	}

	/***
	 * 使用build模式 set和 get 分离. 以后有有顺序的构造时候也可以不动
	 * */
	public class Builder extends AbstractBootstrapParam.SuperBuilder<HttpBootstrapParams, Builder> {
		private Builder(){}

		public Builder setWebsocketPath(String websocketPath) {
			HttpBootstrapParams.this.websocketPath = websocketPath;
			return this;
		}

		public Builder setGameURIPath(String gameURIPath) {
			HttpBootstrapParams.this.gameURIPath = gameURIPath;
			return this;
		}

		@Override
		protected HttpBootstrapParams newParams() {
			if (websocketPath != null) {
				if (websocketPath.equals(gameURIPath)) {
					throw new IllegalArgumentException("gameUrl can not equals websocketPath");
				}

				if (startupContext == null) {
					throw new NullPointerException("startupContext can not be Null");
				}

				if (protocolHeaderType == null) {
					throw new NullPointerException("Must set IProtocolHeaderType for Listener!");
				}
			}
			return HttpBootstrapParams.this;
		}
	}
}
