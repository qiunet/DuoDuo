package org.qiunet.flash.handler.context.request.persistconn;


import io.netty.channel.Channel;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.common.player.IMessageActor;
import org.qiunet.flash.handler.context.request.BaseRequestContext;
import org.qiunet.flash.handler.util.ChannelUtil;

/**
 * Created by qiunet.
 * 17/12/2
 */
abstract class AbstractPersistConnRequestContext<RequestData, P extends IMessageActor<P>>
		extends BaseRequestContext<RequestData>
		implements IPersistConnRequestContext<RequestData, P> {

	protected void init(MessageContent content, Channel channel) {
		super.init(content, channel);
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
