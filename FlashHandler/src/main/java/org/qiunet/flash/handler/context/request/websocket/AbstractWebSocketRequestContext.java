package org.qiunet.flash.handler.context.request.websocket;


import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpHeaders;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.request.BaseRequestContext;
import org.qiunet.flash.handler.context.response.IResponse;
import org.qiunet.flash.handler.context.response.push.IResponseMessage;
import org.qiunet.flash.handler.netty.server.param.HttpBootstrapParams;

/**
 * Created by qiunet.
 * 17/12/2
 */
abstract class AbstractWebSocketRequestContext<RequestData, ResponseData>  extends BaseRequestContext<RequestData> implements IWebSocketRequestContext<RequestData>, IResponse {
	protected HttpBootstrapParams params;
	protected HttpHeaders headers;

	protected AbstractWebSocketRequestContext(MessageContent content, ChannelHandlerContext ctx,HttpBootstrapParams params, HttpHeaders headers) {
		super(content, ctx);
		this.headers = headers;
		this.params = params;
	}


	@Override
	public void response(int protocolId, Object data) {
		channel().writeAndFlush(getResponseMessage(protocolId, (ResponseData) data).encode());
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
	protected abstract IResponseMessage getResponseMessage(int protocolId, ResponseData responseData);
	@Override
	public String getRemoteAddress() {
		return getRealIp(this.headers);
	}
}
