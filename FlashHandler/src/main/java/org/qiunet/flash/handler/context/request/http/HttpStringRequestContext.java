package org.qiunet.flash.handler.context.request.http;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.CharsetUtil;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.netty.server.param.HttpBootstrapParams;

/**
 * 把请求解析为string的对象
 * Created by qiunet.
 * 17/11/21
 */
public class HttpStringRequestContext extends AbstractHttpRequestContext<String, String> {

	public HttpStringRequestContext(MessageContent content, Channel channel, HttpBootstrapParams params, HttpRequest request) {
		super(content, channel, params, request);
	}

	@Override
	protected byte[] getResponseDataBytes(String s) {
		return s.getBytes(CharsetUtil.UTF_8);
	}

	@Override
	public void handlerRequest() {
		FacadeHttpRequest<String, String> requestData = new FacadeHttpRequest<>(this);
		String responseData = null;
		try {
			responseData = getHandler().handler(requestData);
		} catch (Exception e) {
			logger.error("HttpStringRequestContext Exception", e);
		}

		if (responseData == null){
			throw new NullPointerException("Response String can not be null!");
		}

		this.response(responseData);
	}

	@Override
	protected String contentType() {
		return "text/plain; charset=UTF-8";
	}

}
