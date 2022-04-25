package org.qiunet.flash.handler.netty.server.param;

/**
 * 使用引导类 参数.
 * 建造者模式
 * Created by qiunet.
 * 17/7/19
 */
public final class TcpBootstrapParams extends AbstractBootstrapParam {
	/**
	 * 放开udp
	 */
	private boolean udpOpen;

	private TcpBootstrapParams(){}

	/***
	 * 得到
	 * @return
	 */
	public static Builder custom(){
		TcpBootstrapParams params = new TcpBootstrapParams();
		return params.new Builder();
	}

	public boolean isUdpOpen() {
		return udpOpen;
	}

	/***
	 * 使用build模式 set和 get 分离. 以后有有顺序的构造时候也可以不动
	 */
	public class Builder extends SuperBuilder<TcpBootstrapParams, Builder> {

		private Builder(){}

		@Override
		protected TcpBootstrapParams newParams() {
			if (protocolHeaderType == null) {
				throw new NullPointerException("Must set IProtocolHeaderType for Listener!");
			}

			return TcpBootstrapParams.this;
		}

		/**
		 * 设置 udp open
		 * @return
		 */
		public Builder setUdpOpen() {
			TcpBootstrapParams.this.udpOpen = true;
			return this;
		}
	}
}
