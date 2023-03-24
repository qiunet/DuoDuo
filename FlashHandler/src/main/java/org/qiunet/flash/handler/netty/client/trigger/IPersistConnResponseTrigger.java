package org.qiunet.flash.handler.netty.client.trigger;

import io.netty.channel.Channel;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.session.ISession;

/**
 * Created by qiunet.
 * 17/11/25
 */
public interface IPersistConnResponseTrigger {
	/***
	 * 触发的响应
	 * @param data
	 */
	void response(ISession session, Channel channel, MessageContent data);
}
