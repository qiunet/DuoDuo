package org.qiunet.flash.handler.context.request.http;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import org.qiunet.flash.handler.context.header.MessageContent;
import org.qiunet.flash.handler.netty.server.param.HttpBootstrapParams;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by qiunet.
 * 17/11/21
 */
public  class HttpProtobufRequestContext<RequestData, ResponseData> extends AbstractHttpRequestContext<RequestData, ResponseData> {
	private RequestData requestData;
	public HttpProtobufRequestContext(MessageContent content, ChannelHandlerContext channelContext, HttpBootstrapParams params, HttpRequest request) {
		super(content, channelContext, params, request);
	}

	@Override
	protected String contentType() {
		return "application/octet-stream";
	}

	@Override
	protected byte[] getResponseDataBytes(ResponseData responseData) {
		return ((GeneratedMessageV3) responseData).toByteArray();
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
	public boolean handler() {

		return true;
	}

	@Override
	public String toStr() {
		return null;
	}
}
