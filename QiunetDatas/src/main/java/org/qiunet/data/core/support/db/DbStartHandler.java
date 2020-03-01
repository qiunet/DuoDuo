package org.qiunet.data.core.support.db;

import org.qiunet.utils.listener.EventHandlerWeight;
import org.qiunet.utils.listener.EventHandlerWeightType;
import org.qiunet.utils.listener.data.ServerStartUpEventData;

/***
 * db 启动监听处理
 */
class DbStartHandler implements ServerStartUpEventData.ServerStartUpListener {

	@Override
	@EventHandlerWeight(EventHandlerWeightType.HIGHEST)
	public void onServerStartUp(ServerStartUpEventData data) {
		DbLoader.getInstance();
	}
}
