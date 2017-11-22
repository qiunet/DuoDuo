package org.qiunet.flash.handler.netty.param;

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

	private HttpBootstrapParams(){}

	public boolean isSsl() {
		return ssl;
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

		public Builder setSsl(boolean ssl) {
			this.ssl = ssl;
			return this;
		}

		@Override
		protected HttpBootstrapParams newParams() {
			return new HttpBootstrapParams();
		}

		@Override
		protected void buildInner(HttpBootstrapParams params) {
			params.address = this.address;
			params.ssl = this.ssl;
		}
	}
}
