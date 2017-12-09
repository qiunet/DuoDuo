package org.qiunet.flash.handler.handler;

import org.qiunet.utils.logger.LoggerManager;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.logger.log.QLogger;

/**
 * Created by qiunet.
 * 17/7/21
 */
public abstract class BaseHandler<RequestData> implements IHandler<RequestData> {
	protected QLogger logger = LoggerManager.getLogger(LoggerType.FLASH_HANDLER);

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
