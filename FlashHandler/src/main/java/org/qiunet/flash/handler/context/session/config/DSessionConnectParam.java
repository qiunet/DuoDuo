package org.qiunet.flash.handler.context.session.config;

import com.google.common.base.Supplier;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.GenericFutureListener;

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

	private final Supplier<ChannelFuture> connector;

	private DSessionConnectParam(DSessionConnectParamBuilder builder) {
		this.connectListener = builder.connectListener;
		this.connector = builder.connector;
	}

	/**
	 * 连接服务器
	 * @return
	 */
	public ChannelFuture connect() {
		ChannelFuture channelFuture = connector.get();
		if (connectListener != null) {
			channelFuture.addListener(connectListener);
		}
		return channelFuture;
	}

	/**
	 * 构造一个builder
	 * 参数传入必传参数 , 后面还有其它参数. 就可以自己set了.
	 * @return
	 */
	public static DSessionConnectParamBuilder newBuilder(Supplier<ChannelFuture> connector){
		return new DSessionConnectParamBuilder(connector);
	}

	public static class DSessionConnectParamBuilder {
		/**
		 * 连接
		 */
		private final Supplier<ChannelFuture> connector;
		/**
		 * 连接后的监听
		 */
		private GenericFutureListener<ChannelFuture> connectListener;
		private DSessionConnectParamBuilder(Supplier<ChannelFuture> connector) {
			this.connector = connector;
		}

		public DSessionConnectParamBuilder setConnectListener(GenericFutureListener<ChannelFuture> connectListener) {
			this.connectListener = connectListener;
			return this;
		}

		public DSessionConnectParam build(){
			return new DSessionConnectParam(this);
		}
	}
}
