package org.qiunet.test.handler.startup.context;

import org.qiunet.flash.handler.common.player.PlayerActor;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.flash.handler.netty.server.param.adapter.IStartupContext;

/***
 *
 * @author qiunet
 * 2020-04-12 16:32
 **/
public class StartupContext implements IStartupContext<PlayerActor> {
	@Override
	public PlayerActor buildMessageActor(ISession session) {
		return new PlayerActor(session);
	}
}
