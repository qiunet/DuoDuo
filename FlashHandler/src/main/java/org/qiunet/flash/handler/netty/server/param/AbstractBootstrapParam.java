package org.qiunet.flash.handler.netty.server.param;

import org.qiunet.flash.handler.context.header.ContextAdapter;
import org.qiunet.flash.handler.context.header.DefaultContextAdapter;
import org.qiunet.flash.handler.context.session.DefaultSessionBuilder;
import org.qiunet.flash.handler.context.session.DefaultSessionEvent;
import org.qiunet.flash.handler.context.session.ISessionBuilder;
import org.qiunet.flash.handler.context.session.ISessionEvent;
import org.qiunet.flash.handler.netty.server.tcp.error.IClientErrorMessage;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * Created by qiunet.
 * 17/11/22
 */
public abstract class AbstractBootstrapParam {

	protected ISessionEvent sessionEvent;

	protected ISessionBuilder sessionBuilder;
	// 一些定义好的错误消息.
	protected IClientErrorMessage errorMessage;
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

	protected int maxReceivedLength;

	public ISessionEvent getSessionEvent() {
		return sessionEvent;
	}

	public IClientErrorMessage getErrorMessage() {
		return errorMessage;
	}

	public ISessionBuilder getSessionBuilder() {
		return sessionBuilder;
	}

	public int getMaxReceivedLength() {
		return maxReceivedLength;
	}

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
		// 最大上行1M的长度(HTTP 同样有满足)
		protected int maxReceivedLength = 1024 * 1024;

		protected ContextAdapter adapter = new DefaultContextAdapter();

		protected ISessionBuilder sessionBuilder = new DefaultSessionBuilder();

		protected ISessionEvent sessionEvent = new DefaultSessionEvent();

		protected IClientErrorMessage errorMessage;

		protected boolean crc = true;

		public B setSessionEvent(ISessionEvent sessionEvent) {
			this.sessionEvent = sessionEvent;
			return (B) this;
		}
		public B setErrorMessage(IClientErrorMessage errorMessage) {
			this.errorMessage = errorMessage;
			return (B) this;
		}

		public B setSessionBuilder(ISessionBuilder sessionBuilder) {
			this.sessionBuilder = sessionBuilder;
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
			p.maxReceivedLength = maxReceivedLength;
			p.sessionBuilder = sessionBuilder;
			p.errorMessage = errorMessage;
			p.sessionEvent = sessionEvent;
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
