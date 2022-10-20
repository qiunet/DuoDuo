package org.qiunet.flash.handler.netty.client.param;

import org.qiunet.flash.handler.common.enums.ServerConnType;
import org.qiunet.flash.handler.context.header.DefaultProtocolHeader;
import org.qiunet.flash.handler.context.header.IProtocolHeader;

import java.net.InetSocketAddress;

public interface IClientConfig {
	/***
	 * 获得处理Header对象
	 * @return
	 */
	default IProtocolHeader getProtocolHeader() {
		return DefaultProtocolHeader.instance;
	}
	/***
	 * 地址
	 * @return
	 */
	InetSocketAddress getAddress();
	/***
	 * 最大的长度
	 * @return
	 */
	default int getMaxReceivedLength() {
		return 1024 * 1024 * 8;
	}
	/***
	 * 是否加密
	 * @return
	 */
	default boolean isEncryption() {
		return true;
	}
	/***
	 * 得到处理类型
	 * @return
	 */
	ServerConnType getConnType();
}
