package org.qiunet.flash.handler.context.request.http;

import io.netty.util.CharsetUtil;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.request.http.json.IResultResponse;
import org.qiunet.flash.handler.context.response.push.DefaultBytesMessage;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.utils.json.JsonUtil;

/**
 * 把请求解析为json对象
 * Created by qiunet.
 * 17/11/21
 */
public class HttpJsonRequestContext<RequestData, ResponseData extends IResultResponse> extends AbstractHttpRequestContext<RequestData, ResponseData> {

	public HttpJsonRequestContext(ISession session, MessageContent content) {
		this.init(session, content);
	}

	public void init(ISession session, MessageContent content) {
		super.init(session, content);
	}

	@Override
	protected DefaultBytesMessage getResponseDataMessage(ResponseData data) {
		return new DefaultBytesMessage(getHandler().getProtocolID(), JsonUtil.toJsonString(data).getBytes(CharsetUtil.UTF_8));
	}

	@Override
	protected String contentType() {
		return "application/json; charset=UTF-8";
	}

}
