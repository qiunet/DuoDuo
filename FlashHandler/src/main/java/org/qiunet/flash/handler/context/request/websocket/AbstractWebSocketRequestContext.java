package org.qiunet.flash.handler.context.request.websocket;


import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.request.BaseRequestContext;
import org.qiunet.flash.handler.context.response.IResponse;
import org.qiunet.flash.handler.context.response.push.IMessage;
import org.qiunet.flash.handler.context.response.push.ResponseMsgUtil;
import org.qiunet.flash.handler.context.session.SessionManager;
import org.qiunet.flash.handler.netty.server.param.HttpBootstrapParams;

import java.net.InetSocketAddress;

/**
 * Created by qiunet.
 * 17/12/2
 */
abstract class AbstractWebSocketRequestContext<RequestData, ResponseData>  extends BaseRequestContext<RequestData> implements IWebSocketRequestContext<RequestData>, IResponse {
	protected HttpBootstrapParams params;
	protected AbstractWebSocketRequestContext(MessageContent content, ChannelHandlerContext ctx,HttpBootstrapParams params) {
		super(content, ctx);
		this.params = params;
	}

	@Override
	public int getQueueIndex() {
		return SessionManager.getInstance().getSession(ctx.channel()).getQueueIndex();
	}

	@Override
	public void response(int protocolId, Object data) {
		ResponseMsgUtil.responseWebsocketMessage(ctx.channel(), getResponseMessage(protocolId, (ResponseData) data));
	}

	@Override
	public Channel channel() {
		return ctx.channel();
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
}
