package org.qiunet.flash.handler.netty.client.param;

import org.qiunet.flash.handler.context.header.DefaultProtocolHeader;
import org.qiunet.flash.handler.context.header.IProtocolHeader;

import java.net.InetSocketAddress;

/**
 * Created by qiunet.
 * 17/11/22
 */
public abstract class AbstractClientConfig implements IClientConfig {
	/***
	 * 地址
	 */
	protected InetSocketAddress address;

	protected boolean encryption = true;

	protected int maxReceivedLength = 1024 * 1024 * 8;

	protected IProtocolHeader protocolHeader = DefaultProtocolHeader.instance;

	@Override
	public IProtocolHeader getProtocolHeader() {
		return protocolHeader;
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
	public abstract class SuperBuilder<P extends AbstractClientConfig, B extends SuperBuilder> {
		public B setAddress(InetSocketAddress address) {
			AbstractClientConfig.this.address = address;
			return (B) this;
		}
		public B setAddress(String host, int port) {
			return setAddress(new InetSocketAddress(host, port));
		}
		/**
		 * 设置
		 * @param protocolHeader
		 * @return
		 */
		public B setProtocolHeader(IProtocolHeader protocolHeader) {
			AbstractClientConfig.this.protocolHeader = protocolHeader;
			return (B) this;
		}

		public B setMaxReceivedLength(int maxReceivedLength) {
			AbstractClientConfig.this.maxReceivedLength = maxReceivedLength;
			return (B) this;
		}

		public B setEncryption(boolean encryption) {
			AbstractClientConfig.this.encryption = encryption;
			return (B) this;
		}

		/**
		 * 构造build
		 * @return
		 */
		public P build(){
			return newConfig();
		}

		/**
		 * 得到一个新的param
		 * @return
		 */
		protected abstract P newConfig();
	}
}
