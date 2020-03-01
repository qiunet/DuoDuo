package org.qiunet.flash.handler.context.request.websocket;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpHeaders;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.response.push.DefaultProtobufMessage;
import org.qiunet.flash.handler.context.response.push.IResponseMessage;
import org.qiunet.flash.handler.handler.websocket.IWebSocketHandler;
import org.qiunet.flash.handler.netty.server.param.HttpBootstrapParams;

/**
 * Created by qiunet.
 * 17/12/2
 */
public class WebSocketProtobufRequestContext<RequestData> extends AbstractWebSocketRequestContext<RequestData, GeneratedMessageV3> {
	private RequestData requestData;
	public WebSocketProtobufRequestContext(MessageContent content, ChannelHandlerContext ctx, HttpBootstrapParams params, HttpHeaders headers) {
		super(content, ctx, params, headers);
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
		FacadeWebSocketRequest<RequestData> facadeWebSocketRequest = new FacadeWebSocketRequest(this);
		params.getWebSocketInterceptor().handler((IWebSocketHandler) getHandler(), facadeWebSocketRequest);
	}
}
