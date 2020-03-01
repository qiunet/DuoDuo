package org.qiunet.flash.handler.context.request.tcp;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.ChannelHandlerContext;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.response.push.DefaultProtobufMessage;
import org.qiunet.flash.handler.context.response.push.IResponseMessage;
import org.qiunet.flash.handler.handler.tcp.ITcpHandler;
import org.qiunet.flash.handler.netty.server.param.TcpBootstrapParams;

/**
 * Created by qiunet.
 * 17/11/21
 */
public class TcpProtobufRequestContext<RequestData> extends AbstractTcpRequestContext<RequestData, GeneratedMessageV3> {
	private RequestData requestData;
	public TcpProtobufRequestContext(MessageContent content, ChannelHandlerContext channelContext, TcpBootstrapParams params) {
		super(content, channelContext, params);
	}

	@Override
	public RequestData getRequestData() {
		if (requestData != null) return requestData;
		this.requestData = getHandler().parseRequestData(messageContent.bytes());
		return requestData;
	}

	@Override
	protected IResponseMessage getResponseMessage(int protocolId, GeneratedMessageV3 generatedMessageV3) {
		return new DefaultProtobufMessage(protocolId, generatedMessageV3);
	}

	@Override
	public void handlerRequest() {
		FacadeTcpRequest<RequestData> facadeTcpRequest = new FacadeTcpRequest<>(this);
		params.getTcpInterceptor().handler((ITcpHandler) getHandler(), facadeTcpRequest);
	}
}
