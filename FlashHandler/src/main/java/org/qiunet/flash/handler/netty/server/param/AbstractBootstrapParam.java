package org.qiunet.flash.handler.netty.server.param;

import org.qiunet.flash.handler.context.IStartupContextAdapter;
import org.qiunet.flash.handler.netty.server.tcp.error.IClientErrorMessage;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * Created by qiunet.
 * 17/11/22
 */
public abstract class AbstractBootstrapParam {
	// 一些定义好的错误消息.
	protected IClientErrorMessage errorMessage;
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

	protected int maxReceivedLength;

	protected IStartupContextAdapter protocolHeaderAdapter;

	public IStartupContextAdapter getProtocolHeaderAdapter() {
		return protocolHeaderAdapter;
	}

	public IClientErrorMessage getErrorMessage() {
		return errorMessage;
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

	/***
	 * 使用build模式 set和 get 分离. 以后有有顺序的构造时候也可以不动
	 * */
	public abstract static class SuperBuilder<P extends AbstractBootstrapParam, B extends SuperBuilder> {
		protected SocketAddress address;
		// 最大上行1M的长度(HTTP 同样有满足)
		protected int maxReceivedLength = 1024 * 1024;

		protected IClientErrorMessage errorMessage;

		protected boolean encryption = true;

		protected IStartupContextAdapter startupContextAdapter;
		/***
		 * 读超时处理.默认300秒 (单位秒)
		 */
		private int readIdleCheckSeconds = 300;

		/***
		 * 启动需要的上下文对象
		 * @param startupContextAdapter
		 * @return
		 */
		public B setStartupContextAdapter(IStartupContextAdapter startupContextAdapter) {
			this.startupContextAdapter = startupContextAdapter;
			return (B) this;
		}

		public B setErrorMessage(IClientErrorMessage errorMessage) {
			this.errorMessage = errorMessage;
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
			p.errorMessage = errorMessage;
			p.readIdleCheckSeconds = readIdleCheckSeconds;
			p.address = address;
			p.protocolHeaderAdapter = startupContextAdapter;
			p.encryption = encryption;
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
