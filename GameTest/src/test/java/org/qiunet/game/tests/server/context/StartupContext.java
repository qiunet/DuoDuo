package org.qiunet.game.tests.server.context;

import org.qiunet.flash.handler.context.session.DSession;
import org.qiunet.flash.handler.netty.server.param.adapter.IStartupContext;

/***
 *
 * @author qiunet
 * 2020-04-12 16:32
 **/
public class StartupContext implements IStartupContext<PlayerActor> {

	@Override
	public PlayerActor buildMessageActor(DSession session) {
		return new PlayerActor(session);
	}
}
