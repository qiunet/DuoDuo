package org.qiunet.flash.handler.netty.client.param;

import java.net.InetSocketAddress;

/**
 * 使用引导类 参数.
 * 建造者模式
 * Created by qiunet.
 * 17/7/19
 */
public final class TcpClientParams extends AbstractClientParam {
	/***
	 * 接收端口
	 */
	protected InetSocketAddress address;

	private TcpClientParams(){}

	public InetSocketAddress getAddress() {
		return address;
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
	public static class Builder extends SuperBuilder<TcpClientParams, Builder> {
		protected InetSocketAddress address;

		public TcpClientParams.Builder setAddress(InetSocketAddress address) {
			this.address = address;
			return this;
		}
		public TcpClientParams.Builder setAddress(String host, int port) {
			return setAddress(new InetSocketAddress(host, port));
		}

		@Override
		protected TcpClientParams newParams() {
			return new TcpClientParams();
		}

		@Override
		protected void buildInner(TcpClientParams tcpClientParams) {
			if (address == null) throw new NullPointerException("Must set port for Http Listener! ");

			tcpClientParams.address = address;
		}
	}
}
