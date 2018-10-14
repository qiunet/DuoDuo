package org.qiunet.flash.handler.netty.server.param;

import org.qiunet.flash.handler.context.session.DefaultSessionEvent;
import org.qiunet.flash.handler.context.session.ISessionEvent;
import org.qiunet.flash.handler.netty.server.interceptor.UdpInterceptor;

/***
 *
 * @Author qiunet
 * @Date Create in 2018/7/18 16:12
 **/
public class UdpBootstrapParams extends AbstractBootstrapParam {

	private UdpInterceptor udpInterceptor;

	private ISessionEvent sessionEvent;

	private UdpBootstrapParams(){}

	public ISessionEvent getSessionEvent() {
		return sessionEvent;
	}

	public UdpInterceptor getUdpInterceptor() {
		return udpInterceptor;
	}

	/***
	 * 得到
	 * @return
	 */
	public static UdpBootstrapParams.Builder custom(){
		return new UdpBootstrapParams.Builder();
	}

	/***
	 * 使用build模式 set和 get 分离. 以后有有顺序的构造时候也可以不动
	 */
	public static class Builder extends SuperBuilder<UdpBootstrapParams, UdpBootstrapParams.Builder> {

		private UdpInterceptor udpInterceptor;

		private ISessionEvent sessionEvent = new DefaultSessionEvent();

		private Builder(){}

		public UdpBootstrapParams.Builder setSessionEvent(ISessionEvent sessionEvent) {
			this.sessionEvent = sessionEvent;
			return this;
		}

		public UdpBootstrapParams.Builder setUdpInterceptor(UdpInterceptor interceptor) {
			this.udpInterceptor = interceptor;
			return this;
		}

		@Override
		protected UdpBootstrapParams newParams() {
			return new UdpBootstrapParams();
		}

		@Override
		protected void buildInner(UdpBootstrapParams udpBootstrapParams) {
			if (udpInterceptor == null) throw new NullPointerException("udpInterceptor can not be Null");
			if (errorMessage == null) throw new NullPointerException("IClientErrorMessage can not be Null");
			udpBootstrapParams.udpInterceptor = this.udpInterceptor;
			udpBootstrapParams.sessionEvent = this.sessionEvent;
		}
	}
}
