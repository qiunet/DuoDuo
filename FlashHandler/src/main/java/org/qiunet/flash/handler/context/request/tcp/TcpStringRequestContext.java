package org.qiunet.flash.handler.context.request.tcp;

import io.netty.channel.ChannelHandlerContext;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.response.push.DefaultStringMessage;
import org.qiunet.flash.handler.context.response.push.IResponseMessage;
import org.qiunet.flash.handler.handler.tcp.ITcpHandler;
import org.qiunet.flash.handler.netty.server.param.TcpBootstrapParams;
import org.qiunet.utils.string.StringUtil;

/**
 * tcp 请求解析成字符串的.
 * Created by qiunet.
 * 17/11/21
 */
public class TcpStringRequestContext extends AbstractTcpRequestContext<String, String> {
	protected String requestData;
	public TcpStringRequestContext(MessageContent content, ChannelHandlerContext channelContext, TcpBootstrapParams params) {
		super(content, channelContext, params);
	}

	@Override
	protected IResponseMessage getResponseMessage(int protocolId, String s) {
		return new DefaultStringMessage(protocolId, s);
	}

	@Override
	public String getRequestData() {
		if (StringUtil.isEmpty(requestData)) {
			this.requestData = getHandler().parseRequestData(messageContent.bytes());
		}
		return requestData;
	}

	@Override
	public void handlerRequest() {
		FacadeTcpRequest<String> facadeTcpRequest = new FacadeTcpRequest<>(this);
		params.getTcpInterceptor().handler((ITcpHandler) getHandler(), facadeTcpRequest);
	}
}
