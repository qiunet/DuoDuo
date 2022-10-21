package org.qiunet.flash.handler.netty.server.http.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.*;
import org.qiunet.flash.handler.netty.server.constants.ServerConstants;
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
		} else if (msg instanceof CloseWebSocketFrame frame) {
			WebSocketServerHandshaker handshaker = ctx.channel().attr(ServerConstants.HANDSHAKER_ATTR_KEY).get();
			if (handshaker != null) {
				handshaker.close(ctx.channel(), frame.retain());
			} else {
				ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
			}
		}else {
			LoggerType.DUODUO_FLASH_HANDLER.error("Do not support msg type [{}]", msg.getClass());
		}
	}
}
