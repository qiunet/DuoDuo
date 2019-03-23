package org.qiunet.flash.handler.netty.server.http.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

/**
 * Created by qiunet.
 * 2019-03-23 20:59
 */
public class WebSocketFrameToByteBufHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ctx.fireChannelRead(((WebSocketFrame) msg).content());
	}
}
