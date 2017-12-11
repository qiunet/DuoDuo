package org.qiunet.flash.handler.context.request.tcp;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.qiunet.flash.handler.context.request.BaseRequestContext;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.response.IResponse;
import org.qiunet.flash.handler.context.response.push.IMessage;
import org.qiunet.flash.handler.context.response.push.ResponseMsgUtil;
import org.qiunet.flash.handler.netty.server.param.TcpBootstrapParams;

import java.net.InetSocketAddress;

/**
 * tcp udp的上下文
 * Created by qiunet.
 * 17/7/19
 */
public abstract class AbstractTcpRequestContext<RequestData, ResponseData> extends BaseRequestContext<RequestData> implements ITcpRequestContext<RequestData>, IResponse {
	protected TcpBootstrapParams params;
	protected AbstractTcpRequestContext(MessageContent content, ChannelHandlerContext channelContext,TcpBootstrapParams params) {
		super(content, channelContext);
		this.params = params;
	}
	@Override
	public int getQueueHandlerIndex() {
		return ctx.channel().id().asLongText().hashCode();
	}

	@Override
	public void response(int protocolId, Object data) {
		ResponseMsgUtil.responseTcpMessage(ctx.channel(), getResponseMessage(protocolId, (ResponseData) data));
	}
	/***
	 * 得到responseData的数组数据
	 * @param responseData
	 * @return
	 */
	protected abstract IMessage getResponseMessage(int protocolId, ResponseData responseData);

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
