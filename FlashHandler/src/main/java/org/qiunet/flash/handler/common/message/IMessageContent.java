package org.qiunet.flash.handler.common.message;

/***
 *
 * 请求 响应协议内容类
 * protocolId 可能是数字 可能是string
 *
 * @author qiunet
 * 2020-04-13 07:57
 **/
public interface IMessageContent<ProtocolId> {
	/**
	 * 协议ID
	 * @return
	 */
	ProtocolId getProtocolId();

	/**
	 * 内容
	 * @return
	 */
	byte [] bytes();
}
