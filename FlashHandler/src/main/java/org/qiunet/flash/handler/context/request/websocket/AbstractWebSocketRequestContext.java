package org.qiunet.flash.handler.context.request.websocket;


import io.netty.channel.Channel;
import io.netty.handler.codec.http.HttpHeaders;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.common.player.IMessageActor;
import org.qiunet.flash.handler.context.request.BaseRequestContext;

/**
 * Created by qiunet.
 * 17/12/2
 */
abstract class AbstractWebSocketRequestContext<RequestData, P extends IMessageActor>  extends BaseRequestContext<RequestData> implements IWebSocketRequestContext<RequestData, P>{
	protected HttpHeaders headers;
	protected P messageActor;

	protected AbstractWebSocketRequestContext(MessageContent content, Channel channel, P messageActor, HttpHeaders headers) {
		super(content, channel);
		this.headers = headers;
		this.messageActor = messageActor;
	}

	@Override
	public Channel channel() {
		return channel;
	}

	@Override
	public String getRemoteAddress() {
		return getRealIp(this.headers);
	}
}
