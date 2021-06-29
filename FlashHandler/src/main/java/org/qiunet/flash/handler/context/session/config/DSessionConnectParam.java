package org.qiunet.flash.handler.context.session.config;

import com.google.common.base.Preconditions;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.GenericFutureListener;
import org.qiunet.utils.string.StringUtil;

/***
 * DSession 连接参数
 *
 * qiunet
 * 2021/6/29 08:23
 **/
public class DSessionConnectParam {
	/**
	 * 连接后的监听
	 */
	private final GenericFutureListener<ChannelFuture> connectListener;
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
		this.connectListener = builder.connectListener;
		this.bootstrap = builder.bootstrap;
		this.host = builder.host;
		this.port = builder.port;
	}

	public Bootstrap getBootstrap() {
		return bootstrap;
	}

	public GenericFutureListener<ChannelFuture> getConnectListener() {
		return connectListener;
	}

	public int getPort() {
		return port;
	}

	public String getHost() {
		return host;
	}

	/**
	 * 构造一个builder
	 * 参数传入必传参数 , 后面还有其它参数. 就可以自己set了.
	 * @return
	 */
	public static DSessionConnectParamBuilder newBuilder(Bootstrap bootstrap, String host, int port){
		return new DSessionConnectParamBuilder(bootstrap, host, port);
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
		/**
		 * 连接后的监听
		 */
		private GenericFutureListener<ChannelFuture> connectListener;
		private DSessionConnectParamBuilder(Bootstrap bootstrap, String host, int port) {
			this.bootstrap = bootstrap;
			this.host = host;
			this.port = port;
		}

		public DSessionConnectParamBuilder setConnectListener(GenericFutureListener<ChannelFuture> connectListener) {
			this.connectListener = connectListener;
			return this;
		}

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
			Preconditions.checkArgument(! StringUtil.isEmpty(host), "ConnectParam.host is empty");
			Preconditions.checkArgument(port > 0, "ConnectParam.port is empty");
			Preconditions.checkNotNull(bootstrap, "ConnectParam.bootstrap is null");
			return new DSessionConnectParam(this);
		}
	}
}
