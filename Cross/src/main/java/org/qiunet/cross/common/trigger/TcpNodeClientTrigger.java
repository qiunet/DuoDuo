package org.qiunet.cross.common.trigger;

import org.qiunet.flash.handler.common.IMessage;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.common.player.IMessageActor;
import org.qiunet.flash.handler.context.session.DSession;
import org.qiunet.flash.handler.handler.IHandler;
import org.qiunet.flash.handler.handler.mapping.RequestHandlerMapping;
import org.qiunet.flash.handler.netty.client.trigger.ILongConnResponseTrigger;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;

/***
 *
 *
 * @author qiunet
 * 2020-10-23 17:44
 */
public class TcpNodeClientTrigger implements ILongConnResponseTrigger {
	@Override
	public void response(DSession session, MessageContent data) {
		IMessageActor iMessageActor = session.getAttachObj(ServerConstants.MESSAGE_ACTOR_KEY);
		IHandler handler = RequestHandlerMapping.getInstance().getHandler(data);
		IMessage message = handler.getHandlerType().createRequestContext(data, session.channel(), handler, iMessageActor);
		iMessageActor.addMessage(message);
	}
}
