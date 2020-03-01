package org.qiunet.flash.handler.context.sender;

import org.qiunet.flash.handler.context.response.push.IResponseMessage;

/***
 * 所有发送响应的都实现该接口
 * @author qiunet
 * 2020/3/1 20:53
 **/
public interface IMessageSender {
	/***
	 * 发送消息.
	 * @param message
	 */
	void send(IResponseMessage message);
}
