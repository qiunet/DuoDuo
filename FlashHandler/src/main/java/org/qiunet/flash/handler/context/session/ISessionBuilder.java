package org.qiunet.flash.handler.context.session;

import io.netty.channel.ChannelHandlerContext;

/**
 * Created by qiunet.
 * 17/11/26
 */
public interface ISessionBuilder {
	/***
	 * 创建一个Session
	 * @param ctx
	 * @param <T>
	 * @return
	 */
	public <T extends ISession> T createSession(ChannelHandlerContext ctx);
}
