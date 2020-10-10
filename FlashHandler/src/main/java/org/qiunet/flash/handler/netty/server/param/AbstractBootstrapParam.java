package org.qiunet.flash.handler.netty.server.param;

import org.qiunet.flash.handler.common.player.IMessageActor;
import org.qiunet.flash.handler.context.header.DefaultProtocolHeaderAdapter;
import org.qiunet.flash.handler.netty.server.param.adapter.IProtocolHeaderAdapter;
import org.qiunet.flash.handler.netty.server.param.adapter.IStartupContext;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * Created by qiunet.
 * 17/11/22
 */
public abstract class AbstractBootstrapParam {
	/**
	 * 可以自定义协议头
	 */
	protected IProtocolHeaderAdapter protocolHeaderAdapter;
	/***
	 * 接收端口
	 */
	protected SocketAddress address;
	/***
	 * 读超时处理.默认300秒 (单位秒)
	 */
	protected int readIdleCheckSeconds;
	/**
	 * 是否检验crc
	 * 一般测试时候使用
	 */
	protected boolean encryption;
	/**
	 * 最大接受的数据长度
	 */
	protected int maxReceivedLength;

	protected IStartupContext<? extends IMessageActor> startupContext;

	public IProtocolHeaderAdapter getProtocolHeaderAdapter() {
		return protocolHeaderAdapter;
	}

	public int getMaxReceivedLength() {
		return maxReceivedLength;
	}

	public SocketAddress getAddress() {
		return address;
	}

	public boolean isEncryption() {
		return encryption;
	}

	public int getReadIdleCheckSeconds() {
		return readIdleCheckSeconds;
	}

	public IStartupContext<? extends IMessageActor> getStartupContext() {
		return startupContext;
	}

	/***
	 * 使用build模式 set和 get 分离. 以后有有顺序的构造时候也可以不动
	 * */
	public abstract static class SuperBuilder<P extends AbstractBootstrapParam, B extends SuperBuilder> {
		protected SocketAddress address;
		// 最大上行1M的长度(HTTP 同样有满足)
		protected int maxReceivedLength = 1024 * 1024;

		protected boolean encryption = true;

		protected IProtocolHeaderAdapter protocolHeaderAdapter = new DefaultProtocolHeaderAdapter();
		/***
		 * 读超时处理.默认300秒 (单位秒)
		 */
		private int readIdleCheckSeconds = 300;

		protected IStartupContext<? extends IMessageActor> startupContext;
		/***
		 * 启动需要的上下文对象
		 * @param protocolHeaderAdapter
		 * @return
		 */
		public B setProtocolHeaderAdapter(IProtocolHeaderAdapter protocolHeaderAdapter) {
			this.protocolHeaderAdapter = protocolHeaderAdapter;
			return (B) this;
		}

		public B setStartupContext(IStartupContext<? extends IMessageActor> startupContext) {
			this.startupContext = startupContext;
			return (B) this;
		}

		public B setMaxReceivedLength(int maxReceivedLength) {
			this.maxReceivedLength = maxReceivedLength;
			return (B) this;
		}

		public B setEncryption(boolean encryption) {
			this.encryption = encryption;
			return (B) this;
		}
		public B setPort(int port) {
			this.address = new InetSocketAddress(port);
			return (B)this;
		}

		public B setReadIdleCheckSeconds(int readIdleCheckSeconds) {
			this.readIdleCheckSeconds = readIdleCheckSeconds;
			return (B)this;
		}

		/**
		 * 构造build
		 * @return
		 */
		public P build(){
			if (address == null) throw new NullPointerException("Must set port for Http Listener! ");
			P p = newParams();
			p.maxReceivedLength = maxReceivedLength;
			p.readIdleCheckSeconds = readIdleCheckSeconds;
			p.protocolHeaderAdapter = protocolHeaderAdapter;
			p.startupContext = startupContext;
			p.encryption = encryption;
			p.address = address;
			this.buildInner(p);
			return p;
		}

		/**
		 * 得到一个新的param
		 * @return
		 */
		protected abstract P newParams();
		/**
		 * 隐藏的构造
		 * @return
		 */
		protected abstract void buildInner(P p);
	}
}
