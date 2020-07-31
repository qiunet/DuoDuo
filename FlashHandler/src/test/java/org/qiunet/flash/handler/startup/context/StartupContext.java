package org.qiunet.flash.handler.startup.context;

import io.netty.channel.Channel;
import org.qiunet.flash.handler.context.response.push.IResponseMessage;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.flash.handler.netty.server.param.adapter.IStartupContext;

/***
 *
 * @author qiunet
 * 2020-04-12 16:32
 **/
public class StartupContext implements IStartupContext<GameSession, PlayerActor> {
	@Override
	public IResponseMessage getHandlerNotFound() {
		return null;
	}

	@Override
	public IResponseMessage exception(Throwable cause) {
		return null;
	}

	@Override
	public GameSession buildSession(Channel channel) {
		return new GameSession(channel);
	}

	@Override
	public PlayerActor buildPlayerActor(ISession session) {
		return new PlayerActor((GameSession) session);
	}
}
