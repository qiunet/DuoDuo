package org.qiunet.flash.handler.netty.client.param;

import org.qiunet.flash.handler.common.enums.ServerConnType;
import org.qiunet.flash.handler.context.header.IProtocolHeaderType;

import java.net.InetSocketAddress;

public interface IClientConfig {
	/***
	 * 获得处理Header对象
	 * @return
	 */
	IProtocolHeaderType getProtocolHeaderType();
	/***
	 * 地址
	 * @return
	 */
	InetSocketAddress getAddress();
	/***
	 * 最大的长度
	 * @return
	 */
	int getMaxReceivedLength();
	/***
	 * 是否加密
	 * @return
	 */
	boolean isEncryption();
	/***
	 * 得到处理类型
	 * @return
	 */
	ServerConnType getConnType();
}
