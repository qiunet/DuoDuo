package org.qiunet.flash.handler.context.request.tcp;

import io.netty.channel.ChannelHandlerContext;
import org.qiunet.flash.handler.context.request.BaseRequestContext;
import org.qiunet.flash.handler.context.header.MessageContent;
import org.qiunet.flash.handler.context.response.IResponse;

import java.net.InetSocketAddress;

/**
 * tcp udp的上下文
 * Created by qiunet.
 * 17/7/19
 */
public abstract class AbstractTcpRequestContext<RequestData, ResponseData> extends BaseRequestContext<RequestData> implements ITcpRequestContext<RequestData>, IResponse<ResponseData> {
	protected AbstractTcpRequestContext(MessageContent content, ChannelHandlerContext channelContext) {
		super(content, channelContext);
	}
	@Override
	public int getQueueHandlerIndex() {
		return ctx.channel().id().asLongText().hashCode();
	}

	@Override
	public String getRemoteAddress() {
		String ip = "";
		if (ctx.channel().remoteAddress() != null && ctx.channel().remoteAddress() instanceof InetSocketAddress) {
			ip = ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress();
		}
		return ip;
	}
}
