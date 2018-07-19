package org.qiunet.flash.handler.netty.server.param;

import org.qiunet.flash.handler.netty.server.interceptor.UdpInterceptor;

/***
 *
 * @Author qiunet
 * @Date Create in 2018/7/18 16:12
 **/
public class UdpBootstrapParam extends AbstractBootstrapParam {

	private UdpInterceptor udpInterceptor;

	private UdpBootstrapParam(){}

	public void setUdpInterceptor(UdpInterceptor udpInterceptor) {
		this.udpInterceptor = udpInterceptor;
	}

	/***
	 * 得到
	 * @return
	 */
	public static UdpBootstrapParam.Builder custom(){
		return new UdpBootstrapParam.Builder();
	}

	/***
	 * 使用build模式 set和 get 分离. 以后有有顺序的构造时候也可以不动
	 */
	public static class Builder extends SuperBuilder<UdpBootstrapParam, UdpBootstrapParam.Builder> {

		private UdpInterceptor udpInterceptor;

		private Builder(){}


		public UdpBootstrapParam.Builder setUdpInterceptor(UdpInterceptor interceptor) {
			this.udpInterceptor = interceptor;
			return this;
		}

		@Override
		protected UdpBootstrapParam newParams() {
			return new UdpBootstrapParam();
		}

		@Override
		protected void buildInner(UdpBootstrapParam udpBootstrapParams) {
			if (udpInterceptor == null) throw new NullPointerException("udpInterceptor can not be Null");
			if (errorMessage == null) throw new NullPointerException("IClientErrorMessage can not be Null");
			udpBootstrapParams.udpInterceptor = this.udpInterceptor;
		}
	}
}
