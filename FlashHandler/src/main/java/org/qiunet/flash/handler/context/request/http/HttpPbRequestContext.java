package org.qiunet.flash.handler.context.request.http;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.HttpRequest;
import org.qiunet.flash.handler.common.annotation.SkipDebugOut;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.netty.server.param.HttpBootstrapParams;
import org.qiunet.utils.string.ToString;

/**
 * Created by qiunet.
 * 17/11/21
 */
public  class HttpPbRequestContext<RequestData extends IChannelData, ResponseData  extends IChannelData> extends AbstractHttpRequestContext<RequestData, ResponseData> {
	public HttpPbRequestContext(MessageContent content, Channel channel, HttpBootstrapParams params, HttpRequest request) {
		super(content, channel, params, request);
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
	public void handlerRequest() throws Exception {
		FacadeHttpRequest<RequestData, GeneratedMessageV3> request = new FacadeHttpRequest<>(this);
		ResponseData data = null;
		try {
			if (logger.isInfoEnabled() && ! getHandler().getClass().isAnnotationPresent(SkipDebugOut.class)) {
				logger.info("HTTP <<< {}", ToString.toString(getRequestData()));
			}
			data = getHandler().handler(request);
		} catch (Exception e) {
			logger.error("HttpProtobufRequestContext Exception: ", e);
		}

		if (data == null) {
			throw new NullPointerException("Response Protobuf data can not be null!");
		}
		if (logger.isInfoEnabled() && ! data.getClass().isAnnotationPresent(SkipDebugOut.class)) {
			logger.info("HTTP >>> {}", ToString.toString(data));
		}
		this.response(data);
	}
}
