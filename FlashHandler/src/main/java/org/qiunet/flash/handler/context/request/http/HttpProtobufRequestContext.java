package org.qiunet.flash.handler.context.request.http;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.handler.http.IHttpHandler;
import org.qiunet.flash.handler.netty.server.param.HttpBootstrapParams;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by qiunet.
 * 17/11/21
 */
public  class HttpProtobufRequestContext<RequestData extends GeneratedMessageV3, ResponseData  extends GeneratedMessageV3> extends AbstractHttpRequestContext<RequestData, ResponseData> {
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
		return  responseData.toByteArray();
	}

	@Override
	public RequestData getRequestData() {
		if (requestData != null) return requestData;
		this.requestData = getHandler().parseRequestData(messageContent.bytes());
		return requestData;
	}

	@Override
	public boolean handler() {
		FacadeHttpRequest<RequestData> request = new FacadeHttpRequest<>(this);
		ResponseData data = (ResponseData) params.getHttpInterceptor().handler((IHttpHandler)getHandler(), request);
		this.response(data);
		return true;
	}

	@Override
	public int getQueueIndex() {
		return 0;
	}

	@Override
	public String toStr() {
		return null;
	}
}
