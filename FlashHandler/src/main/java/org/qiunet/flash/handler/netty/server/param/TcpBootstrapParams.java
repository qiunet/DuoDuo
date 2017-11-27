package org.qiunet.flash.handler.netty.server.param;

import org.qiunet.flash.handler.context.session.DefaultSessionBuilder;
import org.qiunet.flash.handler.context.session.ISessionBuilder;
import org.qiunet.flash.handler.context.session.ISessionEvent;
import org.qiunet.flash.handler.netty.server.interceptor.TcpInterceptor;
import org.qiunet.flash.handler.netty.server.tcp.error.IClientErrorMessage;

/**
 * 使用引导类 参数.
 * 建造者模式
 * Created by qiunet.
 * 17/7/19
 */
public final class TcpBootstrapParams extends AbstractBootstrapParam {
	private TcpInterceptor interceptor;

	private ISessionEvent sessionEvent;

	private ISessionBuilder sessionBuilder;
	// 一些定义好的错误消息.
	private IClientErrorMessage errorMessage;

	private TcpBootstrapParams(){}

	public ISessionEvent getSessionEvent() {
		return sessionEvent;
	}

	public TcpInterceptor getInterceptor() {
		return interceptor;
	}

	public IClientErrorMessage getErrorMessage() {
		return errorMessage;
	}

	public ISessionBuilder getSessionBuilder() {
		return sessionBuilder;
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
		private ISessionBuilder sessionBuilder = new DefaultSessionBuilder();

		private IClientErrorMessage errorMessage;

		private ISessionEvent sessionEvent;

		private TcpInterceptor interceptor;

		private Builder(){}

		public Builder setSessionEvent(ISessionEvent sessionEvent) {
			this.sessionEvent = sessionEvent;
			return this;
		}

		public Builder setSessionBuilder(ISessionBuilder sessionBuilder) {
			this.sessionBuilder = sessionBuilder;
			return this;
		}

		public Builder setInterceptor(TcpInterceptor interceptor) {
			this.interceptor = interceptor;
			return this;
		}

		public Builder setErrorMessage(IClientErrorMessage errorMessage) {
			this.errorMessage = errorMessage;
			return this;
		}

		@Override
		protected TcpBootstrapParams newParams() {
			return new TcpBootstrapParams();
		}

		@Override
		protected void buildInner(TcpBootstrapParams tcpBootstrapParams) {
			if (interceptor == null) throw new NullPointerException("Interceptor can not be Null");
			tcpBootstrapParams.sessionBuilder = this.sessionBuilder;
			tcpBootstrapParams.errorMessage = this.errorMessage;
			tcpBootstrapParams.sessionEvent = this.sessionEvent;
			tcpBootstrapParams.interceptor = this.interceptor;
		}
	}
}
