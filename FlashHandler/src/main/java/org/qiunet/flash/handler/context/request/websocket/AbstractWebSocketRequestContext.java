package org.qiunet.flash.handler.context.request.websocket;


import io.netty.channel.Channel;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.common.player.IMessageActor;
import org.qiunet.flash.handler.context.request.BaseRequestContext;
import org.qiunet.flash.handler.util.ChannelUtil;

/**
 * Created by qiunet.
 * 17/12/2
 */
abstract class AbstractWebSocketRequestContext<RequestData, P extends IMessageActor>  extends BaseRequestContext<RequestData> implements IWebSocketRequestContext<RequestData, P>{
	protected P messageActor;

	protected AbstractWebSocketRequestContext(MessageContent content, Channel channel, P messageActor) {
		super(content, channel);
		this.messageActor = messageActor;
	}

	@Override
	public Channel channel() {
		return channel;
	}

	@Override
	public String getRemoteAddress() {
		return ChannelUtil.getIp(channel);
	}
}
