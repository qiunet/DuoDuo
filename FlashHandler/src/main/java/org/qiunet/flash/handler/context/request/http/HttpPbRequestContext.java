package org.qiunet.flash.handler.context.request.http;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.HttpRequest;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.context.response.push.DefaultProtobufMessage;
import org.qiunet.flash.handler.netty.server.param.HttpBootstrapParams;

/**
 * Created by qiunet.
 * 17/11/21
 */
public  class HttpPbRequestContext<RequestData extends IChannelData, ResponseData  extends IChannelData> extends AbstractHttpRequestContext<RequestData, ResponseData> {
	public HttpPbRequestContext(MessageContent content, Channel channel, HttpBootstrapParams params, HttpRequest request) {
		this.init(content, channel, params, request);
	}

	public void init(MessageContent content, Channel channel, HttpBootstrapParams params, HttpRequest request) {
		super.init(content, channel, params, request);
	}

	@Override
	protected String contentType() {
		return "application/octet-stream";
	}

	@Override
	protected DefaultProtobufMessage getResponseDataMessage(ResponseData responseData) {
		return  responseData.buildChannelMessage();
	}
}
