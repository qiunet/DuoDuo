package org.qiunet.flash.handler.context.request.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.CharsetUtil;
import org.qiunet.flash.handler.context.header.MessageContent;

/**
 * 把请求解析为string的对象
 * Created by qiunet.
 * 17/11/21
 */
public class HttpStringRequest extends AbstractHttpRequest<String, String> {
	private String reqeustData;

	public HttpStringRequest(MessageContent content, ChannelHandlerContext channelContext, HttpRequest request) {
		super(content, channelContext, request);
	}


	@Override
	public String getRequestData() {
		if (reqeustData == null) {
			reqeustData = new String(bytes, CharsetUtil.UTF_8);
		}
		return reqeustData;
	}
	@Override
	protected byte[] getResponseDataBytes(String s) {
		return s.getBytes(CharsetUtil.UTF_8);
	}

	@Override
	public boolean handler() {
		return false;
	}

	@Override
	public String toStr() {
		return "request: "+reqeustData;
	}
}
