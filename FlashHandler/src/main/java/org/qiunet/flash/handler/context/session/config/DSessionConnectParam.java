package org.qiunet.flash.handler.context.session.config;

import io.netty.bootstrap.Bootstrap;

/***
 * DSession 连接参数
 *
 * qiunet
 * 2021/6/29 08:23
 **/
public class DSessionConnectParam {
	/**
	 * 启动netty参数
	 */
	private final Bootstrap bootstrap;
	/**
	 * 主机地址
	 */
	private final String host;
	/**
	 * 端口
	 */
	private final int port;

	private DSessionConnectParam(DSessionConnectParamBuilder builder) {
		this.bootstrap = builder.bootstrap;
		this.host = builder.host;
		this.port = builder.port;
	}

	public Bootstrap getBootstrap() {
		return bootstrap;
	}

	public int getPort() {
		return port;
	}

	public String getHost() {
		return host;
	}

	public static DSessionConnectParamBuilder newBuilder(){
		return new DSessionConnectParamBuilder();
	}

	public static class DSessionConnectParamBuilder {
		/**
		 * 启动netty参数
		 */
		private Bootstrap bootstrap;
		/**
		 * 主机地址
		 */
		private String host;
		/**
		 * 端口
		 */
		private int port;


		public DSessionConnectParamBuilder setBootstrap(Bootstrap bootstrap) {
			this.bootstrap = bootstrap;
			return this;
		}

		public DSessionConnectParamBuilder setHost(String host) {
			this.host = host;
			return this;
		}

		public DSessionConnectParamBuilder setPort(int port) {
			this.port = port;
			return this;
		}

		public DSessionConnectParam build(){
			return new DSessionConnectParam(this);
		}
	}
}
