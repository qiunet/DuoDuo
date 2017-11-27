package org.qiunet.flash.handler.context.session;

import io.netty.channel.ChannelHandlerContext;

/**
 * Created by qiunet.
 * 17/11/27
 */
public class DefaultSessionBuilder implements ISessionBuilder {

	@Override
	public <T extends ISession> T createSession(ChannelHandlerContext ctx) {
		return (T) new DefaultPlayerSession(ctx);
	}
}
