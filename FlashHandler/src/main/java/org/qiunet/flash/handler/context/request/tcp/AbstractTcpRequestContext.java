package org.qiunet.flash.handler.context.request.tcp;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.common.player.IPlayerActor;
import org.qiunet.flash.handler.context.request.BaseRequestContext;
import org.qiunet.flash.handler.context.response.push.IResponseMessage;

import java.net.InetSocketAddress;

/**
 * tcp udp的上下文
 * Created by qiunet.
 * 17/7/19
 */
abstract class AbstractTcpRequestContext<RequestData, ResponseData, P extends IPlayerActor> extends BaseRequestContext<RequestData> implements ITcpRequestContext<RequestData, P> {
	protected P playerActor;
	protected AbstractTcpRequestContext(MessageContent content, ChannelHandlerContext channelContext,P playerActor) {
		super(content, channelContext);
		this.playerActor = playerActor;
	}

	/***
	 * 得到responseData的数组数据
	 * @param responseData
	 * @return
	 */
	protected abstract IResponseMessage getResponseMessage(int protocolId, ResponseData responseData);

	@Override
	public String getRemoteAddress() {
		String ip = "";
		if (ctx.channel().remoteAddress() != null && ctx.channel().remoteAddress() instanceof InetSocketAddress) {
			ip = ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress();
		}
		return ip;
	}

	@Override
	public Channel channel() {
		return ctx.channel();
	}
}
