package org.qiunet.flash.handler.netty.param;

import org.qiunet.flash.handler.netty.interceptor.HttpInterceptor;

/**
 * 使用引导类 参数.
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

	private HttpInterceptor interceptor;

	private HttpBootstrapParams(){}

	public boolean isSsl() {
		return ssl;
	}

	public String getGameURIPath() {
		return gameURIPath;
	}

	public HttpInterceptor getInterceptor() {
		return interceptor;
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

		private HttpInterceptor interceptor;

		public Builder setSsl(boolean ssl) {
			this.ssl = ssl;
			return this;
		}

		public Builder setInterceptor(HttpInterceptor interceptor) {
			this.interceptor = interceptor;
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
			if (interceptor == null) throw new NullPointerException("Interceptor can not be Null");
			params.gameURIPath = this.gameURIPath;
			params.interceptor = interceptor;
			params.ssl = this.ssl;
		}
	}
}
