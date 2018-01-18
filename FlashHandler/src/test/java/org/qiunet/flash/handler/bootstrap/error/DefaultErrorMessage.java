package org.qiunet.flash.handler.bootstrap.error;

import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.response.push.DefaultStringMessage;
import org.qiunet.flash.handler.context.response.push.IMessage;
import org.qiunet.flash.handler.netty.server.tcp.error.IClientErrorMessage;

/**
 * Created by qiunet.
 * 17/11/26
 */
public class DefaultErrorMessage implements IClientErrorMessage{


	@Override
	public IMessage getHandlerNotFound() {
		return null;
	}
}
