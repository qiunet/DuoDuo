package org.qiunet.flash.handler.bootstrap.error;

import org.qiunet.flash.handler.context.header.MessageContent;
import org.qiunet.flash.handler.netty.server.tcp.error.IClientErrorMessage;

/**
 * Created by qiunet.
 * 17/11/26
 */
public class DefaultErrorMessage implements IClientErrorMessage{


	@Override
	public MessageContent getHandlerNotFound() {


		return new MessageContent(404, new byte[]{});
	}
}
