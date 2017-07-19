package org.qiunet.flash.handler.context;

import org.qiunet.flash.handler.handler.IHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * @author qiunet
 *         Created on 17/3/13 19:50.
 */
abstract class BaseContext<RequestData> implements IContext<RequestData> {
	protected RequestData requestData;
	protected IHandler handler;
	private Map<String, Object> attributes;
	protected BaseContext(IHandler handler, RequestData requestData) {
		this.handler = handler;
		this.requestData = requestData;
		this.attributes = new HashMap<>();
	}
	@Override
	public IHandler getHandler() {
		return handler;
	}

	@Override
	public RequestData getRequestData() {
		return requestData;
	}

	@Override
	public boolean handler() {
//		acceptor.handler(this);
		return true;
	}

	@Override
	public Object getAttribute(String key) {
		return attributes.get(key);
	}
	@Override
	public void setAttribute(String key, Object val) {
		attributes.put(key, val);
	}
	@Override
	public String toStr() {
		return "RequestID ["+handler.getRequestID()+"]:"+requestData.toString();
	}
}
