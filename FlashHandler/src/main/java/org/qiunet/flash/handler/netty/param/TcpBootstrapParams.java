package org.qiunet.flash.handler.netty.param;

import java.net.SocketAddress;

/**
 * 使用引导类 参数.
 * 建造者模式
 * Created by qiunet.
 * 17/7/19
 */
public final class TcpBootstrapParams {
	/***
	 * 接收端口
 	 */
	private SocketAddress Address;

	private TcpBootstrapParams(){}

	public SocketAddress getAddress() {
		return Address;
	}
	/***
	 * 得到
	 * @return
	 */
	public static Build custom(){
		return new Build();
	}

	/***
	 * 使用build模式 set和 get 分离. 以后有有顺序的构造时候也可以不动
	 */
	public static class Build {
		 private Build(){}

		 private SocketAddress address;

		 public Build setAddress(SocketAddress address) {
		 	this.address = address;
		 	return this;
		 }

		 public TcpBootstrapParams build(){
			TcpBootstrapParams params = new TcpBootstrapParams();
			params.Address = this.address;
		 	return params;
		 }
	}
}
