package org.qiunet.flash.handler.netty.client.param;

import org.qiunet.flash.handler.context.header.DefaultProtocolHeaderAdapter;
import org.qiunet.flash.handler.netty.server.param.adapter.IProtocolHeaderAdapter;

import java.net.InetSocketAddress;

/**
 * Created by qiunet.
 * 17/11/22
 */
public abstract class AbstractClientParam implements IClientConfig {
	/***
	 * 地址
	 */
	protected InetSocketAddress address;

	protected boolean encryption;

	protected int maxReceivedLength;

	protected IProtocolHeaderAdapter protocolHeaderAdapter;
	@Override
	public IProtocolHeaderAdapter getProtocolHeaderAdapter() {
		return protocolHeaderAdapter;
	}

	@Override
	public InetSocketAddress getAddress() {
		return address;
	}

	@Override
	public boolean isEncryption() {
		return encryption;
	}

	@Override
	public int getMaxReceivedLength() {
		return maxReceivedLength;
	}

	/***
	 * 使用build模式 set和 get 分离. 以后有有顺序的构造时候也可以不动
	 * */
	public abstract static class SuperBuilder<P extends AbstractClientParam, B extends SuperBuilder> {
		protected IProtocolHeaderAdapter startupContextAdapter = new DefaultProtocolHeaderAdapter();
		protected int maxReceivedLength = 1024 * 1024 * 8;
		protected InetSocketAddress address;
		protected boolean encryption = true;

		public B setAddress(InetSocketAddress address) {
			this.address = address;
			return (B) this;
		}
		public B setAddress(String host, int port) {
			return setAddress(new InetSocketAddress(host, port));
		}

		/**
		 * 设置启动需要的一些东西上下文
		 * @param startupContextAdapter
		 * @return
		 */
		public B setStartupContextAdapter(IProtocolHeaderAdapter startupContextAdapter) {
			this.startupContextAdapter = startupContextAdapter;
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

		/**
		 * 构造build
		 * @return
		 */
		public P build(){
			if (address == null) throw new NullPointerException("Must set port for Http Listener! ");

			P p = newParams();
			p.protocolHeaderAdapter = startupContextAdapter;
			p.maxReceivedLength = maxReceivedLength;
			p.address = address;
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
