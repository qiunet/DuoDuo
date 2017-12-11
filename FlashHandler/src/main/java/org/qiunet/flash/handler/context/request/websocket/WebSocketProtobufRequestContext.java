package org.qiunet.flash.handler.context.request.websocket;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.ChannelHandlerContext;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.response.push.DefaultProtobufMessage;
import org.qiunet.flash.handler.context.response.push.IMessage;
import org.qiunet.flash.handler.handler.websocket.IWebSocketHandler;
import org.qiunet.flash.handler.netty.server.param.HttpBootstrapParams;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by qiunet.
 * 17/12/2
 */
public class WebSocketProtobufRequestContext<RequestData> extends AbstractWebSocketRequestContext<RequestData, GeneratedMessageV3> {
	private RequestData requestData;
	public WebSocketProtobufRequestContext(MessageContent content, ChannelHandlerContext ctx, HttpBootstrapParams params) {
		super(content, ctx, params);
	}
	@Override
	public RequestData getRequestData() {
		if (requestData != null) return requestData;
		try {
			this.requestData = getHandler().parseRequestData(bytes);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return requestData;
	}

	@Override
	protected IMessage getResponseMessage(int protocolId, GeneratedMessageV3 generatedMessageV3) {
		return new DefaultProtobufMessage(protocolId, generatedMessageV3);
	}

	@Override
	public boolean handler() {
		FacadeWebSocketRequest<RequestData> facadeWebSocketRequest = new FacadeWebSocketRequest(this);
		params.getWebSocketInterceptor().handler((IWebSocketHandler) getHandler(), facadeWebSocketRequest);
		return true;
	}

	@Override
	public String toStr() {
		return null;
	}
}
