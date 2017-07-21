package org.qiunet.flash.handler.handler;

/**
 * Created by qiunet.
 * 17/7/21
 */
abstract class BaseHandler<RequestData> implements IHandler<RequestData> {
	protected Class<RequestData> requestDataClass;
	protected int requestId;
	@Override
	public int getRequestID() {
		return requestId;
	}

	@Override
	public boolean needSid() {
		return true;
	}

	@Override
	public Class<RequestData> getRequestClass() {
		return requestDataClass;
	}
}
