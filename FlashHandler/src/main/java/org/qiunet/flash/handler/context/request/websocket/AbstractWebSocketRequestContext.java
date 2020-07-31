package org.qiunet.flash.handler.context.request.websocket;


import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpHeaders;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.common.player.IPlayerActor;
import org.qiunet.flash.handler.context.request.BaseRequestContext;

/**
 * Created by qiunet.
 * 17/12/2
 */
abstract class AbstractWebSocketRequestContext<RequestData, P extends IPlayerActor>  extends BaseRequestContext<RequestData> implements IWebSocketRequestContext<RequestData, P>{
	protected HttpHeaders headers;
	protected P playerActor;

	protected AbstractWebSocketRequestContext(MessageContent content, ChannelHandlerContext ctx,P playerActor, HttpHeaders headers) {
		super(content, ctx);
		this.headers = headers;
		this.playerActor = playerActor;
	}

	@Override
	public Channel channel() {
		return ctx.channel();
	}

	@Override
	public String getRemoteAddress() {
		return getRealIp(this.headers);
	}
}
