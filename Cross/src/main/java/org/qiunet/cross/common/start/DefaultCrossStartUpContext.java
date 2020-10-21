package org.qiunet.cross.common.start;

import org.qiunet.cross.actor.CrossPlayerActor;
import org.qiunet.flash.handler.context.session.DSession;
import org.qiunet.flash.handler.netty.server.param.adapter.IStartupContext;

/**
 *
 * @author qiunet
 * 2020-10-21 18:16
 */
public class DefaultCrossStartUpContext implements IStartupContext<CrossPlayerActor> {

	@Override
	public CrossPlayerActor buildMessageActor(DSession session) {
		return new CrossPlayerActor(session);
	}
}
