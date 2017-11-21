package org.qiunet.flash.handler.context.request.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.CharsetUtil;
import org.qiunet.flash.handler.context.header.MessageContent;

import java.lang.reflect.InvocationTargetException;

/**
 * 把请求解析为string的对象
 * Created by qiunet.
 * 17/11/21
 */
public class HttpStringRequestContext extends AbstractHttpRequestContext<String, String> {
	private String reqeustData;

	public HttpStringRequestContext(MessageContent content, ChannelHandlerContext channelContext, HttpRequest request) {
		super(content, channelContext, request);
	}

	@Override
	public String getRequestData() {
		if (reqeustData == null) {
			try {
				reqeustData = getHandler().parseRequestData(bytes);
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
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
	protected String contentType() {
		return "text/plain; charset=UTF-8";
	}

	@Override
	public String toStr() {
		return "request: "+reqeustData;
	}
}
