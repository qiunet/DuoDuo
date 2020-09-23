package org.qiunet.flash.handler.context.request.http;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.qiunet.flash.handler.common.annotation.SkipDebugOut;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.request.data.pb.IpbRequestData;
import org.qiunet.flash.handler.context.request.data.pb.IpbResponseData;
import org.qiunet.flash.handler.netty.server.param.HttpBootstrapParams;

/**
 * Created by qiunet.
 * 17/11/21
 */
public  class HttpProtobufRequestContext<RequestData extends IpbRequestData, ResponseData  extends IpbResponseData> extends AbstractHttpRequestContext<RequestData, ResponseData> {
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
	public void handlerRequest() {
		FacadeHttpRequest<RequestData, GeneratedMessageV3> request = new FacadeHttpRequest<>(this);
		ResponseData data = null;
		try {
			if (logger.isInfoEnabled() && ! getHandler().getClass().isAnnotationPresent(SkipDebugOut.class)) {
				logger.info(" <<< {}", ToStringBuilder.reflectionToString(getRequestData(), ToStringStyle.SHORT_PREFIX_STYLE));
			}
			data = getHandler().handler(request);
		} catch (Exception e) {
			logger.error("HttpProtobufRequestContext Exception: ", e);
		}

		if (data == null) {
			throw new NullPointerException("Response Protobuf data can not be null!");
		}
		if (logger.isInfoEnabled() && ! data.getClass().isAnnotationPresent(SkipDebugOut.class)) {
			logger.info(">>> {}", ToStringBuilder.reflectionToString(data, ToStringStyle.SHORT_PREFIX_STYLE));
		}
		this.response(data);
	}
}
