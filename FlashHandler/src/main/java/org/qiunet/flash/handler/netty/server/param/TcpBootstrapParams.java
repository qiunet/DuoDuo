package org.qiunet.flash.handler.netty.server.param;

import java.net.InetSocketAddress;

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
	private KcpBootstrapParams.KcpParam kcpParam;

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
		return kcpParam != null;
	}

	/**
	 * 转为kcp 参数
	 * @return
	 */
	public KcpBootstrapParams toKcpBootstrapParams() {
		if (! isUdpOpen()) {
			throw new IllegalArgumentException("Can not use to kcp!");
		}
		return KcpBootstrapParams.custom()
				.setPort(((InetSocketAddress) this.getAddress()).getPort())
				.setReadIdleCheckSeconds(this.readIdleCheckSeconds)
				.setProtocolHeaderType(this.protocolHeaderType)
				.setMaxReceivedLength(this.maxReceivedLength)
				.setStartupContext(this.startupContext)
				.setEncryption(this.encryption)
				.setServerName(this.serverName)
				.setDependOnTcpWs()
				.build();
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
			TcpBootstrapParams.this.kcpParam = KcpBootstrapParams.KcpParam.DEFAULT_KCP_PARAM;
			return this;
		}
		/**
		 * 设置 udp open
		 * @return
		 */
		public Builder setUdpOpen(int snd_wnd, int rcv_wnd, int interval, boolean noDelay, int mtu, int fastResend, boolean noCwnd) {
			TcpBootstrapParams.this.kcpParam = new KcpBootstrapParams.KcpParam(snd_wnd, rcv_wnd, interval, noDelay, mtu, fastResend, noCwnd);
			return this;
		}
	}
}
