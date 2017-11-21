package org.qiunet.flash.handler.context.request.http;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import org.qiunet.flash.handler.context.header.MessageContent;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by qiunet.
 * 17/11/21
 */
public  class HttpProtobufRequest<RequestData, ResponseData> extends AbstractHttpRequest<RequestData, ResponseData> {
	private RequestData requestData;
	public HttpProtobufRequest(MessageContent content, ChannelHandlerContext channelContext, HttpRequest request) {
		super(content, channelContext, request);
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
		return false;
	}

	@Override
	public String toStr() {
		return null;
	}
}
