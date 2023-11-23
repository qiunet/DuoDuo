package org.qiunet.flash.handler.context.request.http;

import io.netty.util.CharsetUtil;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.response.push.DefaultBytesMessage;
import org.qiunet.flash.handler.context.session.ISession;

/**
 * 把请求解析为string的对象
 * Created by qiunet.
 * 17/11/21
 */
public class HttpStringRequestContext extends AbstractHttpRequestContext<String, String> {

	public HttpStringRequestContext(ISession session, MessageContent content) {
		this.init(session, content);
	}
	public void init(ISession session, MessageContent content) {
		super.init(session, content);
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
