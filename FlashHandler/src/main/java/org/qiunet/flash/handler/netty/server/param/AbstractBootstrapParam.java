package org.qiunet.flash.handler.netty.server.param;

import org.qiunet.flash.handler.context.header.ContextAdapter;
import org.qiunet.flash.handler.context.header.DefaultContextAdapter;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * Created by qiunet.
 * 17/11/22
 */
public abstract class AbstractBootstrapParam {
	/***
	 * 接收端口
	 */
	protected SocketAddress address;

	protected ContextAdapter adapter;
	/**
	 * 是否检验crc
	 * 一般测试时候使用
	 */
	protected boolean crc;

	public SocketAddress getAddress() {
		return address;
	}

	public ContextAdapter getAdapter() {
		return adapter;
	}

	public boolean isCrc() {
		return crc;
	}

	/***
	 * 使用build模式 set和 get 分离. 以后有有顺序的构造时候也可以不动
	 * */
	public abstract static class SuperBuilder<P extends AbstractBootstrapParam, B extends SuperBuilder> {
		protected SocketAddress address;

		protected ContextAdapter adapter = new DefaultContextAdapter();

		protected boolean crc = true;

		public B setCrc(boolean crc) {
			this.crc = crc;
			return (B) this;
		}
		public B setPort(int port) {
			this.address = new InetSocketAddress(port);
			return (B)this;
		}

		public B setAdapter(ContextAdapter adapter) {
			this.adapter = adapter;
			return (B)this;
		}
		/**
		 * 构造build
		 * @return
		 */
		public P build(){
			if (address == null) throw new NullPointerException("Must set port for Http Listener! ");
			P p = newParams();
			p.address = address;
			p.adapter = adapter;
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
