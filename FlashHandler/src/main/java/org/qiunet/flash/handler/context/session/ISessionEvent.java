package org.qiunet.flash.handler.context.session;

import io.netty.channel.ChannelHandlerContext;

/**
 * Created by qiunet.
 * 17/11/27
 */
public interface ISessionEvent {
	/***
	 * session 激活
	 * @param ctx
	 */
	public void sessionRegistered(ChannelHandlerContext ctx);


	/***
	 * session 失活
	 * @param ctx
	 */
	public void sessionUnregistered(ChannelHandlerContext ctx);
}
