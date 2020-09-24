package org.qiunet.tests.server.startup.context;

import org.qiunet.flash.handler.context.response.push.IResponseMessage;
import org.qiunet.flash.handler.context.session.DSession;
import org.qiunet.flash.handler.netty.server.param.adapter.IStartupContext;

/***
 *
 * @author qiunet
 * 2020-04-12 16:32
 **/
public class StartupContext implements IStartupContext<PlayerActor> {
	@Override
	public IResponseMessage getHandlerNotFound() {
		return null;
	}

	@Override
	public IResponseMessage exception(Throwable cause) {
		return null;
	}


	@Override
	public PlayerActor buildPlayerActor(DSession session) {
		return new PlayerActor(session);
	}
}
