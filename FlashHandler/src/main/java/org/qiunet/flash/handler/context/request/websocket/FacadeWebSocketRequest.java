package org.qiunet.flash.handler.context.request.websocket;


import io.netty.channel.Channel;

/**
 * Created by qiunet.
 * 17/12/2
 */
class FacadeWebSocketRequest<RequestData> implements IWebSocketRequest<RequestData> {
	private AbstractWebSocketRequestContext<RequestData, Object> context;
	public FacadeWebSocketRequest(AbstractWebSocketRequestContext<RequestData, Object> context) {
		this.context = context;
	}

	@Override
	public RequestData getRequestData() {
		return context.getRequestData();
	}

	@Override
	public String getRemoteAddress() {
		return context.getRemoteAddress();
	}

	@Override
	public Object getAttribute(String key) {
		return context.getAttribute(key);
	}

	@Override
	public void setAttribute(String key, Object val) {
		context.setAttribute(key, val);
	}

	@Override
	public Channel channel() {
		return context.channel();
	}

	@Override
	public void response(int protocolId, Object responseData) {
		if (responseData == null){
			throw new NullPointerException("ResponseData can not be null");
		}
		context.response(protocolId, responseData);
	}
}
