package org.qiunet.flash.handler.handler;

/**
 * Created by qiunet.
 * 17/7/21
 */
public abstract class BaseHandler<RequestData> implements IHandler<RequestData> {
	private Class<RequestData> requestDataClass;
	private int protocolId;
	@Override
	public int getProtocolID() {
		return protocolId;
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
