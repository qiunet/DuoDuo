package org.qiunet.cross.common.start;

import org.qiunet.cross.node.ServerNode;
import org.qiunet.flash.handler.context.session.DSession;
import org.qiunet.flash.handler.netty.server.param.adapter.IStartupContext;

/**
 *
 * @author qiunet
 * 2020-10-21 18:16
 */
public class DefaultCommunicationStartUpContext implements IStartupContext<ServerNode> {

	@Override
	public ServerNode buildMessageActor(DSession session) {
		return new ServerNode(session);
	}
}
