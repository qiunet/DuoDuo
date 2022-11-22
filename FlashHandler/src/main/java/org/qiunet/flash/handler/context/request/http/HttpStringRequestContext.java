package org.qiunet.flash.handler.context.request.http;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.CharsetUtil;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.response.push.DefaultBytesMessage;
import org.qiunet.flash.handler.netty.server.config.ServerBootStrapConfig;

/**
 * 把请求解析为string的对象
 * Created by qiunet.
 * 17/11/21
 */
public class HttpStringRequestContext extends AbstractHttpRequestContext<String, String> {

	public HttpStringRequestContext(MessageContent content, Channel channel, ServerBootStrapConfig config, HttpRequest request) {
		this.init(content, channel, config, request);
	}
	public void init(MessageContent content, Channel channel, ServerBootStrapConfig config, HttpRequest request) {
		super.init(content, channel, config, request);
	}

	@Override
	protected DefaultBytesMessage getResponseDataMessage(String s) {
		return new DefaultBytesMessage(getHandler().getProtocolID(), s.getBytes(CharsetUtil.UTF_8));
	}

	@Override
	protected String contentType() {
		return "text/plain; charset=UTF-8";
	}

}
