package org.qiunet.flash.handler.startup.context;

import org.qiunet.flash.handler.context.request.data.pb.IpbResponseData;
import org.qiunet.flash.handler.context.response.push.IChannelMessage;
import org.qiunet.flash.handler.context.session.DSession;
import org.qiunet.flash.handler.netty.server.param.adapter.IStartupContext;

/***
 *
 * @author qiunet
 * 2020-04-12 16:32
 **/
public class StartupContext implements IStartupContext<PlayerActor> {
	@Override
	public IChannelMessage<IpbResponseData> getHandlerNotFound() {
		return null;
	}

	@Override
	public IChannelMessage<IpbResponseData> exception(Throwable cause) {
		return null;
	}


	@Override
	public PlayerActor buildPlayerActor(DSession session) {
		return new PlayerActor(session);
	}
}
