package org.qiunet.flash.handler.netty.server.param;

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

	/**
	 * 是否检验crc
	 * 一般测试时候使用
	 */
	protected boolean crc;

	protected int maxReceivedLength;

	public IClientErrorMessage getErrorMessage() {
		return errorMessage;
	}

	public int getMaxReceivedLength() {
		return maxReceivedLength;
	}

	public SocketAddress getAddress() {
		return address;
	}

	public boolean isCrc() {
		return crc;
	}

	/***
	 * 使用build模式 set和 get 分离. 以后有有顺序的构造时候也可以不动
	 * */
	public abstract static class SuperBuilder<P extends AbstractBootstrapParam, B extends SuperBuilder> {
		protected SocketAddress address;
		// 最大上行1M的长度(HTTP 同样有满足)
		protected int maxReceivedLength = 1024 * 1024;

		protected IClientErrorMessage errorMessage;

		protected boolean crc = true;

		public B setErrorMessage(IClientErrorMessage errorMessage) {
			this.errorMessage = errorMessage;
			return (B) this;
		}

		public B setMaxReceivedLength(int maxReceivedLength) {
			this.maxReceivedLength = maxReceivedLength;
			return (B) this;
		}

		public B setCrc(boolean crc) {
			this.crc = crc;
			return (B) this;
		}
		public B setPort(int port) {
			this.address = new InetSocketAddress(port);
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
			p.address = address;
			p.crc = crc;
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
