package org.qiunet.flash.handler.bootstrap.hook;

import org.qiunet.flash.handler.netty.server.hook.ShutdownHook;
import org.qiunet.utils.logger.LoggerManager;
import org.qiunet.utils.logger.LoggerType;
import org.qiunet.utils.logger.log.QLogger;

/**
 * Created by qiunet.
 * 17/11/22
 */
public class MyShutdownHook implements ShutdownHook {
	QLogger qLogger = LoggerManager.getLogger(LoggerType.FLASH_HANDLER);
	@Override
	public void shutdown() {
		qLogger.error("Called MyShutdownHook");
	}
}
