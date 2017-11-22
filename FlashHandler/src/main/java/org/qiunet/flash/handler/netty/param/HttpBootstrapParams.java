package org.qiunet.flash.handler.netty.param;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * 使用引导类 参数.
 * 建造者模式
 * Created by qiunet.
 * 17/7/19
 */
public final class HttpBootstrapParams {
	/***
	 * 接收端口
 	 */
	private SocketAddress Address;
	/**
	 * 使用ssl 模式
	 */
	private boolean ssl;

	private HttpBootstrapParams(){}

	public boolean isSsl() {
		return ssl;
	}
	public SocketAddress getAddress() {
		return Address;
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
	public static class Builder {
		private Builder(){}
		private boolean ssl;
		private SocketAddress address;

		public Builder setPort(int port) {
			this.address = new InetSocketAddress(port);
			return this;
		}

		public Builder setSsl(boolean ssl) {
			this.ssl = ssl;
			return this;
		}

		public HttpBootstrapParams build(){
			if (address == null) throw new NullPointerException("Must set port for Http Listener! ");
			HttpBootstrapParams params = new HttpBootstrapParams();
			params.Address = this.address;
			params.ssl = this.ssl;
			return params;
		}
	}
}
