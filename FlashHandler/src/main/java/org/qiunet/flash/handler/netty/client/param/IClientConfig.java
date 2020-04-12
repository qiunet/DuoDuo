package org.qiunet.flash.handler.netty.client.param;

import org.qiunet.flash.handler.common.enums.HandlerType;
import org.qiunet.flash.handler.netty.server.param.adapter.IProtocolHeaderAdapter;

import java.net.InetSocketAddress;

public interface IClientConfig {
	/***
	 * 获得处理Header对象
	 * @return
	 */
	IProtocolHeaderAdapter getProtocolHeaderAdapter();
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
	HandlerType getHandlerType();
}
