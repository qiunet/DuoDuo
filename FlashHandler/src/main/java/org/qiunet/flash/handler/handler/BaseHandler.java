package org.qiunet.flash.handler.handler;

import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

/**
 * @author qiunet.
 * 17/7/21
 */
public abstract class BaseHandler<RequestData> implements IHandler<RequestData> {
	protected Logger logger = LoggerType.DUODUO.getLogger();

	private Class<RequestData> requestDataClass;
	private int protocolId;
	@Override
	public int getProtocolID() {
		return protocolId;
	}

	@Override
	public boolean needAuth() {
		return true;
	}

	@Override
	public Class<RequestData> getRequestClass() {
		return requestDataClass;
	}
}
