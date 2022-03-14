package org.qiunet.test.cross.common.service;

import org.qiunet.test.cross.common.event.CrossNodeEvent;
import org.qiunet.utils.listener.event.EventListener;
import org.qiunet.utils.logger.LoggerType;

/***
 *
 * @author qiunet
 * 2021/11/30 18:54
 */
public enum CrossService {
	instance;


	@EventListener
	private void nodeEvent(CrossNodeEvent event) {
		LoggerType.DUODUO_FLASH_HANDLER.info("Cross服. ServerNode事件触发. {}!======", event.getPlayerId());
	}
}
