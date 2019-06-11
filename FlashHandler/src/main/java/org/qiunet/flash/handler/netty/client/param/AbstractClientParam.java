package org.qiunet.flash.handler.netty.client.param;

import org.qiunet.flash.handler.context.header.DefaultProtocolHeaderAdapter;
import org.qiunet.flash.handler.context.header.IProtocolHeaderAdapter;

import java.net.InetSocketAddress;

/**
 * Created by qiunet.
 * 17/11/22
 */
public abstract class AbstractClientParam implements IClientConfig {
	protected boolean encryption;

	protected int maxReceivedLength;

	protected IProtocolHeaderAdapter protocolHeaderAdapter;
	@Override
	public IProtocolHeaderAdapter getProtocolHeaderAdapter() {
		return protocolHeaderAdapter;
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

		protected IProtocolHeaderAdapter protocolHeaderAdapter = new DefaultProtocolHeaderAdapter();

		protected int maxReceivedLength = 1024 * 1024 * 8;

		public B setProtocolHeaderAdapter(IProtocolHeaderAdapter protocolHeaderAdapter) {
			this.protocolHeaderAdapter = protocolHeaderAdapter;
			return (B) this;
		}

		public B setMaxReceivedLength(int maxReceivedLength) {
			this.maxReceivedLength = maxReceivedLength;
			return (B) this;
		}

		/**
		 * 构造build
		 * @return
		 */
		public P build(){
			P p = newParams();
			p.protocolHeaderAdapter = protocolHeaderAdapter;
			p.maxReceivedLength = maxReceivedLength;
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
