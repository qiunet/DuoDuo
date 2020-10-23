package org.qiunet.cross.test.event;

import org.qiunet.listener.event.EventListener;
import org.qiunet.utils.logger.LoggerType;

/***
 *
 *
 * @author qiunet
 * 2020-10-22 22:26
 */
public enum EventAcceptService {
	instance;

	@EventListener
	public void login(PlayerLoginEventData eventData) {
		LoggerType.DUODUO_CROSS.info("PlayerId: {},登录事件", eventData.getPlayer().getId());
	}

	@EventListener
	public void crossLogin(CrossPlayerLoginEventData eventData) {
		LoggerType.DUODUO_CROSS.info("PlayerId: {},跨服登录事件", eventData.getPlayer().getId());
	}
}
