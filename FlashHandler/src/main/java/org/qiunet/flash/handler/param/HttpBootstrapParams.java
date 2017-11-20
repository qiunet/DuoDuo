package org.qiunet.flash.handler.param;

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
	/**
	 * 解析成字符串就行
	 */
	private boolean string;
	/**
	 * 解析成json
	 */
	private boolean json;

	private HttpBootstrapParams(){}

	public boolean isJson(){
		return json;
	}

	public boolean isString() {
		return string;
	}

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
	private static class Builder {
		private Builder(){}
		private boolean json;
		private boolean string;
		private boolean ssl;
		private SocketAddress address;

		public Builder setJson(boolean json) {
			this.json = json;
			return this;
		}

		public Builder setString(boolean string) {
			this.string = string;
			return this;
		}

		public Builder setAddress(SocketAddress address) {
			this.address = address;
			return this;
		}

		public Builder setSsl(boolean ssl) {
			this.ssl = ssl;
			return this;
		}

		public HttpBootstrapParams build(){
			HttpBootstrapParams params = new HttpBootstrapParams();
			params.Address = this.address;
			params.ssl = this.ssl;
			params.json = this.json;
			params.string = string;
			return params;
		}
	}
}
