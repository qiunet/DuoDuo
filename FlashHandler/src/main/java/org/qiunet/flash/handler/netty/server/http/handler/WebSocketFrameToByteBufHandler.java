package org.qiunet.flash.handler.netty.server.http.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.qiunet.utils.logger.LoggerType;

/**
 * Created by qiunet.
 * 2019-03-23 20:59
 */
public class WebSocketFrameToByteBufHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof BinaryWebSocketFrame) {
			ctx.fireChannelRead(((WebSocketFrame) msg).content());
		} else if(msg instanceof PingWebSocketFrame) {
			ctx.writeAndFlush(new PongWebSocketFrame(((PingWebSocketFrame) msg).content().retain()));
		} else {
			LoggerType.DUODUO_FLASH_HANDLER.error("Do not support msg type [{}]", msg.getClass());
		}
	}
}
