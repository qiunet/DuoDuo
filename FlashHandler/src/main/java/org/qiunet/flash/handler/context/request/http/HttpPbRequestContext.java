package org.qiunet.flash.handler.context.request.http;

import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.context.request.data.IChannelData;
import org.qiunet.flash.handler.context.response.push.DefaultProtobufMessage;
import org.qiunet.flash.handler.context.session.ISession;

/**
 * Created by qiunet.
 * 17/11/21
 */
public  class HttpPbRequestContext<RequestData extends IChannelData, ResponseData  extends IChannelData> extends AbstractHttpRequestContext<RequestData, ResponseData> {
	public HttpPbRequestContext(ISession session, MessageContent content) {
		this.init(session, content);
	}

	public void init(ISession session, MessageContent content) {
		super.init(session, content);
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
