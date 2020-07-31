package org.qiunet.tests.server.startup.context;

import io.netty.channel.Channel;
import org.qiunet.flash.handler.context.session.AbstractSession;

/***
 *
 * @author qiunet
 * 2020-04-12 16:28
 **/
public class GameSession extends AbstractSession<PlayerActor> {


	public GameSession(Channel channel) {
		super(channel);
	}
}
