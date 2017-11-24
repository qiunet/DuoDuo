package org.qiunet.flash.handler.netty.server.param;

import org.qiunet.flash.handler.netty.server.interceptor.TcpInterceptor;

/**
 * 使用引导类 参数.
 * 建造者模式
 * Created by qiunet.
 * 17/7/19
 */
public final class TcpBootstrapParams extends AbstractBootstrapParam {

	private TcpInterceptor interceptor;

	private TcpBootstrapParams(){}

	public TcpInterceptor getInterceptor() {
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
	 */
	public static class Builder extends SuperBuilder<TcpBootstrapParams, Builder> {

		private TcpInterceptor interceptor;

		private Builder(){}

		public Builder setInterceptor(TcpInterceptor interceptor) {
			this.interceptor = interceptor;
			return this;
		}

		@Override
		protected TcpBootstrapParams newParams() {
			return new TcpBootstrapParams();
		}

		@Override
		protected void buildInner(TcpBootstrapParams tcpBootstrapParams) {
			if (interceptor == null) throw new NullPointerException("Interceptor can not be Null");

			tcpBootstrapParams.interceptor = this.interceptor;
		}
	}
}
