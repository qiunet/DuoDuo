package org.qiunet.cross.common.context;

import org.qiunet.cross.common.message.HandlerNotFoundResponse;
import org.qiunet.cross.common.message.ServerExceptionResponse;
import org.qiunet.cross.node.ServerNode;
import org.qiunet.flash.handler.context.response.push.DefaultProtobufMessage;
import org.qiunet.flash.handler.context.session.DSession;
import org.qiunet.flash.handler.netty.server.param.adapter.IStartupContext;

/***
 *
 *
 * @author qiunet
 * 2020-10-10 15:15
 */
public class ServerStartupContext implements IStartupContext<ServerNode> {

	@Override
	public ServerNode buildMessageActor(DSession session) {
		return new ServerNode(session);
	}

	@Override
	public DefaultProtobufMessage getHandlerNotFound() {
		return new HandlerNotFoundResponse().buildResponseMessage();
	}

	@Override
	public DefaultProtobufMessage exception(Throwable cause) {
		return new ServerExceptionResponse().buildResponseMessage();
	}
}
