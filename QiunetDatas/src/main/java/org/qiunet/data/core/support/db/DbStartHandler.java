package org.qiunet.data.core.support.db;

import org.qiunet.data.core.support.db.event.DbLoaderOverEventData;
import org.qiunet.listener.event.EventHandlerWeightType;
import org.qiunet.listener.event.EventListener;
import org.qiunet.listener.event.data.ServerStartupEventData;

/***
 * db 启动监听处理
 */
class DbStartHandler {

	@EventListener(EventHandlerWeightType.HIGHEST)
	public void onServerStartUp(ServerStartupEventData data) {
		DbLoader.getInstance();
		DbLoaderOverEventData.fireEvent();
	}
}
