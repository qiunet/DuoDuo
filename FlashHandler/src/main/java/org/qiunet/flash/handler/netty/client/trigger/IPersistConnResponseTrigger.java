package org.qiunet.flash.handler.netty.client.trigger;

import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.session.DSession;

/**
 * Created by qiunet.
 * 17/11/25
 */
public interface IPersistConnResponseTrigger {
	/***
	 * 触发的响应
	 * @param data
	 */
	void response(DSession session, MessageContent data);
}
