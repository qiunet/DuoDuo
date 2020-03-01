package org.qiunet.flash.handler.context.request.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.CharsetUtil;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.handler.http.IHttpHandler;
import org.qiunet.flash.handler.netty.server.param.HttpBootstrapParams;

/**
 * 把请求解析为string的对象
 * Created by qiunet.
 * 17/11/21
 */
public class HttpStringRequestContext extends AbstractHttpRequestContext<String, String> {
	private String reqeustData;

	public HttpStringRequestContext(MessageContent content, ChannelHandlerContext channelContext, HttpBootstrapParams params, HttpRequest request) {
		super(content, channelContext, params, request);
	}

	@Override
	public String getRequestData() {
		if (reqeustData == null) {
			reqeustData = getHandler().parseRequestData(messageContent.bytes());
		}
		return reqeustData;
	}
	@Override
	protected byte[] getResponseDataBytes(String s) {
		return s.getBytes(CharsetUtil.UTF_8);
	}

	@Override
	public void handlerRequest() {
		FacadeHttpRequest<String> requestData = new FacadeHttpRequest<>(this);
		String responseData = (String) params.getHttpInterceptor().handler((IHttpHandler) getHandler(), requestData);
		if (requestData == null){
			throw new NullPointerException("Response String can not be null!");
		}
		this.response(responseData);
	}

	@Override
	protected String contentType() {
		return "text/plain; charset=UTF-8";
	}

}
