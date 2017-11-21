package org.qiunet.flash.handler.context.request.tcp;

import io.netty.channel.ChannelHandlerContext;
import org.qiunet.flash.handler.context.request.BaseRequest;
import org.qiunet.flash.handler.context.header.MessageContent;
import org.qiunet.flash.handler.handler.response.IResponse;

/**
 * tcp udp的上下文
 * Created by qiunet.
 * 17/7/19
 */
public abstract class AbstractTcpRequest<RequestData> extends BaseRequest<RequestData> implements ITcpRequest<RequestData>, IResponse {
	protected AbstractTcpRequest(MessageContent content, ChannelHandlerContext channelContext) {
		super(content, channelContext);
	}
	@Override
	public int getQueueHandlerIndex() {
		return ctx.channel().id().asLongText().hashCode();
	}
}
