package org.qiunet.flash.handler.context.response.push;

import org.qiunet.flash.handler.common.message.MessageContent;

/**
 * 对外的编码
 * Created by qiunet.
 * 17/12/11
 */
public interface IMessage<T> {
	/***
	 * push 消息 编码
	 * @return
	 */
	MessageContent encode();

	/***
	 * 得到消息的内容
	 * @return
	 */
	T getContent();
	/**
	 * 得到消息id
	 * @return
	 */
	int getProtocolID();

	/**
	 * 转logger 格式字符串
	 * @return
	 */
	String toStr();
}
