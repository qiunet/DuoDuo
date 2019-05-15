package org.qiunet.flash.handler.handler;

import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by qiunet.
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
	public boolean needToken() {
		return true;
	}

	@Override
	public Class<RequestData> getRequestClass() {
		return requestDataClass;
	}
}
