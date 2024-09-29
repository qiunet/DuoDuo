package org.qiunet.data.core.support.db;

import org.qiunet.utils.listener.EventListener;
import org.qiunet.utils.listener.event_data.ServerStartEventData;

/***
 * db 启动监听处理
 */
class DbStartHandler {

	@EventListener
	public void eventHandler(ServerStartEventData eventData) {
		DbLoader.getInstance();
	}
}
