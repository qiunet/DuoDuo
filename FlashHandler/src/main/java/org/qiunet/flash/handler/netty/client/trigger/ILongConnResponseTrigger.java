package org.qiunet.flash.handler.netty.client.trigger;

import org.qiunet.flash.handler.common.message.MessageContent;

/**
 * Created by qiunet.
 * 17/11/25
 */
public interface ILongConnResponseTrigger {
	/***
	 * 触发的响应
	 * @param data
	 */
	public void response(MessageContent data);
}
