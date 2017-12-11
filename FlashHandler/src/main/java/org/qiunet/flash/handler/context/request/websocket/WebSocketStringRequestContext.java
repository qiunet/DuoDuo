package org.qiunet.flash.handler.context.request.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.handler.websocket.IWebSocketHandler;
import org.qiunet.flash.handler.netty.server.param.HttpBootstrapParams;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by qiunet.
 * 17/12/2
 */
public class WebSocketStringRequestContext extends AbstractWebSocketRequestContext<String, String> {
	private String requestData;
	public WebSocketStringRequestContext(MessageContent content, ChannelHandlerContext ctx, HttpBootstrapParams params) {
		super(content, ctx, params);
	}
	@Override
	public String getRequestData() {
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
	protected byte[] getResponseDataBytes(String data) {
		return data.getBytes(CharsetUtil.UTF_8);
	}

	@Override
	public boolean handler() {
		FacadeWebSocketRequest<String> facadeWebSocketRequest = new FacadeWebSocketRequest(this);
		params.getWebSocketInterceptor().handler((IWebSocketHandler) getHandler(), facadeWebSocketRequest);
		return true;
	}

	@Override
	public String toStr() {
		return null;
	}
}
